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
package com.ge.protein.util;

import com.ge.protein.data.ProteinAdapterFactory;
import com.ge.protein.data.model.AccessToken;
import com.ge.protein.data.model.User;
import com.google.gson.GsonBuilder;

public class AccountManager {

    private static AccountManager accountManager = new AccountManager();
    private AccessToken accessToken;
    private User me;

    private AccountManager() {
    }

    public static AccountManager getInstance() {
        return accountManager;
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public void setAccessToken(String token) {
        accessToken = new GsonBuilder()
                .registerTypeAdapterFactory(ProteinAdapterFactory.create())
                .create()
                .fromJson(token, AccessToken.class);
    }

    public boolean isLogin() {
        return accessToken != null;
    }

    public User getMe() {
        return me;
    }

    public void setMe(User me) {
        this.me = me;
    }

    public void clear() {
        accessToken = null;
        me = null;
    }
}
