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

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ge.protein.data.model.User;
import com.ge.protein.user.followee.FolloweeListFragment;
import com.ge.protein.user.followee.FolloweeListPresenter;
import com.ge.protein.user.follower.FollowerListFragment;
import com.ge.protein.user.follower.FollowerListPresenter;
import com.ge.protein.user.shot.UserShotListFragment;
import com.ge.protein.user.shot.UserShotListPresenter;
import com.ge.protein.user.shotlike.ShotLikeListFragment;
import com.ge.protein.user.shotlike.ShotLikeListPresenter;

class UserPagerAdapter extends FragmentPagerAdapter {

    private User user;

    UserPagerAdapter(FragmentManager fm, User user) {
        super(fm);
        this.user = user;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                UserShotListFragment userShotListFragment = UserShotListFragment.newInstance();
                new UserShotListPresenter(userShotListFragment, user);
                return userShotListFragment;
            case 1:
                ShotLikeListFragment shotLikeListFragment = ShotLikeListFragment.newInstance();
                new ShotLikeListPresenter(shotLikeListFragment, user);
                return shotLikeListFragment;
            case 2:
                FollowerListFragment followerListFragment = FollowerListFragment.newInstance();
                new FollowerListPresenter(followerListFragment, user);
                return followerListFragment;
            case 3:
                FolloweeListFragment followeeListFragment = FolloweeListFragment.newInstance();
                new FolloweeListPresenter(followeeListFragment, user);
                return followeeListFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
