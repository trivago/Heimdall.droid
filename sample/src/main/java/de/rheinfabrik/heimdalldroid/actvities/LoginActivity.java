package de.rheinfabrik.heimdalldroid.actvities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import de.rheinfabrik.heimdalldroid.R;
import de.rheinfabrik.heimdalldroid.network.oauth2.TraktTvAuthorizationCodeGrant;
import de.rheinfabrik.heimdalldroid.network.oauth2.TraktTvOauth2AccessTokenManager;
import de.rheinfabrik.heimdalldroid.utils.AlertDialogFactory;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Activity used to let the user login with his GitHub credentials.
 * You may want to move most of this code to your presenter class or view model.
 * For the sake of simplicity the code is inside the activity.
 */
public class LoginActivity extends AppCompatActivity {

    // Members
    private WebView mWebView;
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    // Activity lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set content view
        setContentView(R.layout.activity_login);

        mWebView = findViewById(R.id.webView);

        // Start authorization
        authorize();
    }

    @Override
    protected void onDestroy() {
        mCompositeDisposable.clear();
        super.onDestroy();
    }

    // Private Api

    private void authorize() {

        // Grab a new token manger
        TraktTvOauth2AccessTokenManager tokenManager = TraktTvOauth2AccessTokenManager.from(this);

        // Grab a new grant
        TraktTvAuthorizationCodeGrant grant = tokenManager.newAuthorizationCodeGrant();

        // Listen for the authorization url and load it once needed
        mCompositeDisposable.add(
            grant.authorizationUri()
                .map(URL::toString)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mWebView::loadUrl)
        );


        // Sent loaded website to grant so it can check if we have an access token
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String urlString, Bitmap favicon) {
                super.onPageStarted(view, urlString, favicon);

                try {
                    URL url = new URL(urlString);
                    grant.getOnUrlLoadedCommand().onNext(url);

                    // Hide redirect page from user
                    if (urlString.startsWith(grant.getRedirectUri())) {
                        mWebView.setVisibility(View.GONE);
                    }
                } catch (MalformedURLException ignored) {
                    // Empty
                }
            }
        });

        // Start authorization and listen for success
        mCompositeDisposable.add(
            tokenManager.grantNewAccessToken(grant, Calendar.getInstance())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x -> handleSuccess(), x -> handleError())
        );
    }

    // Set the result to ok and finish this activity
    private void handleSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    // Build an error dialog and show it
    private void handleError() {
        AlertDialogFactory.errorAlertDialog(this).show();
    }
}
