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

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.airbnb.epoxy.EpoxyModel;
import com.ge.protein.R;
import com.ge.protein.data.model.Shot;
import com.ge.protein.data.model.ShotLike;
import com.ge.protein.data.model.User;
import com.ge.protein.mvp.ListContract;
import com.ge.protein.mvp.ListPresenter;
import com.ge.protein.shot.ShotActivity;
import com.ge.protein.ui.epoxy.models.ShotListSimpleModel;
import com.ge.protein.ui.epoxy.models.ShotListSimpleModel_;
import com.ge.protein.user.UserActivity;
import com.ge.protein.util.PageLinks;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.ge.protein.util.Preconditions.checkNotNull;

public class ShotLikeListPresenter extends ListPresenter<ShotLike> {

    private static final String STATE_USER = "state_user";

    private ShotLikeListRepository repository;
    @NonNull
    private User user;
    private List<ShotLike> firstPageShotLikes = new ArrayList<>();

    ShotLikeListPresenter(@NonNull ListContract.View view) {
        super(view);
        repository = new ShotLikeListRepository();
    }

    public ShotLikeListPresenter(@NonNull ListContract.View view, @NonNull User user) {
        super(view);
        this.user = checkNotNull(user, "user cannot be null");
        repository = new ShotLikeListRepository();
    }

    @Override
    public void start() {
        if (firstPageShotLikes.size() > 0) {
            view.showData(generateEpoxyModels(firstPageShotLikes));
        } else {
            fetchData();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(STATE_USER, user);
        if (firstPageShotLikes.size() > 0) {
            outState.putParcelableArrayList(STATE_FIRST_PAGE_DATA, (ArrayList<? extends Parcelable>) firstPageShotLikes);
        }
        outState.putString(STATE_NEXT_PAGE_URL, getNextPageUrl());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        user = savedInstanceState.getParcelable(STATE_USER);
        firstPageShotLikes = savedInstanceState.getParcelableArrayList(STATE_FIRST_PAGE_DATA);
        if (firstPageShotLikes == null) {
            firstPageShotLikes = new ArrayList<>();
        }
        setNextPageUrl(savedInstanceState.getString(STATE_NEXT_PAGE_URL));
    }

    @Override
    public void fetchData() {
        view.showLoading(true);
        repository.listShotLikesForUser(user.id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(((LifecycleProvider<FragmentEvent>) view).bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(listResponse -> {
                    view.showLoading(false);
                    firstPageShotLikes.addAll(listResponse.body());
                    view.showData(generateEpoxyModels(listResponse.body()));
                    if (listResponse.body().isEmpty()) {
                        view.showEmptyView();
                    }
                    setNextPageUrl(new PageLinks(listResponse).getNext());
                }, throwable -> {
                    view.showLoading(false);
                    view.showErrorView();
                    throwable.printStackTrace();
                });
    }

    @Override
    public void fetchMoreData() {
        if (TextUtils.isEmpty(getNextPageUrl()))
            return;

        view.showLoadingMore(true);
        repository.listShotLikesForUserOfNextPage(getNextPageUrl())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(((LifecycleProvider<FragmentEvent>) view).bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(listResponse -> {
                    view.showLoadingMore(false);
                    view.showMoreData(generateEpoxyModels(listResponse.body()));
                    setNextPageUrl(new PageLinks(listResponse).getNext());
                }, throwable -> {
                    view.showLoadingMore(false);
                    view.showSnackbar(throwable.getMessage());
                    throwable.printStackTrace();
                });
    }

    @Override
    public Collection<? extends EpoxyModel<?>> generateEpoxyModels(List<ShotLike> shotLikes) {
        List<ShotListSimpleModel> epoxyModels = new ArrayList<>();
        for (ShotLike shotLike : shotLikes) {
            ShotListSimpleModel model = new ShotListSimpleModel_()
                    .shot(shotLike.shot())
                    .context(view.getContext())
                    .shotOnClickListener(shotOnClickListener)
                    .userOnClickListener(userOnClickListener);
            epoxyModels.add(model);
        }
        return epoxyModels;
    }

    private View.OnClickListener shotOnClickListener = v -> {
        Intent shotDetailIntent = new Intent();
        shotDetailIntent.setClass(view.getContext(), ShotActivity.class);
        shotDetailIntent.putExtra(ShotActivity.EXTRA_SHOT, (Shot) v.getTag(R.id.clicked_model));
        view.getContext().startActivity(shotDetailIntent);
    };

    private View.OnClickListener userOnClickListener = v -> {
        Intent userIntent = new Intent();
        userIntent.setClass(view.getContext(), UserActivity.class);
        userIntent.putExtra(UserActivity.EXTRA_USER, (User) v.getTag(R.id.clicked_model));
        view.getContext().startActivity(userIntent);
    };
}
