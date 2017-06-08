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
package com.ge.protein.shot;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.ge.protein.R;
import com.ge.protein.data.model.Shot;
import com.ge.protein.firebase.FirebaseCrashUtils;
import com.ge.protein.ui.activity.BaseProteinActivity;

@DeepLink("https://dribbble.com/shots/{id}")
public class ShotActivity extends BaseProteinActivity {

    public static final String EXTRA_SHOT = "extra_shot";

    private ShotPresenter shotPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseCrashUtils.log("ShotActivity created");

        setContentView(R.layout.activity_simple_fragment);

        Intent intent = getIntent();
        boolean isDeepLink = intent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false);
        long shotId;
        Shot shot;
        // create view
        ShotFragment shotFragment = (ShotFragment) getSupportFragmentManager()
                .findFragmentByTag(ShotFragment.class.getSimpleName());
        if (shotFragment == null) {
            shotFragment = ShotFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, shotFragment, ShotFragment.class.getSimpleName())
                    .commit();
        }
        // hook presenter
        if (isDeepLink) {
            String id = intent.getExtras().getString("id");
            // https://dribbble.com/shots/3495164-Google-people and https://dribbble.com/shots/3495164
            // are both valid
            int dashIndex = TextUtils.isEmpty(id) ? -1 : id.indexOf("-");
            if (dashIndex != -1) {
                id = id.substring(0, dashIndex);
            }
            shotId = Long.parseLong(id);
            shotPresenter = new ShotPresenter(shotFragment, shotId);
        } else {
            shot = intent.getParcelableExtra(EXTRA_SHOT);
            shotPresenter = new ShotPresenter(shotFragment, shot);
        }
        if (savedInstanceState != null) {
            shotPresenter.onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        shotPresenter.onSaveInstanceState(outState);
    }

}
