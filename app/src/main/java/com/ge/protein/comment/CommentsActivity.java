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

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ge.protein.R;
import com.ge.protein.comment.post.CommentPostFragment;
import com.ge.protein.comment.post.CommentPostPresenter;
import com.ge.protein.data.model.Shot;
import com.ge.protein.firebase.FirebaseCrashUtils;
import com.ge.protein.ui.activity.BaseProteinActivity;
import com.ge.protein.util.AccountManager;
import com.ge.protein.util.UserUtils;

public class CommentsActivity extends BaseProteinActivity {

    public static final String EXTRA_SHOT = "extra_shot";

    private CommentListPresenter commentListPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseCrashUtils.log("CommentsActivity created");

        Shot shot = getIntent().getParcelableExtra(EXTRA_SHOT);
        setContentView(R.layout.activity_comments);

        // comment list
        CommentListFragment commentsFragment = (CommentListFragment) getSupportFragmentManager().findFragmentByTag(
                CommentListFragment.class.getSimpleName());
        if (commentsFragment == null) {
            commentsFragment = CommentListFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.comment_list_container, commentsFragment, CommentListFragment.class.getSimpleName())
                    .commit();
        }

        commentListPresenter = new CommentListPresenter(shot, commentsFragment);

        if (savedInstanceState != null)
            commentListPresenter.onRestoreInstanceState(savedInstanceState);

        // comment edit and post
        if (UserUtils.canComment(AccountManager.getInstance().getMe())) {
            CommentPostFragment commentPostFragment = (CommentPostFragment) getSupportFragmentManager()
                    .findFragmentByTag(CommentPostFragment.class.getSimpleName());
            if (commentPostFragment == null) {
                commentPostFragment = CommentPostFragment.newInstance();
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.comment_post_container, commentPostFragment, CommentPostFragment.class
                                .getSimpleName())
                        .commit();
            }

            new CommentPostPresenter(shot, commentPostFragment);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        commentListPresenter.onSaveInstanceState(outState);
    }
}
