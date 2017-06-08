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
package com.ge.protein.user.shotlike;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.ge.protein.mvp.ListFragment;

public class ShotLikeListFragment extends ListFragment {

    public static ShotLikeListFragment newInstance() {
        return new ShotLikeListFragment();
    }

    @Override
    public boolean hasToolbar() {
        return false;
    }

    @Override
    protected void onPreStart(@NonNull Bundle savedInstanceState) {
        super.onPreStart(savedInstanceState);
        listPresenter = new ShotLikeListPresenter(this);
        listPresenter.onRestoreInstanceState(savedInstanceState);
    }
}
