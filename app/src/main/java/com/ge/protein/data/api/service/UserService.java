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
package com.ge.protein.data.api.service;

import com.ge.protein.data.model.Followee;
import com.ge.protein.data.model.Follower;
import com.ge.protein.data.model.Shot;
import com.ge.protein.data.model.ShotLike;
import com.ge.protein.data.model.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface UserService {

    @GET("/v1/user")
    Observable<Response<User>> getMe();

    @GET("/v1/users/{user_id}/shots")
    Observable<Response<List<Shot>>> listShotsForUser(@Path("user_id") long userId,
                                                      @Query("per_page") int perPage);

    @GET("/v1/users/{user_id}/likes")
    Observable<Response<List<ShotLike>>> listShotLikesForUser(@Path("user_id") long userId,
                                                              @Query("per_page") int perPage);

    @GET
    Observable<Response<List<ShotLike>>> listShotLikesForUserOfNextPage(@Url String url);

    @GET("/v1/users/{user_id}/following")
    Observable<Response<List<Followee>>> listUserFollowing(@Path("user_id") long userId,
                                                           @Query("per_page") int perPage);

    @GET
    Observable<Response<List<Followee>>> listFolloweeOfNextPage(@Url String url);

    @GET("/v1/users/{user_id}/followers")
    Observable<Response<List<Follower>>> listUserFollowers(@Path("user_id") long userId,
                                                           @Query("per_page") int perPage);

    @GET
    Observable<Response<List<Follower>>> listFollowerOfNextPage(@Url String url);

    @GET("/v1/user/following/{user_id}")
    Observable<Response<Body>> isFollowing(@Path("user_id") long userId);

    @PUT("/v1/users/{user_id}/follow")
    Observable<Response<Body>> follow(@Path("user_id") long userId);

    @DELETE("/v1/users/{user_id}/follow")
    Observable<Response<Body>> unfollow(@Path("user_id") long userId);

}
