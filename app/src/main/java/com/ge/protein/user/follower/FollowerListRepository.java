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
package com.ge.protein.user.follower;

import com.ge.protein.data.api.ApiConstants;
import com.ge.protein.data.api.ServiceGenerator;
import com.ge.protein.data.api.service.UserService;
import com.ge.protein.data.model.Follower;
import com.ge.protein.util.AccountManager;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;

class FollowerListRepository {

    private UserService userService;

    FollowerListRepository() {
        userService = ServiceGenerator.createService(UserService.class,
                AccountManager.getInstance().getAccessToken());
    }

    Observable<Response<List<Follower>>> listUserFollowers(long userId) {
        return userService.listUserFollowers(userId, ApiConstants.PER_PAGE);
    }

    Observable<Response<List<Follower>>> listFollowerOfNextPage(String url) {
        return userService.listFollowerOfNextPage(url);
    }
}
