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
package com.ge.protein.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ge.protein.R;
import com.ge.protein.data.ProteinAdapterFactory;
import com.ge.protein.data.api.ErrorUtils;
import com.ge.protein.data.model.AccessToken;
import com.ge.protein.main.MainActivity;
import com.ge.protein.util.AccountManager;
import com.ge.protein.util.Constants;
import com.google.gson.GsonBuilder;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.FragmentEvent;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.ge.protein.util.Preconditions.checkNotNull;

class AuthPresenter implements AuthContract.Presenter {

    @NonNull
    private AuthContract.View view;
    private AuthRepository repository;

    AuthPresenter(@Nullable AuthContract.View view) {
        this.view = checkNotNull(view, "view cannot be null");
        this.view.setPresenter(this);
        repository = new AuthRepository();
    }

    @Override
    public void start() {

    }

    @Override
    public void getAccessToken(String code) {
        view.setProgressDialogVisibility(true);
        repository.getAccessToken(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(((LifecycleProvider<FragmentEvent>) view).bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(accessTokenResponse -> {
                    view.setProgressDialogVisibility(false);

                    if (accessTokenResponse.isSuccessful()) {
                        AccessToken accessToken = accessTokenResponse.body();
                        AccountManager.getInstance().setAccessToken(accessToken);

                        SharedPreferences sp = view.getContext().getSharedPreferences(
                                Constants.DEFAULT_SHARED_PREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        String strRepre = new GsonBuilder()
                                .registerTypeAdapterFactory(ProteinAdapterFactory.create())
                                .create()
                                .toJson(accessToken);
                        editor.putString(Constants.ACCESS_TOKEN_KEY, strRepre);
                        editor.apply();

                        Intent intent = new Intent(view.getContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        view.getContext().startActivity(intent);
                    } else {
                        view.setProgressDialogVisibility(false);
                        view.showSnackbar(ErrorUtils.parseError(accessTokenResponse).error());
                    }
                }, throwable -> {
                    view.setProgressDialogVisibility(false);
                    view.showSnackbar(R.string.request_access_token_failed);
                });
    }
}
