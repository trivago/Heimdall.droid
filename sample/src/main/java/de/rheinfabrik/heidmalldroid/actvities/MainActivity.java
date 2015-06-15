package de.rheinfabrik.heidmalldroid.actvities;

import android.app.Activity;
import android.os.Bundle;

import de.rheinfabrik.heimdall.OAuth2AccessToken;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OAuth2AccessToken token = new OAuth2AccessToken();
        token.accessToken = "access fokken";
    }
}
