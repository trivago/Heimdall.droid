package de.rheinfabrik.heimdall.utils;

import android.test.InstrumentationTestCase;

public class MockitoUtils {

    // Public Api

    public static void enableMockito(InstrumentationTestCase testCase) {
        System.setProperty("dexmaker.dexcache", testCase.getInstrumentation().getTargetContext().getCacheDir().getPath());
    }
}
