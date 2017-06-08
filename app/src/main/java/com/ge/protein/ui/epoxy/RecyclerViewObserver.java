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
package com.ge.protein.ui.epoxy;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public class RecyclerViewObserver {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private boolean loading;

    private OnLoadMoreListener onLoadMoreListener;

    public RecyclerViewObserver subscribeOn(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        return this;
    }

    public void unSubscribe() {
        recyclerView = null;
        onLoadMoreListener = null;
    }

    public RecyclerViewObserver setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
        return this;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public boolean isLoading() {
        return loading;
    }

    public RecyclerViewObserver initialize() {
        layoutManager = recyclerView.getLayoutManager();
        if (!(layoutManager instanceof LinearLayoutManager)) {
            throw new IllegalArgumentException("Support only LinearLayoutManager now.");
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalItemCount = recyclerView.getAdapter().getItemCount();
                int lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();

                if (!loading && lastVisibleItemPosition == (totalItemCount - 1)) {
                    if (onLoadMoreListener != null) {
                        Log.d("RecyclerViewObserver", "onLoadMore");
                        onLoadMoreListener.onLoadMore();
                    }
                }
            }
        });

        return this;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

}
