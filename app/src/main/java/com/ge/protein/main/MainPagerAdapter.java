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
package com.ge.protein.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ge.protein.main.shot.ShotListFragment;
import com.ge.protein.main.shot.ShotListPresenter;
import com.ge.protein.util.AccountManager;

import java.util.ArrayList;
import java.util.List;

import static com.ge.protein.main.shot.ShotListPresenter.DEBUTS;
import static com.ge.protein.main.shot.ShotListPresenter.FOLLOWING;
import static com.ge.protein.main.shot.ShotListPresenter.POPULAR;
import static com.ge.protein.main.shot.ShotListPresenter.RECENT;

class MainPagerAdapter extends FragmentPagerAdapter {

    private List<Integer> fragments = new ArrayList<>();

    MainPagerAdapter(FragmentManager fm) {
        super(fm);
        if (AccountManager.getInstance().isLogin()) {
            fragments.add(POPULAR);
            fragments.add(FOLLOWING);
            fragments.add(RECENT);
            fragments.add(DEBUTS);
        } else {
            fragments.add(POPULAR);
            fragments.add(RECENT);
            fragments.add(DEBUTS);
        }
    }

    @Override
    public Fragment getItem(int position) {
        ShotListFragment fragment = ShotListFragment.newInstance();
        new ShotListPresenter(fragments.get(position), fragment);
        return fragment;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
