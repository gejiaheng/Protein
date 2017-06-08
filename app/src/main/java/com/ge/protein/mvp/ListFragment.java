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

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.airbnb.epoxy.EpoxyModel;
import com.ge.protein.R;
import com.ge.protein.ui.epoxy.ProteinAdapter;
import com.ge.protein.ui.epoxy.RecyclerViewObserver;

import java.util.Collection;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Base list fragment implemented with {@link RecyclerView}.
 */
public abstract class ListFragment extends BaseFragment implements
        SwipeRefreshLayout.OnRefreshListener, ListContract.View<ListContract.Presenter> {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.swipe_refresh_layout)
    protected SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view)
    protected RecyclerView recyclerView;
    @BindView(R.id.empty_view)
    protected View emptyView;
    @BindView(R.id.error_view)
    protected View errorView;
    @BindView(R.id.button_retry)
    protected Button retry;

    protected ListContract.Presenter listPresenter;

    protected ProteinAdapter proteinAdapter;
    protected RecyclerViewObserver recyclerViewObserver;

    @Nullable
    @Override
    public android.view.View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                                          @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(android.view.View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            onPreStart(savedInstanceState);
        }
        setupView(view);
        listPresenter.start();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        listPresenter.onSaveInstanceState(outState);
    }

    // used to restore presenter and saved state
    protected void onPreStart(@NonNull Bundle savedInstanceState) {
    }

    protected void setupView(View view) {
        if (hasToolbar()) {
            toolbar.setVisibility(android.view.View.VISIBLE);
            toolbar.setBackgroundColor(
                    ResourcesCompat.getColor(getResources(), toolbarBackground(),
                            getContext().getTheme()));
        } else {
            toolbar.setVisibility(android.view.View.GONE);
        }

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(proteinAdapter = new ProteinAdapter());
        recyclerView.setLayoutManager(createLayoutManager());

        recyclerViewObserver = new RecyclerViewObserver()
                .subscribeOn(recyclerView)
                .setOnLoadMoreListener(() -> {
                    if (!TextUtils.isEmpty(listPresenter.getNextPageUrl())) {
                        listPresenter.fetchMoreData();
                    }
                })
                .initialize();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (recyclerViewObserver != null) {
            recyclerViewObserver.unSubscribe();
        }
    }

    protected RecyclerView.LayoutManager createLayoutManager() {
        return new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
    }

    @Override
    public void onRefresh() {
        listPresenter.fetchData();
    }

    @Override
    public void setPresenter(ListContract.Presenter presenter) {
        listPresenter = presenter;
    }

    @Override
    public void showData(@NonNull Collection<? extends EpoxyModel<?>> epoxyModels) {
        recyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        proteinAdapter.swap(epoxyModels);
    }

    @Override
    public void showMoreData(@NonNull Collection<? extends EpoxyModel<?>> epoxyModels) {
        proteinAdapter.addMore(epoxyModels);
    }

    @Override
    public void addModel(EpoxyModel<?> epoxyModel) {
        proteinAdapter.addModel(epoxyModel);
    }

    @Override
    public void showLoading(boolean loading) {
        swipeRefreshLayout.setRefreshing(loading);
        emptyView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingMore(boolean loadingMore) {
        proteinAdapter.showLoadMore(loadingMore);
        if (recyclerViewObserver != null) {
            recyclerViewObserver.setLoading(loadingMore);
        }
    }

    @Override
    public void showEmptyView() {
        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
    }

    @Override
    public void showErrorView() {
        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);

        if (!retry.hasOnClickListeners()) {
            retry.setOnClickListener(view -> {
                listPresenter.fetchData();
            });
        }
    }

    @Override
    public void showSnackbar(int resId) {
        showSnackbar(getString(resId));
    }

    @Override
    public void showSnackbar(String message) {
        if (getView() == null || TextUtils.isEmpty(message))
            return;

        Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean hasToolbar() {
        return false;
    }

    @Override
    public void setNavigationAsUp() {

    }

    @Override
    public void setNavigationAsClose() {
        setNavigationIcon(R.drawable.ic_close_white_24dp);
        setNavigationIconTintColor(R.color.icon_grey);
        setNavigationOnClickListener(v -> getActivity().onBackPressed());
    }

    @Override
    public void setNavigationIcon(@DrawableRes int resId) {
        if (!hasToolbar()) {
            return;
        }

        toolbar.setNavigationIcon(resId);
    }

    @Override
    public void setNavigationIconTintColor(@ColorRes int resId) {
        if (!hasToolbar())
            return;

        Drawable navigationIcon = toolbar.getNavigationIcon();
        if (navigationIcon == null)
            return;

        navigationIcon.setTint(ResourcesCompat.getColor(getResources(), resId, getContext().getTheme()));
    }

    @Override
    public void setNavigationOnClickListener(android.view.View.OnClickListener listener) {
        if (!hasToolbar()) {
            return;
        }

        toolbar.setNavigationOnClickListener(listener);
    }

    @Override
    public void setTitle(@StringRes int resId) {
        if (!hasToolbar()) {
            return;
        }

        toolbar.setTitle(resId);
    }

    @Override
    public void setTitle(CharSequence title) {
        if (!hasToolbar()) {
            return;
        }

        toolbar.setTitle(title);
    }

    @Override
    @ColorRes
    public int toolbarBackground() {
        return R.color.protein_white;
    }

}
