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

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.airbnb.epoxy.EpoxyModel;

import java.util.Collection;
import java.util.List;

public interface ListContract {

    interface View<T> extends BaseView<T>, SnackbarView {

        void showData(Collection<? extends EpoxyModel<?>> epoxyModels);

        void showMoreData(Collection<? extends EpoxyModel<?>> epoxyModels);

        void addModel(EpoxyModel<?> epoxyModel);

        void showLoading(boolean loading);

        void showLoadingMore(boolean loadingMore);

        void showEmptyView();

        void showErrorView();

        boolean hasToolbar();

        void setNavigationAsUp();

        void setNavigationAsClose();

        void setNavigationIcon(@DrawableRes int resId);

        void setNavigationIconTintColor(@ColorRes int resId);

        void setNavigationOnClickListener(android.view.View.OnClickListener listener);

        void setTitle(@StringRes int resId);

        void setTitle(CharSequence title);

        @ColorRes
        int toolbarBackground();
    }

    interface Presenter<DATA> extends BasePresenter, InstanceStatePresenter {

        void fetchData();

        void fetchMoreData();

        String getNextPageUrl();

        void setNextPageUrl(String nextPageUrl);

        Collection<? extends EpoxyModel<?>> generateEpoxyModels(List<DATA> dataList);
    }

}
