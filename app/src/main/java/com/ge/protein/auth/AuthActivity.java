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
package com.ge.protein.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ge.protein.R;
import com.ge.protein.firebase.FirebaseCrashUtils;
import com.ge.protein.ui.activity.BaseProteinActivity;

public class AuthActivity extends BaseProteinActivity {

    private AuthPresenter authPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseCrashUtils.log("AuthActivity created");

        setContentView(R.layout.activity_simple_fragment);

        AuthFragment authFragment = (AuthFragment) getSupportFragmentManager()
                .findFragmentByTag(AuthFragment.class.getSimpleName());
        if (authFragment == null) {
            authFragment = AuthFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, authFragment, AuthFragment.class.getSimpleName())
                    .commit();
        }

        authPresenter = new AuthPresenter(authFragment);
    }
}
