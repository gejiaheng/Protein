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
package com.ge.protein.data.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import javax.annotation.Nullable;

@AutoValue
public abstract class Team implements Parcelable {

    public abstract long id();

    public abstract String name();

    public abstract String username();

    public abstract String html_url();

    public abstract String avatar_url();

    public abstract String bio();

    @Nullable
    public abstract String location();

    public abstract Links links();

    public abstract long buckets_count();

    public abstract long comments_received_count();

    public abstract long followers_count();

    public abstract long followings_count();

    public abstract long likes_count();

    public abstract long likes_received_count();

    public abstract long members_count();

    public abstract long projects_count();

    public abstract long rebounds_received_count();

    public abstract long shots_count();

    public abstract boolean can_upload_shot();

    public abstract boolean type();

    public abstract boolean pro();

    public abstract String buckets_url();

    public abstract String followers_url();

    public abstract String following_url();

    public abstract String likes_url();

    public abstract String members_url();

    public abstract String shots_url();

    public abstract String team_shots_url();

    public abstract String created_at();

    public abstract String updated_at();

    public static TypeAdapter<Team> typeAdapter(Gson gson) {
        return new AutoValue_Team.GsonTypeAdapter(gson).nullSafe();
    }
}
