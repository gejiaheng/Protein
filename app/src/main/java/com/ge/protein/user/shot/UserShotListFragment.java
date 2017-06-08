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
package com.ge.protein.user.shot;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ge.protein.mvp.ListFragment;

public class UserShotListFragment extends ListFragment {

    public static UserShotListFragment newInstance() {
        return new UserShotListFragment();
    }

    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        proteinAdapter.setSpanCount(2);
        gridLayoutManager.setSpanSizeLookup(proteinAdapter.getSpanSizeLookup());
        return gridLayoutManager;
    }

    @Override
    public boolean hasToolbar() {
        return false;
    }

    @Override
    protected void onPreStart(@NonNull Bundle savedInstanceState) {
        super.onPreStart(savedInstanceState);
        listPresenter = new UserShotListPresenter(this);
        listPresenter.onRestoreInstanceState(savedInstanceState);
    }
}
