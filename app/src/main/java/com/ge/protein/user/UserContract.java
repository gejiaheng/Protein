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

import com.ge.protein.mvp.BasePresenter;
import com.ge.protein.mvp.BaseView;
import com.ge.protein.data.model.User;

interface UserContract {

    interface View extends BaseView<Presenter> {

        void setupView(User user);

        void showUser(User user);

        void setFollowButtonEnabled(boolean enabled);

        void setFollowButtonVisibility(boolean visible);

        void setFollowing(boolean following);
    }

    interface Presenter extends BasePresenter {

        void back();

        void toggleFollow();

        void toMap();

        void toTwitter();

        void toWeb();
    }
}
