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

import com.ge.protein.BuildConfig;
import com.ge.protein.data.api.ApiConstants;
import com.ge.protein.data.api.ServiceGenerator;
import com.ge.protein.data.api.service.AccessTokenService;
import com.ge.protein.data.model.AccessToken;
import com.ge.protein.util.AccountManager;

import io.reactivex.Observable;
import retrofit2.Response;

class AuthRepository {

    private AccessTokenService accessTokenService;

    AuthRepository() {
        accessTokenService = ServiceGenerator.createService(AccessTokenService.class,
                AccountManager.getInstance().getAccessToken());
    }

    Observable<Response<AccessToken>> getAccessToken(String code) {
        return accessTokenService.getAccessToken(
                ApiConstants.DRIBBBLE_GET_ACCESS_TOKEN_URL,
                BuildConfig.DRIBBBLE_CLIENT_ID,
                BuildConfig.DRIBBBLE_CLIENT_SECRET,
                code,
                ApiConstants.DRIBBBLE_AUTHORIZE_CALLBACK_URI);
    }
}
