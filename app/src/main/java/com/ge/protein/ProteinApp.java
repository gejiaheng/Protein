/*
 * Copyright 2017 Jiaheng Ge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ge.protein;

import android.app.Application;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.facebook.stetho.Stetho;
import com.ge.protein.data.api.ServiceGenerator;
import com.ge.protein.util.AccountManager;
import com.ge.protein.util.Constants;
import com.squareup.leakcanary.RefWatcher;

import butterknife.ButterKnife;
import hugo.weaving.DebugLog;

public class ProteinApp extends Application {

    private RefWatcher refWatcher;

    @DebugLog
    @Override
    public void onCreate() {
        super.onCreate();
        ServiceGenerator.init(this);
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
        initAccessToken();
        ButterKnife.setDebug(BuildConfig.DEBUG);
        refWatcher = installLeakCanary();
    }

    private void initAccessToken() {
        SharedPreferences sp = getSharedPreferences(Constants.DEFAULT_SHARED_PREFERENCES, MODE_PRIVATE);
        String accessToken = sp.getString(Constants.ACCESS_TOKEN_KEY, null);
        if (!TextUtils.isEmpty(accessToken)) {
            AccountManager.getInstance().setAccessToken(accessToken);
        }
    }

    protected RefWatcher installLeakCanary() {
        return RefWatcher.DISABLED;
    }

}
