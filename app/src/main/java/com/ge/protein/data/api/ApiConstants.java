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
package com.ge.protein.data.api;

public final class ApiConstants {

    private ApiConstants() {
        throw new AssertionError("No construction for constant class");
    }

    // general constants of Dribbble API
    public static final String DRIBBBLE_V1_BASE_URL = "https://api.dribbble.com";
    public static final String DRIBBBLE_AUTHORIZE_URL = "https://dribbble.com/oauth/authorize";
    public static final String DRIBBBLE_GET_ACCESS_TOKEN_URL = "https://dribbble.com/oauth/token";

    // for both flavor open and play
    public static final String DRIBBBLE_AUTHORIZE_CALLBACK_URI = "x-protein-oauth-dribbble://callback";
    public static final String DRIBBBLE_AUTHORIZE_CALLBACK_URI_SCHEMA = "x-protein-oauth-dribbble";
    public static final String DRIBBBLE_AUTHORIZE_CALLBACK_URI_HOST = "callback";
    public static final String DRIBBBLE_AUTHORIZE_SCOPE = "public write comment upload";

    public static final int PER_PAGE = 20;
}
