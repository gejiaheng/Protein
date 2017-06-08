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
package com.ge.protein.about;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ge.protein.R;
import com.ge.protein.firebase.FirebaseCrashUtils;
import com.ge.protein.ui.activity.BaseProteinActivity;

public class AboutActivity extends BaseProteinActivity {

    private AboutPresenter aboutPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseCrashUtils.log("AboutActivity created");

        setContentView(R.layout.activity_simple_fragment);

        AboutFragment aboutFragment = (AboutFragment) getSupportFragmentManager()
                .findFragmentByTag(AboutFragment.class.getSimpleName());
        if (aboutFragment == null) {
            aboutFragment = AboutFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, aboutFragment, AboutFragment.class.getSimpleName())
                    .commit();
        }

        aboutPresenter = new AboutPresenter(aboutFragment);
    }
}
