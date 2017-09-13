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

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ge.protein.R;
import com.ge.protein.data.model.User;
import com.ge.protein.ui.widget.TabCustomView;
import com.ge.protein.util.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.placeholderOf;

public class UserView extends CoordinatorLayout implements UserContract.View, View.OnClickListener {

    private UserContract.Presenter presenter;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.user_bio)
    TextView userBio;
    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.user_info)
    View userInfo;
    @BindView(R.id.location)
    TextView location;
    @BindView(R.id.tiny_divider)
    View tinyDivider;
    @BindView(R.id.twitter)
    ImageView twitter;
    @BindView(R.id.web)
    ImageView web;
    @BindView(R.id.follow_button)
    Button followButton;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    public UserView(Context context) {
        super(context);
        init(context);
    }

    public UserView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public UserView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.user_view_content, this, true);
        ButterKnife.bind(this);
    }

    @Override
    public void setPresenter(UserContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setupView(User user) {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.getNavigationIcon().setColorFilter(
                getResources().getColor(R.color.icon_grey), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(v -> presenter.back());

        userBio.setMovementMethod(LinkMovementMethod.getInstance());

        Drawable[] drawables = location.getCompoundDrawables();
        if (drawables[0] != null) {
            drawables[0].setColorFilter(getResources().getColor(R.color.icon_grey), PorterDuff.Mode.SRC_IN);
        }

        followButton.setOnClickListener(this);
        location.setOnClickListener(this);
        twitter.setOnClickListener(this);
        web.setOnClickListener(this);

        viewPager.setAdapter(new UserPagerAdapter(((FragmentActivity)getContext()).getSupportFragmentManager(), user));

        TabLayout.Tab shotsTab = tabLayout.newTab();
        TabCustomView shotsTabCustomView = new TabCustomView(getContext());
        shotsTab.setCustomView(shotsTabCustomView);
        shotsTabCustomView.setCount(user.shots_count());
        shotsTabCustomView.setContentCategory(R.string.user_tab_shots);
        tabLayout.addTab(shotsTab);

        TabLayout.Tab likesTab = tabLayout.newTab();
        TabCustomView likesTabCustomView = new TabCustomView(getContext());
        likesTab.setCustomView(likesTabCustomView);
        likesTabCustomView.setCount(user.likes_count());
        likesTabCustomView.setContentCategory(R.string.user_tab_likes);
        tabLayout.addTab(likesTab);

        TabLayout.Tab followersTab = tabLayout.newTab();
        TabCustomView followersTabCustomView = new TabCustomView(getContext());
        followersTab.setCustomView(followersTabCustomView);
        followersTabCustomView.setCount(user.followers_count());
        followersTabCustomView.setContentCategory(R.string.user_tab_followers);
        tabLayout.addTab(followersTab);

        TabLayout.Tab followingTab = tabLayout.newTab();
        TabCustomView followingTabCustomView = new TabCustomView(getContext());
        followingTab.setCustomView(followingTabCustomView);
        followingTabCustomView.setCount(user.followings_count());
        followingTabCustomView.setContentCategory(R.string.user_tab_following);
        tabLayout.addTab(followingTab);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.setOffscreenPageLimit(3);
    }

    @Override
    public void showUser(User user) {
        collapsingToolbarLayout.setTitle(user.name());

        Glide.with(getContext())
                .load(user.avatar_url())
                .transition(withCrossFade())
                .apply(placeholderOf(R.color.avatar_placeholder).diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(avatar);

        userBio.setVisibility(TextUtils.isEmpty(user.bio()) ? GONE : VISIBLE);
        userBio.setText(StringUtils.trimTrailingWhitespace(Html.fromHtml(user.bio())));

        userInfo.setVisibility((TextUtils.isEmpty(user.location()) && TextUtils.isEmpty(user.links().twitter()) &&
                TextUtils.isEmpty(user.links().web())) ? GONE : VISIBLE);
        location.setVisibility(TextUtils.isEmpty(user.location()) ? GONE : VISIBLE);
        location.setText(user.location());
        tinyDivider.setVisibility((TextUtils.isEmpty(user.links().twitter()) && TextUtils.isEmpty(user.links().web()) ||
                TextUtils.isEmpty(user.location())) ? GONE : VISIBLE);
        twitter.setVisibility(TextUtils.isEmpty(user.links().twitter()) ? GONE : VISIBLE);
        web.setVisibility(TextUtils.isEmpty(user.links().web()) ? GONE : VISIBLE);
    }

    @Override
    public void setFollowButtonEnabled(boolean enabled) {
        followButton.setEnabled(enabled);
        followButton.animate().alpha(1.0f).start();
    }

    @Override
    public void setFollowButtonVisibility(boolean visible) {
        followButton.setVisibility(visible ? VISIBLE : GONE);
    }

    @Override
    public void setFollowing(boolean following) {
        Drawable bg = followButton.getBackground();
        if (following) {
            followButton.setText(R.string.user_following);
            bg.setColorFilter(getResources().getColor(R.color.button_grey), PorterDuff.Mode.SRC_IN);
        } else {
            followButton.setText(R.string.user_follow);
            bg.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.follow_button:
                presenter.toggleFollow();
                break;
            case R.id.location:
                presenter.toMap();
                break;
            case R.id.twitter:
                presenter.toTwitter();
                break;
            case R.id.web:
                presenter.toWeb();
                break;
        }
    }
}
