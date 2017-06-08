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
package com.ge.protein.comment.post;

import com.ge.protein.data.api.ServiceGenerator;
import com.ge.protein.data.api.service.ShotsService;
import com.ge.protein.data.model.Comment;
import com.ge.protein.util.AccountManager;

import io.reactivex.Observable;
import retrofit2.Response;

class CommentPostRepository {

    private ShotsService shotsService;

    CommentPostRepository() {
        shotsService = ServiceGenerator.createService(ShotsService.class,
                AccountManager.getInstance().getAccessToken());
    }

    Observable<Response<Comment>> createCommentForShot(long shotId, String comment) {
        return shotsService.createCommentForShot(shotId, comment);
    }
}
