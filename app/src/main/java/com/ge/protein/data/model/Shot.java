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

import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

@AutoValue
public abstract class Shot implements Parcelable {

    public abstract long id();

    public abstract String title();

    @Nullable
    public abstract String description();

    public abstract int width();

    public abstract int height();

    public abstract Images images();

    public abstract long views_count();

    public abstract long likesCount();

    public abstract long comments_count();

    public abstract long attachments_count();

    public abstract long rebounds_count();

    public abstract long buckets_count();

    public abstract Date created_at();

    public abstract Date updated_at();

    public abstract String html_url();

    public abstract String attachments_url();

    public abstract String buckets_url();

    public abstract String comments_url();

    public abstract String likes_url();

    public abstract String projects_url();

    public abstract String rebounds_url();

    public abstract boolean animated();

    public abstract List<String> tags();

    @Nullable
    public abstract User user();

    @Nullable
    public abstract Team team();

    public abstract Shot withUser(User user);

    public abstract Shot withLikesCount(long likesCount);

    public static TypeAdapter<Shot> typeAdapter(Gson gson) {
        return new AutoValue_Shot.GsonTypeAdapter(gson).nullSafe();
    }
}
