package de.rheinfabrik.heimdalldroid.actvities;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.rheinfabrik.heimdalldroid.R;
import de.rheinfabrik.heimdalldroid.network.oauth2.TraktTvAuthorizationCodeGrant;
import de.rheinfabrik.heimdalldroid.network.oauth2.TraktTvOauth2AccessTokenManager;
import de.rheinfabrik.heimdalldroid.utils.AlertDialogFactory;
import de.rheinfabrik.heimdalldroid.utils.rx.RxAppCompatActivity;
import rx.android.schedulers.AndroidSchedulers;

import static rx.android.lifecycle.LifecycleObservable.bindActivityLifecycle;

/**
 * Activity used to let the user login with his GitHub credentials.
 * You may want to move most of this code to your presenter class or view model.
 * For the sake of simplicity the code is inside the activity.
 */
public class LoginActivity extends RxAppCompatActivity {

    // Members

    @InjectView(R.id.webView)
    protected WebView mWebView;

    // Activity lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set content view
        setContentView(R.layout.activity_login);

        // Inject views
        ButterKnife.inject(this);

        // Start authorization
        authorize();
    }

    // Private Api

    private void authorize() {

        // Grab a new token manger
        TraktTvOauth2AccessTokenManager tokenManager = TraktTvOauth2AccessTokenManager.from(this);

        // Grab a new grant
        TraktTvAuthorizationCodeGrant grant = tokenManager.newAuthorizationCodeGrant();

        // Listen for the authorization url and load it once needed
        bindActivityLifecycle(lifecycle(), grant.authorizationUri())
                .map(Uri::toString)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mWebView::loadUrl);

        // Sent loaded website to grant so it can check if we have an access token
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                grant.onUriLoadedCommand.onNext(Uri.parse(url));

                // Hide redirect page from user
                if (url.startsWith(grant.redirectUri)) {
                    mWebView.setVisibility(View.GONE);
                }
            }
        });

        // Start authorization and listen for success
        bindActivityLifecycle(lifecycle(), tokenManager.grantNewAccessToken(grant))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x -> handleSuccess(), x -> handleError());
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
