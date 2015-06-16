package de.rheinfabrik.heimdalldroid.actvities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.rheinfabrik.heimdalldroid.R;
import de.rheinfabrik.heimdalldroid.adapter.TraktTvListsRecyclerViewAdapter;
import de.rheinfabrik.heimdalldroid.network.TraktTvApiFactory;
import de.rheinfabrik.heimdalldroid.network.models.TraktTvList;
import de.rheinfabrik.heimdalldroid.network.oauth2.TraktTvOauth2AccessTokenManager;
import de.rheinfabrik.heimdalldroid.utils.AlertDialogFactory;
import de.rheinfabrik.heimdalldroid.utils.IntentFactory;
import de.rheinfabrik.heimdalldroid.utils.rx.RxAppCompatActivity;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

import static rx.android.lifecycle.LifecycleObservable.bindActivityLifecycle;

/**
 * Activity showing either the list of the user's repositories or the login screen.
 * You may want to move most of this code to your presenter class or view model.
 * For the sake of simplicity the code is inside the activity.
 */
public class MainActivity extends RxAppCompatActivity {

    // Constants

    private static final int AUTHORIZATION_REQUEST_CODE = 1;

    // Members

    @InjectView(R.id.recyclerView)
    protected RecyclerView mRecyclerView;

    @InjectView(R.id.toolbar)
    protected Toolbar mToolbar;

    @InjectView(R.id.swipeRefreshLayout)
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    private TraktTvOauth2AccessTokenManager mTokenManager;

    // Activity lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set content view
        setContentView(R.layout.activity_main);

        // Inject views
        ButterKnife.inject(this);

        // Setup toolbar
        setSupportActionBar(mToolbar);

        // Setup swipe refresh layout
        mSwipeRefreshLayout.setOnRefreshListener(MainActivity.this::refresh);

        // Setup recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        // Grab a new manager
        mTokenManager = TraktTvOauth2AccessTokenManager.from(this);

        // Check if we are logged in
        refresh();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the request code is correct
        if (requestCode == AUTHORIZATION_REQUEST_CODE) {

            // Check if login was successful
            if (resultCode == Activity.RESULT_OK) {
                loadLists();
            }
        }
    }

    // Private Api

    private void loadLists() {

        // Load the lists
        Observable<List<TraktTvList>> listsObservable = mTokenManager

                 /* Grab a valid access token (automatically refreshes the token if it is expired) */
                .getValidAccessToken()

                 /* Load lists */
                .concatMap(authorizationHeader -> TraktTvApiFactory.newApiService().getLists(authorizationHeader));

        // Bind to lifecycle
        bindActivityLifecycle(lifecycle(), listsObservable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lists -> {
                    if (lists == null || lists.isEmpty()) {
                        handleEmptyList();
                    } else {
                        handleSuccess(lists);
                    }
                }, x -> handleError());
    }

    // Start the LoginActivity.
    private void showLogin() {
        mSwipeRefreshLayout.setRefreshing(false);

        Intent loginIntent = IntentFactory.loginIntent(this);
        startActivityForResult(loginIntent, AUTHORIZATION_REQUEST_CODE);
    }

    // Shows a dialog saying that there were no lists.
    private void handleEmptyList() {
        mSwipeRefreshLayout.setRefreshing(false);

        AlertDialogFactory.noListsFoundDialog(this).show();
    }

    // Show an error dialog
    private void handleError() {
        mSwipeRefreshLayout.setRefreshing(false);

        AlertDialogFactory.errorAlertDialog(this).show();
    }

    // Update our recycler view
    private void handleSuccess(List<TraktTvList> traktTvLists) {
        mSwipeRefreshLayout.setRefreshing(false);

        mRecyclerView.setAdapter(new TraktTvListsRecyclerViewAdapter(traktTvLists));
    }

    // Check if logged in and show either login or load lists
    private void refresh() {
        mSwipeRefreshLayout.setRefreshing(true);

        mTokenManager.getStorage().hasAccessToken()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loggedIn -> {
                    if (loggedIn) {
                        loadLists();
                    } else {
                        showLogin();
                    }
                });
    }
}
