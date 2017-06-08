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
package com.ge.protein.shot;

import com.ge.protein.data.api.ServiceGenerator;
import com.ge.protein.data.api.service.ShotsService;
import com.ge.protein.data.model.Shot;
import com.ge.protein.data.model.ShotLike;
import com.ge.protein.util.AccountManager;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;

class ShotRepository {

    private ShotsService shotsService;

    ShotRepository() {
        shotsService = ServiceGenerator.createService(ShotsService.class,
                AccountManager.getInstance().getAccessToken());
    }

    Observable<Response<Shot>> getShot(long shotId) {
        return shotsService.getShot(shotId);
    }

    Observable<Response<List<Shot>>> listShotsForUser(long userId) {
        return shotsService.listShotsForUser(userId, 5);
    }

    Observable<Response<ShotLike>> checkLike(long shotId) {
        return shotsService.checkLike(shotId);
    }

    Observable<Response<ShotLike>> likeShot(long shotId) {
        return shotsService.likeShot(shotId);
    }

    Observable<Response<ShotLike>> unlikeShot(long shotId) {
        return shotsService.unlikeShot(shotId);
    }
}
