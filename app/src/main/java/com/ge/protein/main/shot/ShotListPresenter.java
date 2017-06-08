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
package com.ge.protein.main.shot;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.airbnb.epoxy.EpoxyModel;
import com.ge.protein.R;
import com.ge.protein.data.model.Shot;
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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ShotListPresenter extends ListPresenter<Shot> {

    private static final String STATE_TYPE = "state_type";

    public static final int POPULAR = 0x00;
    public static final int FOLLOWING = 0x01;
    public static final int RECENT = 0x02;
    public static final int DEBUTS = 0x03;

    @IntDef({POPULAR, FOLLOWING, RECENT, DEBUTS})
    @Retention(RetentionPolicy.SOURCE)
    @interface ShotListType {
    }

    @ShotListType
    private int type;

    private ShotListRepository repository;
    private List<Shot> firstPageShots = new ArrayList<>();

    public ShotListPresenter(@NonNull ListContract.View view) {
        super(view);
        repository = new ShotListRepository();
    }

    public ShotListPresenter(@ShotListType int type, @NonNull ListContract.View view) {
        super(view);
        repository = new ShotListRepository();
        this.type = type;
    }

    @Override
    public void start() {
        if (firstPageShots.size() > 0) {
            view.showData(generateEpoxyModels(firstPageShots));
        } else {
            fetchData();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_TYPE, type);
        if (firstPageShots.size() > 0) {
            outState.putParcelableArrayList(STATE_FIRST_PAGE_DATA, (ArrayList<? extends Parcelable>) firstPageShots);
        }
        outState.putString(STATE_NEXT_PAGE_URL, getNextPageUrl());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        type = savedInstanceState.getInt(STATE_TYPE);
        firstPageShots = savedInstanceState.getParcelableArrayList(STATE_FIRST_PAGE_DATA);
        if (firstPageShots == null) {
            firstPageShots = new ArrayList<>();
        }
        setNextPageUrl(savedInstanceState.getString(STATE_NEXT_PAGE_URL));
    }

    @Override
    public void fetchData() {
        view.showLoading(true);
        if (type == FOLLOWING) {
            repository.listFollowingShots()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(((LifecycleProvider<FragmentEvent>) view).bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    .subscribe(response -> {
                        view.showLoading(false);
                        firstPageShots.addAll(response.body());
                        view.showData(generateEpoxyModels(response.body()));
                        if (response.body().isEmpty()) {
                            view.showEmptyView();
                        }
                        setNextPageUrl(new PageLinks(response).getNext());
                    }, throwable -> {
                        view.showLoading(false);
                        view.showErrorView();
                        throwable.printStackTrace();
                    });
        } else {
            repository.getShots(type)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(((LifecycleProvider<FragmentEvent>) view).bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    .subscribe(response -> {
                        view.showLoading(false);
                        firstPageShots.addAll(response.body());
                        view.showData(generateEpoxyModels(response.body()));
                        if (response.body().isEmpty()) {
                            view.showEmptyView();
                        }
                        setNextPageUrl(new PageLinks(response).getNext());
                    }, throwable -> {
                        view.showLoading(false);
                        view.showErrorView();
                        throwable.printStackTrace();
                    });
        }
    }

    @Override
    public void fetchMoreData() {
        if (TextUtils.isEmpty(getNextPageUrl()))
            return;

        view.showLoadingMore(true);
        repository.getShotsOfNextPage(getNextPageUrl())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(((LifecycleProvider<FragmentEvent>) view).bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(response -> {
                    view.showLoadingMore(false);
                    view.showMoreData(generateEpoxyModels(response.body()));
                    setNextPageUrl(new PageLinks(response).getNext());
                }, throwable -> {
                    view.showLoadingMore(false);
                    view.showSnackbar(throwable.getMessage());
                    throwable.printStackTrace();
                });
    }

    @Override
    public Collection<? extends EpoxyModel<?>> generateEpoxyModels(List<Shot> shots) {
        List<ShotListSimpleModel> epoxyModels = new ArrayList<>();
        for (Shot shot : shots) {
            ShotListSimpleModel model = new ShotListSimpleModel_()
                    .shot(shot)
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
