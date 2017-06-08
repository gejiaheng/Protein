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
package com.ge.protein.user;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ge.protein.R;
import com.ge.protein.data.model.User;
import com.ge.protein.firebase.FirebaseCrashUtils;
import com.ge.protein.ui.activity.BaseProteinActivity;

public class UserActivity extends BaseProteinActivity {

    public static final String EXTRA_USER = "extra_user";

    private User user;
    private UserPresenter userPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseCrashUtils.log("UserActivity created");

        user = getIntent().getParcelableExtra(EXTRA_USER);
        setContentView(R.layout.activity_user);

        userPresenter = new UserPresenter((UserView)findViewById(R.id.user_view), user);
        if (savedInstanceState != null) {
            userPresenter.onRestoreInstanceState(savedInstanceState);
        }
        userPresenter.start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        userPresenter.onSaveInstanceState(outState);
    }
}
