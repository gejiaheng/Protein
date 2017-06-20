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
package com.ge.protein.about;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.ge.droid.mdlicense.Library;
import com.ge.droid.mdlicense.MDLicenseIntent;

import java.util.ArrayList;

import static com.ge.protein.util.Preconditions.checkNotNull;

class AboutPresenter implements AboutContract.Presenter {

    private static final String PROTEIN_MARKET_LINK = "market://details?id=com.ge.protein";
    private static final String PROTEIN_WEB_LINK = "http://play.google.com/store/apps/details?id=com.ge.protein";

    @NonNull
    private AboutContract.View view;

    AboutPresenter(@NonNull AboutContract.View view) {
        this.view = checkNotNull(view, "view cannot be null");
        this.view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void openTwitter(String name) {
        try {
            view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name)));
        } catch (ActivityNotFoundException e) {
            // ignore
        }
    }

    @Override
    public void toOtherContributors() {
        try {
            view.getContext().startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://github.com/gejiaheng/Protein/graphs/contributors")));
        } catch (ActivityNotFoundException e) {
            // ignore
        }
    }

    @Override
    public void toMarket() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            intent.setData(Uri.parse(PROTEIN_MARKET_LINK));
        } catch (ActivityNotFoundException e) {
            intent.setData(Uri.parse(PROTEIN_WEB_LINK));
        }
        view.getContext().startActivity(intent);
    }

    @Override
    public void toLicense() {
        new MDLicenseIntent.Builder(view.getContext())
                .libraries(getLibraryList())
                .build()
                .launch();
    }

    @Override
    public void shareProtein() {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, PROTEIN_WEB_LINK);
            intent.setType("text/plain");
            view.getContext().startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // ignore
        }
    }

    private ArrayList<Library> getLibraryList() {
        ArrayList<Library> libraries = new ArrayList<>();
        libraries.add(new Library("Retrofit", "Square", "https://github.com/square/retrofit"));
        libraries.add(new Library("Retrofit Gson Converter", "Square", "https://github.com/square/retrofit"));
        libraries.add(new Library("Retrofit RxJava2 Adapter", "Square", "https://github.com/square/retrofit"));
        libraries.add(new Library("Gson", "Google", "https://github.com/google/gson"));
        libraries.add(new Library("AutoValue", "Google", "https://github.com/google/auto/tree/master/value"));
        libraries.add(new Library("AutoValue: Parcel Extension", "Ryan Harter",
                "https://github.com/rharter/auto-value-parcel"));
        libraries.add(new Library("AutoValue: Gson Extension", "Ryan Harter",
                "https://github.com/rharter/auto-value-gson"));
        libraries.add(new Library("AutoValue: With Extension", "Gabriel Ittner",
                "https://github.com/gabrielittner/auto-value-with"));
        libraries.add(new Library("RxJava2", "ReactiveX", "https://github.com/ReactiveX/RxJava"));
        libraries.add(new Library("RxLifecycle", "Trello", "https://github.com/trello/RxLifecycle"));
        libraries.add(new Library("RxBinding", "Jake Wharton", "https://github.com/JakeWharton/RxBinding"));
        libraries.add(new Library("Glide", "Bump Technologies", "https://github.com/bumptech/glide"));
        libraries.add(new Library("MDLicense", "Jiaheng Ge", "https://github.com/gejiaheng/MDLicense"));
        libraries.add(new Library("Stetho", "Facebook", "https://github.com/facebook/stetho"));
        libraries.add(new Library("Epoxy", "Airbnb", "https://github.com/airbnb/epoxy"));
        libraries.add(new Library("Butter Knife", "Jake Wharton", "https://github.com/JakeWharton/butterknife"));
        libraries.add(new Library("DeepLinkDispatch", "Airbnb", "https://github.com/airbnb/DeepLinkDispatch"));

        return libraries;
    }
}
