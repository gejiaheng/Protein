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

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.ge.protein.data.model.User;
import com.ge.protein.mvp.InstanceStatePresenter;
import com.ge.protein.util.AccountManager;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.ge.protein.util.Preconditions.checkNotNull;

class UserPresenter implements UserContract.Presenter, InstanceStatePresenter {

    private static final String STATE_FOLLOWING_CHECKED = "state_following_checked";
    private static final String STATE_FOLLOWING = "state_following";

    @NonNull
    private UserContract.View view;
    private UserRepository repository;
    @NonNull
    private User user;
    private boolean followingChecked;
    private boolean following;

    UserPresenter(@NonNull UserContract.View view, @NonNull User user) {
        this.view = checkNotNull(view, "view cannot be null");
        this.view.setPresenter(this);
        repository = new UserRepository();
        this.user = checkNotNull(user, "user cannot be null");
    }

    @Override
    public void start() {
        view.setupView(user);
        if (AccountManager.getInstance().getMe() == null || AccountManager.getInstance().getMe().equals(user)) {
            view.setFollowButtonVisibility(false);
        } else {
            view.setFollowButtonVisibility(true);
            if (followingChecked) {
                view.setFollowButtonEnabled(true);
                view.setFollowing(following);
            } else {
                repository.isFollowing(user.id())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(((LifecycleProvider<ActivityEvent>) view.getContext())
                                .bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribe(response -> {
                            followingChecked = true;
                            view.setFollowButtonEnabled(true);
                            if (response.code() == 204) {
                                following = true;
                                view.setFollowing(true);
                            } else if (response.code() == 404) {
                                following = false;
                                view.setFollowing(false);
                            }
                        }, throwable -> {
                            view.setFollowButtonEnabled(true);
                            view.setFollowing(false);
                        });
            }
        }
        view.showUser(user);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(STATE_FOLLOWING_CHECKED, followingChecked);
        if (followingChecked) {
            outState.putBoolean(STATE_FOLLOWING, following);
        }
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        followingChecked = savedInstanceState.getBoolean(STATE_FOLLOWING_CHECKED);
        if (followingChecked) {
            following = savedInstanceState.getBoolean(STATE_FOLLOWING);
        }
    }

    @Override
    public void back() {
        ((Activity) view.getContext()).onBackPressed();
    }

    @Override
    public void toggleFollow() {
        if (following) {
            repository.unfollow(user.id());
            view.setFollowing(false);
        } else {
            repository.follow(user.id());
            view.setFollowing(true);
        }
        following = !following;
    }

    @Override
    public void toMap() {
        try {
            Uri data = Uri.parse("geo:0,0?q=" + Uri.encode(user.location()));
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(data);
            view.getContext().startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void toTwitter() {
        actionView(user.links().twitter());
    }

    @Override
    public void toWeb() {
        actionView(user.links().web());
    }

    private void actionView(String url) {
        if (TextUtils.isEmpty(url))
            return;

        try {
            view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
