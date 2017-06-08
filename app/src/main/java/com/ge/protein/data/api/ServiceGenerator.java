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

import android.content.Context;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.ge.protein.BuildConfig;
import com.ge.protein.data.ProteinAdapterFactory;
import com.ge.protein.data.model.AccessToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static String lastToken;

    private static Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(ProteinAdapterFactory.create())
            .create();

    private static Cache cache;

    private static Retrofit retrofit;

    public static void init(Context context) {
        if (cache != null) {
            throw new IllegalStateException("Retrofit cache already initialized.");
        }
        cache = new Cache(context.getCacheDir(), 20 * 1024 * 1024);
    }

    public static <S> S createService(Class<S> serviceClass, final AccessToken token) {
        String currentToken = token == null ? BuildConfig.DRIBBBLE_CLIENT_ACCESS_TOKEN : token.access_token();
        if (retrofit == null || !currentToken.equals(lastToken)) {
            lastToken = currentToken;
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            httpClientBuilder.addInterceptor(chain -> {
                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder()
                        .header("Accept", "application/json")
                        .header("Authorization", "Bearer" + " " + lastToken)
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }).cache(cache);
            if (BuildConfig.DEBUG) {
                httpClientBuilder.addNetworkInterceptor(new StethoInterceptor());
            }
            Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                    .baseUrl(ApiConstants.DRIBBBLE_V1_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
            OkHttpClient httpClient = httpClientBuilder.build();
            retrofit = retrofitBuilder.client(httpClient).build();
        }

        return retrofit.create(serviceClass);
    }

}
