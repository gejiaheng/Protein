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
package com.ge.protein.comment.post;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ge.protein.R;
import com.ge.protein.data.model.Shot;
import com.ge.protein.util.RxBus;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.FragmentEvent;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.ge.protein.util.Preconditions.checkNotNull;

public class CommentPostPresenter implements CommentPostContract.Presenter {

    @NonNull
    private CommentPostContract.View view;
    private CommentPostRepository repository;
    @NonNull
    private Shot shot;

    public CommentPostPresenter(@Nullable Shot shot, @NonNull CommentPostContract.View view) {
        repository = new CommentPostRepository();
        this.shot = checkNotNull(shot, "shot cannot be null");
        this.view = checkNotNull(view, "view cannot be null");
        view.setPresenter(this);
    }

    @Override
    public void start() {
        // do nothing
    }

    @Override
    public void postComment(String comment) {
        repository.createCommentForShot(shot.id(), comment)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(((LifecycleProvider<FragmentEvent>) view).bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(commentResponse -> {
                    if (commentResponse.code() == 201 && commentResponse.body() != null) {
                        view.fillInput("");
                        RxBus.getInstance().post(commentResponse.body());
                    } else {
                        view.showSnackbar(R.string.shot_comment_send_failed);
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    view.showSnackbar(R.string.shot_comment_send_failed);
                });
    }
}
