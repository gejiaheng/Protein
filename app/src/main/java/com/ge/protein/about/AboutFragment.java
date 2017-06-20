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

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ge.protein.mvp.BaseFragment;
import com.ge.protein.BuildConfig;
import com.ge.protein.R;
import com.ge.protein.ui.widget.FourThreeImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutFragment extends BaseFragment implements AboutContract.View {

    private AboutContract.Presenter presenter;

    @BindView(R.id.banner_image)
    FourThreeImageView bannerImage;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab_like)
    FloatingActionButton fabLike;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.layout_ge)
    View layoutGe;
    @BindView(R.id.layout_melodie)
    View layoutMelodie;
    @BindView(R.id.layout_others)
    View layoutOthers;
    @BindView(R.id.version_layout)
    View versionLayout;
    @BindView(R.id.version_name)
    TextView versionName;
    @BindView(R.id.license_layout)
    View licenseLayout;
    @BindView(R.id.share_layout)
    View shareLayout;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.getNavigationIcon().setColorFilter(
                getResources().getColor(R.color.icon_grey), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());

        int padding = getResources().getDimensionPixelSize(R.dimen.about_header_padding);
        bannerImage.setPadding(padding, padding, padding, padding);
        Glide.with(this)
                .load(R.drawable.egg)
                .into(bannerImage);

        Spannable span = new SpannableString(title.getContext().getString(R.string.about_title));
        span.setSpan(new TextAppearanceSpan(title.getContext(),
                        R.style.ProteinTextAppearance_Regular_XXL_Primary), 0, 7,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new TextAppearanceSpan(title.getContext(),
                        R.style.ProteinTextAppearance_Regular_L_Hint), 7, span.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        title.setText(span);
        versionName.setText(versionName.getContext().getString(R.string.about_version, BuildConfig.VERSION_NAME));

        layoutGe.setOnClickListener(aboutOnClickListener);
        layoutMelodie.setOnClickListener(aboutOnClickListener);
        layoutOthers.setOnClickListener(aboutOnClickListener);
        versionLayout.setOnClickListener(aboutOnClickListener);
        licenseLayout.setOnClickListener(aboutOnClickListener);
        shareLayout.setOnClickListener(aboutOnClickListener);
    }

    @Override
    public void setPresenter(AboutContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private View.OnClickListener aboutOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.version_layout:
                    presenter.toMarket();
                    break;
                case R.id.license_layout:
                    presenter.toLicense();
                    break;
                case R.id.share_layout:
                    presenter.shareProtein();
                    break;
                case R.id.layout_ge:
                    presenter.openTwitter("GeJiaheng");
                    break;
                case R.id.layout_melodie:
                    presenter.openTwitter("melodiezhang");
                    break;
                case R.id.layout_others:
                    presenter.toOtherContributors();
                    break;
            }
        }
    };
}
