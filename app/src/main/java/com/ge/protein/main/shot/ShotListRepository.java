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
package com.ge.protein.main.shot;

import com.ge.protein.data.api.ApiConstants;
import com.ge.protein.data.api.ServiceGenerator;
import com.ge.protein.data.api.service.FollowersService;
import com.ge.protein.data.api.service.ShotsService;
import com.ge.protein.data.model.Shot;
import com.ge.protein.util.AccountManager;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;

import static com.ge.protein.main.shot.ShotListPresenter.DEBUTS;
import static com.ge.protein.main.shot.ShotListPresenter.RECENT;

class ShotListRepository {

    private ShotsService shotsService;
    private FollowersService followersService;

    ShotListRepository() {
        shotsService = ServiceGenerator.createService(ShotsService.class,
                AccountManager.getInstance().getAccessToken());
        followersService = ServiceGenerator.createService(FollowersService.class,
                AccountManager.getInstance().getAccessToken());
    }

    Observable<Response<List<Shot>>> listFollowingShots() {
        return followersService.listFollowingShots(ApiConstants.PER_PAGE);
    }

    Observable<Response<List<Shot>>> getShots(int type) {
        String queryList = null;
        String timeframe = null;
        String date = null;
        String sort = null;

        if (type == RECENT) {
            sort = "recent";
        } else if (type == DEBUTS) {
            queryList = "debuts";
        }

        return shotsService.listShots(queryList, timeframe, date, sort, ApiConstants.PER_PAGE);
    }

    Observable<Response<List<Shot>>> getShotsOfNextPage(String url) {
        return shotsService.listShotsOfNextPage(url);
    }
}
