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
package com.ge.protein.user.follower;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.view.View;

import com.airbnb.epoxy.EpoxyModel;
import com.ge.protein.R;
import com.ge.protein.data.model.Follower;
import com.ge.protein.data.model.User;
import com.ge.protein.mvp.ListContract;
import com.ge.protein.mvp.ListPresenter;
import com.ge.protein.ui.epoxy.models.FollowerModel;
import com.ge.protein.ui.epoxy.models.FollowerModel_;
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

public class FollowerListPresenter extends ListPresenter<Follower> {

    private static final String STATE_USER = "state_user";

    private FollowerListRepository repository;
    @NonNull
    private User user;
    private List<Follower> firstPageFollowers = new ArrayList<>();

    FollowerListPresenter(@NonNull ListContract.View view) {
        super(view);
        repository = new FollowerListRepository();
    }

    public FollowerListPresenter(@NonNull ListContract.View view, @NonNull User user) {
        super(view);
        this.user = checkNotNull(user, "user cannot be null");
        repository = new FollowerListRepository();
    }

    @Override
    public void start() {
        if (firstPageFollowers.size() > 0) {
            view.showData(generateEpoxyModels(firstPageFollowers));
        } else {
            fetchData();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(STATE_USER, user);
        if (firstPageFollowers.size() > 0) {
            outState.putParcelableArrayList(STATE_FIRST_PAGE_DATA, (ArrayList<? extends Parcelable>) firstPageFollowers);
        }
        outState.putString(STATE_NEXT_PAGE_URL, getNextPageUrl());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        user = savedInstanceState.getParcelable(STATE_USER);
        firstPageFollowers = savedInstanceState.getParcelableArrayList(STATE_FIRST_PAGE_DATA);
        if (firstPageFollowers == null) {
            firstPageFollowers = new ArrayList<>();
        }
        setNextPageUrl(savedInstanceState.getString(STATE_NEXT_PAGE_URL));
    }

    @Override
    public void fetchData() {
        view.showLoading(true);
        repository.listUserFollowers(user.id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(((LifecycleProvider<FragmentEvent>) view).bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(listResponse -> {
                    view.showLoading(false);
                    firstPageFollowers.addAll(listResponse.body());
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
        view.showLoadingMore(true);
        repository.listFollowerOfNextPage(getNextPageUrl())
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
    public Collection<? extends EpoxyModel<?>> generateEpoxyModels(List<Follower> followers) {
        Collection<FollowerModel> epoxyModels = new ArrayList<>();
        for (Follower follower : followers) {
            FollowerModel model = new FollowerModel_()
                    .follower(follower)
                    .itemOnClickListener(itemOnClickListener);
            epoxyModels.add(model);
        }
        return epoxyModels;
    }

    private View.OnClickListener itemOnClickListener = v -> {
        Intent intent = new Intent();
        intent.setClass(view.getContext(), UserActivity.class);
        intent.putExtra(UserActivity.EXTRA_USER, ((Follower) v.getTag(R.id.clicked_model)).follower());
        view.getContext().startActivity(intent);
    };
}
