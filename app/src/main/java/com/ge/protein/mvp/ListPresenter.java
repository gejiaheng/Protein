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
package com.ge.protein.mvp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.ge.protein.util.Preconditions.checkNotNull;

public abstract class ListPresenter<DATA> implements ListContract.Presenter<DATA> {

    protected static final String STATE_NEXT_PAGE_URL = "state_next_page_url";
    // only save first page data, or the App may occur TransactionTooLargeException
    // see https://developer.android.com/reference/android/os/TransactionTooLargeException.html
    // FIXME still happens
    protected static final String STATE_FIRST_PAGE_DATA = "state_first_page_data";

    @NonNull
    protected ListContract.View view;
    @Nullable
    private String nextPageUrl;

    public ListPresenter(@NonNull ListContract.View view) {
        this.view = checkNotNull(view, "view cannot be null");
        this.view.setPresenter(this);
    }

    @Nullable
    @Override
    public String getNextPageUrl() {
        return nextPageUrl;
    }

    @Override
    public void setNextPageUrl(@Nullable String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }
}
