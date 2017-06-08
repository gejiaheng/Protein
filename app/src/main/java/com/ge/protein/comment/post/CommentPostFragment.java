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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ge.protein.mvp.BaseFragment;
import com.ge.protein.R;

public class CommentPostFragment extends BaseFragment implements CommentPostContract.View {

    private CommentPostContract.Presenter presenter;

    private EditText input;

    public static CommentPostFragment newInstance() {
        return new CommentPostFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comment_post, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final View sendButton = view.findViewById(R.id.send_button);
        sendButton.setOnClickListener(v -> presenter.postComment(input.getEditableText().toString()));

        input = (EditText) view.findViewById(R.id.input);
        sendButton.setEnabled(false);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                sendButton.setEnabled(s.length() > 0);
            }
        });
    }

    @Override
    public void setPresenter(CommentPostContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void fillInput(CharSequence text) {
        input.setText(text);
    }

    @Override
    public void showSnackbar(int resId) {
        showSnackbar(getString(resId));
    }

    @Override
    public void showSnackbar(String message) {
        if (getView() == null)
            return;

        Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }
}
