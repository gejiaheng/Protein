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
package com.ge.protein.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ge.protein.R;
import com.ge.protein.data.model.User;
import com.ge.protein.util.AccountManager;
import com.ge.protein.util.RxUtils;
import com.jakewharton.rxbinding2.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainView extends CoordinatorLayout implements MainContract.View {

    private MainContract.Presenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.user_layout)
    View userLayout;
    @BindView(R.id.user_avatar)
    ImageView userAvatar;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    public MainView(Context context) {
        super(context);
        init(context);
    }

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.main_view_content, this, true);
        ButterKnife.bind(this);
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setupView() {
        RxView.clicks(userLayout)
                .throttleFirst(RxUtils.WINDOW_DURATION, RxUtils.TIME_UNIT)
                .subscribe(o -> presenter.onToolbarUserClicked());

        toolbar.inflateMenu(R.menu.main_menu);
        toolbar.getMenu().findItem(R.id.action_logout).setEnabled(AccountManager.getInstance().isLogin());
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_about) {
                presenter.toAbout();
                return true;
            } else if (item.getItemId() == R.id.action_logout) {
                presenter.showLogoutDialog();
                return true;
            }
            return false;
        });

        viewPager.setAdapter(new MainPagerAdapter(((FragmentActivity) getContext()).getSupportFragmentManager()));

        // Add 4 tabs for TabLayout
        tabLayout.addTab(tabLayout.newTab().setText(R.string.home_tab_popular)); // Popular
        if (AccountManager.getInstance().isLogin()) {
            tabLayout.addTab(tabLayout.newTab().setText(R.string.home_tab_following)); // Following
        }
        tabLayout.addTab(tabLayout.newTab().setText(R.string.home_tab_recent)); // Recent
        tabLayout.addTab(tabLayout.newTab().setText(R.string.home_tab_debuts)); // Debuts

        // Setup sync between TabLayout and ViewPager
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.setOffscreenPageLimit(3);
    }

    @Override
    public void setDefaultUserInfo() {
        Glide.with(getContext())
                .load(R.mipmap.ic_launcher)
                .crossFade()
                .into(userAvatar);
        setUserName(getContext().getString(R.string.action_login));
    }

    @Override
    public void setUserInfo(User user) {
        Glide.with(getContext())
                .load(user.avatar_url())
                .crossFade()
                .placeholder(R.color.avatar_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(userAvatar);
        setUserName(user.name());
    }

    private void setUserName(final String name) {
        String previous = userName.getText().toString();
        if (TextUtils.isEmpty(previous)) {
            userName.setText(name);
            userName.setAlpha(0.0f);
            userName.animate()
                    .alpha(1.0f)
                    .start();
        } else {
            userName.animate()
                    .alpha(0.0f)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            userName.setText(name);
                            userName.animate().alpha(1.0f).start();
                        }
                    }).start();
        }
    }
}
