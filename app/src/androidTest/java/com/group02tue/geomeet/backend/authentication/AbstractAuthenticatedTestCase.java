package com.group02tue.geomeet.backend.authentication;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.Before;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.fail;

@RunWith(AndroidJUnit4.class)
public abstract class AbstractAuthenticatedTestCase {
    private final static String USERNAME = "test";
    private final static String PASSWORD = "test";
    private final static int LOGIN_TIMEOUT = 2000;  // in ms

    private CountDownLatch lock = new CountDownLatch(1);

    protected String authenticationKey = "";
    protected Context applicationContext;
    protected AuthenticationManager authenticationManager;

    @Before
    public void constructor() throws InterruptedException {
        applicationContext = InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
        authenticationManager = new AuthenticationManager(applicationContext);
        authenticationManager.reset();  // Start without any mess

        authenticationManager.login(USERNAME, PASSWORD, new AuthenticationEventListener() {
            @Override
            public void onSuccess() {
                lock.countDown();
            }

            @Override
            public void onFailure() {
                fail("Failed to login");
            }
        });
        lock.await(LOGIN_TIMEOUT, TimeUnit.MILLISECONDS);
        if (lock.getCount() > 0) {
            fail("Failed to login, timeout");
        }
    }
}
