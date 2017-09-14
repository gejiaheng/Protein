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
package com.ge.protein.main;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.ge.protein.about.AboutActivity;
import com.ge.protein.data.ProteinAdapterFactory;
import com.ge.protein.data.model.User;
import com.ge.protein.ui.dialog.LogoutDialog;
import com.ge.protein.ui.dialog.LoginGuideDialog;
import com.ge.protein.user.UserActivity;
import com.ge.protein.util.AccountManager;
import com.ge.protein.util.Constants;
import com.google.gson.GsonBuilder;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.ge.protein.util.Preconditions.checkNotNull;

class MainPresenter implements MainContract.Presenter {

    @NonNull
    private MainContract.View view;
    private MainRepository repository;

    MainPresenter(@NonNull MainContract.View view) {
        this.view = checkNotNull(view, "view cannot be null");
        this.view.setPresenter(this);
        repository = new MainRepository();
    }

    @Override
    public void start() {
        view.setupView();
        if (AccountManager.getInstance().isLogin()) {
            getAuthenticatedUser();
        } else {
            view.setDefaultUserInfo();
        }
    }

    @Override
    public void onToolbarUserClicked() {
        if (AccountManager.getInstance().isLogin()) {
            User me = AccountManager.getInstance().getMe();
            if (me == null) {
                return;
            }
            Intent intent = new Intent(view.getContext(), UserActivity.class);
            intent.putExtra(UserActivity.EXTRA_USER, me);
            view.getContext().startActivity(intent);
        } else {
            LoginGuideDialog.newInstance().show(((FragmentActivity) view.getContext()).getSupportFragmentManager(),
                    LoginGuideDialog.class.getSimpleName());
        }
    }

    @Override
    public void toAbout() {
        Intent intent = new Intent(view.getContext(), AboutActivity.class);
        view.getContext().startActivity(intent);
    }

    @Override
    public void showLogoutDialog() {
        DialogFragment logout = new LogoutDialog();
        logout.show(((FragmentActivity) view.getContext()).getSupportFragmentManager(),
                LogoutDialog.class.getSimpleName());
    }

    @Override
    public void getAuthenticatedUser() {
        String savedUserString = view.getContext()
                .getSharedPreferences(Constants.DEFAULT_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                .getString(Constants.USER, null);
        if (!TextUtils.isEmpty(savedUserString)) {
            User savedUser = new GsonBuilder()
                    .registerTypeAdapterFactory(ProteinAdapterFactory.create())
                    .create()
                    .fromJson(savedUserString, User.class);
            AccountManager.getInstance().setMe(savedUser);
            view.setUserInfo(savedUser);
        }

        repository.getMe()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(((LifecycleProvider<ActivityEvent>) view.getContext()).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(response -> {
                    User user = response.body();
                    if (!user.equals(AccountManager.getInstance().getMe())) {
                        AccountManager.getInstance().setMe(user);
                        view.setUserInfo(user);
                        String userString = new GsonBuilder()
                                .registerTypeAdapterFactory(ProteinAdapterFactory.create())
                                .create()
                                .toJson(user);
                        view.getContext()
                                .getSharedPreferences(Constants.DEFAULT_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                                .edit()
                                .putString(Constants.USER, userString)
                                .apply();
                    }
                }, throwable -> {

                });
    }
}
