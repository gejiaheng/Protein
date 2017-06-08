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

import com.ge.protein.data.model.Comment;
import com.ge.protein.data.model.Shot;
import com.ge.protein.data.model.ShotLike;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * http://developer.dribbble.com/v1/shots/
 */
public interface ShotsService {

    @GET("/v1/shots/{shot_id}")
    Observable<Response<Shot>> getShot(@Path("shot_id") long shotId);

    @GET("/v1/shots")
    Observable<Response<List<Shot>>> listShots(@Query("list") String list,
                                               @Query("timeframe") String timeframe,
                                               @Query("date") String date,
                                               @Query("sort") String sort,
                                               @Query("per_page") int perPage);

    @GET("/v1/users/{user_id}/shots")
    Observable<Response<List<Shot>>> listShotsForUser(@Path("user_id") long userId,
                                                      @Query("per_page") int perPage);

    @GET
    Observable<Response<List<Shot>>> listShotsOfNextPage(@Url String url);

    @GET("/v1/shots/{shot_id}/comments")
    Observable<Response<List<Comment>>> listCommentsForShot(@Path("shot_id") long shotId,
                                                            @Query("per_page") int perPage);

    @GET
    Observable<Response<List<Comment>>> listCommentsOfNextPage(@Url String url);

    @GET("/v1/shots/{shot_id}/like")
    Observable<Response<ShotLike>> checkLike(@Path("shot_id") long shotId);

    @POST("/v1/shots/{shot_id}/like")
    Observable<Response<ShotLike>> likeShot(@Path("shot_id") long shotId);

    @DELETE("/v1/shots/{shot_id}/like")
    Observable<Response<ShotLike>> unlikeShot(@Path("shot_id") long shotId);

    @POST("/v1/shots/{shot_id}/comments")
    Observable<Response<Comment>> createCommentForShot(@Path("shot_id") long shotId,
                                                       @Query("body") String content);
}
