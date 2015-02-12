package de.rheinfabrik.oauth2.utils;

import android.test.InstrumentationTestCase;

import org.mockito.MockitoAnnotations;

public class MockitoTestCase extends InstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        MockitoUtils.enableMockito(this);
        MockitoAnnotations.initMocks(this);
    }

}
