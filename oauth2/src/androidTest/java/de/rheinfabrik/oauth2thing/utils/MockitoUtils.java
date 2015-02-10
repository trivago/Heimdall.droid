package de.rheinfabrik.oauth2thing.utils;

import android.test.InstrumentationTestCase;

public class MockitoUtils {

    public static void enableMockito(InstrumentationTestCase testCase) {
        System.setProperty("dexmaker.dexcache", testCase.getInstrumentation().getTargetContext().getCacheDir().getPath());
    }
}
