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
import android.text.TextUtils;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import javax.annotation.Nullable;

@AutoValue
public abstract class Images implements Parcelable {

    @Nullable
    public abstract String hidpi();

    public abstract String normal();

    public abstract String teaser();

    public static TypeAdapter<Images> typeAdapter(Gson gson) {
        return new AutoValue_Images.GsonTypeAdapter(gson).nullSafe();
    }

    private static final int[] NORMAL_IMAGE_SIZE = new int[]{400, 300};
    private static final int[] TWO_X_IMAGE_SIZE = new int[]{800, 600};

    public String best() {
        return !TextUtils.isEmpty(hidpi()) ? hidpi() : normal();
    }

    public int[] bestSize() {
        return !TextUtils.isEmpty(hidpi()) ? TWO_X_IMAGE_SIZE : NORMAL_IMAGE_SIZE;
    }
}
