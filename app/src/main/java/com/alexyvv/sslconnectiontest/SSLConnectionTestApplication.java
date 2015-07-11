package com.alexyvv.sslconnectiontest;

import android.app.Application;
import android.content.Context;

import com.alexyvv.sslconnectiontest.fragment.TabSlideFragment;

/**
 * Created by alexy on 09.07.15.
 */
public class SSLConnectionTestApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {

        super.onCreate();
        SSLConnectionTestApplication.mContext = getApplicationContext();
    }

    /**
     * Получить контекст работы приложения.
     * @return
     */
    public static Context getAppContext() {

        return mContext;
    }
}
