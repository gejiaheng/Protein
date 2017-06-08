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
package com.ge.protein.user;

import com.ge.protein.data.api.ServiceGenerator;
import com.ge.protein.data.api.service.UserService;
import com.ge.protein.util.AccountManager;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;

class UserRepository {

    private UserService userService;

    UserRepository() {
        userService = ServiceGenerator.createService(UserService.class,
                AccountManager.getInstance().getAccessToken());
    }

    Observable<Response<Body>> isFollowing(long userId) {
        return userService.isFollowing(userId);
    }

    Observable<Response<Body>> follow(long userId) {
        return userService.follow(userId);
    }

    Observable<Response<Body>> unfollow(long userId) {
        return userService.unfollow(userId);
    }
}
