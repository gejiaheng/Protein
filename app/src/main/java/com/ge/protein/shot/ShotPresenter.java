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
import android.support.annotation.NonNull;

import com.ge.protein.R;
import com.ge.protein.comment.CommentsActivity;
import com.ge.protein.data.model.Shot;
import com.ge.protein.data.model.ShotLike;
import com.ge.protein.mvp.InstanceStatePresenter;
import com.ge.protein.user.UserActivity;
import com.ge.protein.util.AccountManager;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.FragmentEvent;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.ge.protein.util.Preconditions.checkNotNull;

class ShotPresenter implements ShotContract.Presenter, InstanceStatePresenter {

    private boolean isDeepLink;
    private Shot shot;
    private long shotId;
    @NonNull
    private ShotContract.View view;
    private ShotRepository repository;
    private boolean likeChecked;
    private boolean like;

    private static final String STATE_LIKE_CHECKED = "state_like_checked";
    private static final String STATE_LIKE = "state_like";
    private static final String STATE_SHOT = "state_shot";

    ShotPresenter(@NonNull ShotContract.View view, @NonNull Shot shot) {
        isDeepLink = false;
        this.view = checkNotNull(view, "view cannot be null");
        this.view.setPresenter(this);
        this.shot = checkNotNull(shot, "shot cannot be null");
        shotId = this.shot.id();
        repository = new ShotRepository();
    }

    ShotPresenter(@NonNull ShotContract.View view, long shotId) {
        isDeepLink = true;
        this.view = checkNotNull(view, "view cannot be null");
        this.view.setPresenter(this);
        this.shotId = shotId;
        repository = new ShotRepository();
    }

    @Override
    public void start() {
        if (isDeepLink) {
            if (shot == null) {
                repository.getShot(shotId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(((LifecycleProvider<FragmentEvent>) view).bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                        .subscribe(shotResponse -> {
                            shot = shotResponse.body();
                            view.show(shot);
                        }, throwable -> {

                        });
            } else {
                view.show(shot);
            }
        } else {
            view.show(shot);
        }
        if (AccountManager.getInstance().isLogin()) {
            checkLike();
        } else {
            view.setLikeFabVisibility(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (isDeepLink && shot != null) {
            outState.putParcelable(STATE_SHOT, shot);
        }
        outState.putBoolean(STATE_LIKE_CHECKED, likeChecked);
        if (likeChecked) {
            outState.putBoolean(STATE_LIKE, like);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (isDeepLink) {
            shot = savedInstanceState.getParcelable(STATE_SHOT);
        }
        likeChecked = savedInstanceState.getBoolean(STATE_LIKE_CHECKED);
        if (likeChecked) {
            like = savedInstanceState.getBoolean(STATE_LIKE);
        }
    }

    @Override
    public void checkLike() {
        if (likeChecked) {
            view.setLikeStatus(like);
            view.setLikeFabVisibility(true);
            return;
        }

        repository.checkLike(shotId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(((LifecycleProvider<FragmentEvent>) view).bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(shotLikeResponse -> {
                    likeChecked = true;
                    ShotLike shotLike = shotLikeResponse.body();
                    view.setLikeStatus(like = (shotLike != null));
                    view.setLikeFabVisibility(true);
                }, throwable -> view.setLikeFabVisibility(true));
    }

    @Override
    public void toggleLike() {
        if (!AccountManager.getInstance().isLogin()) {
            view.showSnackbar(R.string.login_to_proceed);
            return;
        }

        if (like) {
            repository.unlikeShot(shotId);
            view.setLikeStatus(false);
            shot = shot.withLikesCount(shot.likesCount() - 1);
            view.updateLikesCount(view.getContext().getString(R.string.shot_likes_count, shot.likesCount()));
        } else {
            repository.likeShot(shotId);
            view.setLikeStatus(true);
            shot = shot.withLikesCount(shot.likesCount() + 1);
            view.updateLikesCount(view.getContext().getString(R.string.shot_likes_count, shot.likesCount()));

        }
        like = !like;
    }

    @Override
    public void toUser() {
        Intent intent = new Intent(view.getContext(), UserActivity.class);
        intent.putExtra(UserActivity.EXTRA_USER, shot.user());
        view.getContext().startActivity(intent);
    }

    @Override
    public void toComments() {
        Intent intent = new Intent(view.getContext(), CommentsActivity.class);
        intent.putExtra(CommentsActivity.EXTRA_SHOT, shot);
        view.getContext().startActivity(intent);
    }

    @Override
    public void toShare() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_TEXT, shot.html_url());
        share.setType("text/plain");
        view.getContext().startActivity(share);
    }
}
