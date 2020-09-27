package de.rheinfabrik.heimdalldroid.actvities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.CookieManager
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import de.rheinfabrik.heimdalldroid.R
import de.rheinfabrik.heimdalldroid.adapter.TraktTvListsRecyclerViewAdapter
import de.rheinfabrik.heimdalldroid.network.TraktTvApiFactory
import de.rheinfabrik.heimdalldroid.network.models.TraktTvList
import de.rheinfabrik.heimdalldroid.network.oauth2.rxjava.TraktTvOauth2AccessTokenManager
import de.rheinfabrik.heimdalldroid.utils.AlertDialogFactory
import de.rheinfabrik.heimdalldroid.utils.IntentFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException

class MainActivity : AppCompatActivity() {

    companion object {
        // Constants
        private const val AUTHORIZATION_REQUEST_CODE = 1
    }

    // Members
    private val mCompositeDisposable = CompositeDisposable()
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mTokenManager: TraktTvOauth2AccessTokenManager
    private var useCoroutines = true

    // Activity lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set content view
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        mRecyclerView = findViewById(R.id.recyclerView)
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        mRecyclerView = findViewById(R.id.recyclerView)

        // Setup toolbar
        setActionBar(toolbar)

        // Setup swipe refresh layout
        mSwipeRefreshLayout.setOnRefreshListener { refresh() }

        // Setup recycler view
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        mRecyclerView.layoutManager = layoutManager

        // Grab a new manager
        mTokenManager = TraktTvOauth2AccessTokenManager.from(this)

        // Check if we are logged in
        refresh()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Check if the request code is correct and if login was successful
        if (resultCode == RESULT_OK &&
            requestCode == AUTHORIZATION_REQUEST_CODE) {
            loadLists()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            logout()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        mCompositeDisposable.clear()
        super.onDestroy()
    }

    // Private Api
    private fun loadLists() {

        // Load the lists
        val listsObservable = mTokenManager /* Grab a valid access token (automatically refreshes the token if it is expired) */
            .validAccessToken /* Load lists */
            .flatMapObservable { authorizationHeader: String? -> TraktTvApiFactory.newApiServiceRxJava().getLists(authorizationHeader) }
        mCompositeDisposable.add(
            listsObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ lists: List<TraktTvList>? ->
                    if (lists == null || lists.isEmpty()) {
                        handleEmptyList()
                    } else {
                        handleSuccess(lists)
                    }
                }) { error: Throwable -> handleError(error) }
        )
    }

    // Start the LoginActivity.
    private fun showLogin() {
        mSwipeRefreshLayout.isRefreshing = false
        val loginIntent = IntentFactory.loginIntent(this)
        startActivityForResult(loginIntent, AUTHORIZATION_REQUEST_CODE)
    }

    // Shows a dialog saying that there were no lists.
    private fun handleEmptyList() {
        mSwipeRefreshLayout.isRefreshing = false
        AlertDialogFactory.noListsFoundDialog(this).show()
    }

    // Show an error dialog
    private fun handleError(error: Throwable) {
        mSwipeRefreshLayout.isRefreshing = false

        // Clear token and login if 401
        if (error is HttpException) {
            if (error.code() == 401) {
                mTokenManager.getStorage().removeAccessToken()
                refresh()
            }
        } else {
            AlertDialogFactory.errorAlertDialog(this).show()
        }
    }

    // Update our recycler view
    private fun handleSuccess(traktTvLists: List<TraktTvList>) {
        mSwipeRefreshLayout.isRefreshing = false
        mRecyclerView.adapter = TraktTvListsRecyclerViewAdapter(traktTvLists)
    }

    // Check if logged in and show either login or load lists
    private fun refresh() {
        mSwipeRefreshLayout.isRefreshing = true

        mCompositeDisposable.add(
            mTokenManager.getStorage().hasAccessToken()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { loggedIn: Boolean ->
                    if (loggedIn) {
                        loadLists()
                    } else {
                        showLogin()
                    }
                }
        )
    }

    // Logs out the user
    private fun logout() {

        // Ask token manager to revoke the token
        mCompositeDisposable.add(
            mTokenManager
                .logout()
                .subscribe { x: Void? -> showLogin() }
        )

        // Clear webview cache
        val cookieManager = CookieManager.getInstance()
        cookieManager.removeAllCookies(null)
    }
}
