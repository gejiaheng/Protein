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
package com.ge.protein.comment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.airbnb.epoxy.EpoxyModel;
import com.ge.protein.R;
import com.ge.protein.data.model.Comment;
import com.ge.protein.data.model.Shot;
import com.ge.protein.data.model.User;
import com.ge.protein.mvp.InstanceStatePresenter;
import com.ge.protein.mvp.ListContract;
import com.ge.protein.mvp.ListPresenter;
import com.ge.protein.ui.epoxy.models.ShotCommentModel;
import com.ge.protein.ui.epoxy.models.ShotCommentModel_;
import com.ge.protein.user.UserActivity;
import com.ge.protein.util.PageLinks;
import com.ge.protein.util.RxBus;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.ge.protein.util.Preconditions.checkNotNull;

class CommentListPresenter extends ListPresenter<Comment> implements InstanceStatePresenter {

    private CommentListRepository repository;
    @NonNull
    private Shot shot;
    private List<Comment> firstPageComments = new ArrayList<>();

    CommentListPresenter(@NonNull Shot shot, @NonNull ListContract.View view) {
        super(view);
        repository = new CommentListRepository();
        this.shot = checkNotNull(shot, "shot cannot be null");
    }

    @Override
    public void start() {
        view.setTitle(view.getContext().getString(R.string.shot_detail_comment_header, shot.comments_count()));

        RxBus.getInstance().toObservable(Comment.class).subscribe(comment ->
                view.addModel(new ShotCommentModel_()
                        .comment(comment)
                        .shotCommentOnClickListener(shotCommentOnClickListener)));

        if (firstPageComments.size() > 0) {
            view.showData(generateEpoxyModels(firstPageComments));
        } else {
            fetchData();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (firstPageComments.size() > 0) {
            outState.putParcelableArrayList(STATE_FIRST_PAGE_DATA, (ArrayList<? extends Parcelable>) firstPageComments);
        }
        outState.putString(STATE_NEXT_PAGE_URL, getNextPageUrl());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        firstPageComments = savedInstanceState.getParcelableArrayList(STATE_FIRST_PAGE_DATA);
        if (firstPageComments == null) {
            firstPageComments = new ArrayList<>();
        }
        setNextPageUrl(savedInstanceState.getString(STATE_NEXT_PAGE_URL));
    }

    @Override
    public void fetchData() {
        view.showLoading(true);
        repository.getCommentsForShot(shot.id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(((LifecycleProvider<FragmentEvent>) view).bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(commentListResponse -> {
                    view.showLoading(false);
                    firstPageComments.addAll(commentListResponse.body());
                    view.showData(generateEpoxyModels(commentListResponse.body()));
                    if (commentListResponse.body().isEmpty()) {
                        view.showEmptyView();
                    }
                    setNextPageUrl(new PageLinks(commentListResponse).getNext());
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
        repository.getCommentsOfNextPage(getNextPageUrl())
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
    public Collection<? extends EpoxyModel<?>> generateEpoxyModels(List<Comment> comments) {
        List<ShotCommentModel> epoxyModels = new ArrayList<>();
        for (Comment comment : comments) {
            ShotCommentModel model = new ShotCommentModel_()
                    .comment(comment)
                    .shotCommentOnClickListener(shotCommentOnClickListener);
            epoxyModels.add(model);
        }
        return epoxyModels;
    }

    private View.OnClickListener shotCommentOnClickListener = v -> {
        if (v.getId() == R.id.avatar) {
            Intent intent = new Intent();
            intent.setClass(view.getContext(), UserActivity.class);
            intent.putExtra(UserActivity.EXTRA_USER, (User) v.getTag(R.id.clicked_model));
            view.getContext().startActivity(intent);
        }
    };
}
