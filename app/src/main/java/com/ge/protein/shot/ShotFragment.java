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
package com.ge.protein.shot;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ge.protein.R;
import com.ge.protein.data.model.Shot;
import com.ge.protein.mvp.BaseFragment;
import com.ge.protein.ui.widget.BadgedFourThreeImageView;
import com.ge.protein.util.StringUtils;
import com.ge.protein.util.glide.DribbbleTarget;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.placeholderOf;

public class ShotFragment extends BaseFragment implements ShotContract.View {

    private ShotContract.Presenter presenter;

    @BindView(R.id.shot_image)
    BadgedFourThreeImageView shotImage;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab_like)
    FloatingActionButton fabLike;
    @BindView(R.id.shot_title)
    TextView shotTitle;
    @BindView(R.id.user_avatar)
    ImageView userAvatar;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.created_time)
    TextView createdTime;
    @BindView(R.id.shot_desc)
    TextView shotDesc;
    @BindView(R.id.shot_likes_count)
    View shotLikesCount;
    @BindView(R.id.shot_likes_count_text)
    TextView shotLikesCountText;
    @BindView(R.id.shot_views_count)
    View shotViewsCount;
    @BindView(R.id.shot_views_count_text)
    TextView shotViewsCountText;
    @BindView(R.id.shot_comments_count)
    View shotCommentsCount;
    @BindView(R.id.shot_comments_count_text)
    TextView shotCommentsCountText;
    @BindView(R.id.shot_share)
    View shotShare;

    public static ShotFragment newInstance() {
        return new ShotFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shot, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.getNavigationIcon().setColorFilter(ResourcesCompat.getColor(getResources(), R.color.icon_grey,
                getActivity().getTheme()), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());

        presenter.start();
    }

    @Override
    public void setPresenter(ShotContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void show(Shot shot) {
        Glide.with(this)
                .load(shot.images().best())
                .transition(withCrossFade())
                .apply(placeholderOf(R.color.shot_image_placeholder_8).diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(new DribbbleTarget(shotImage));

        shotTitle.setText(shot.title());
        if (shot.user() != null) {
            Glide.with(this)
                    .load(shot.user().avatar_url())
                    .transition(withCrossFade())
                    .apply(placeholderOf(R.color.avatar_placeholder).diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(userAvatar);
            userName.setText(shot.user().name());
        }
        createdTime.setText(DateUtils.getRelativeTimeSpanString(shot.created_at().getTime(),
                System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS));
        shotDesc.setText(TextUtils.isEmpty(shot.description()) ? "" :
                StringUtils.trimTrailingWhitespace(Html.fromHtml(shot.description())));
        shotDesc.setVisibility(TextUtils.isEmpty(shot.description()) ? View.GONE : View.VISIBLE);
        shotDesc.setMovementMethod(LinkMovementMethod.getInstance());
        shotLikesCountText.setText(shotLikesCountText.getContext().getString(R.string.shot_likes_count, shot.likes_count()));
        shotViewsCountText.setText(shotViewsCountText.getContext().getString(R.string.shot_views_count, shot.views_count()));
        shotCommentsCountText.setText(shotCommentsCountText.getContext().getString(R.string.shot_comments_count,
                shot.comments_count()));

        fabLike.setOnClickListener(shotDetailClickListener);
        userAvatar.setOnClickListener(shotDetailClickListener);
        shotLikesCount.setOnClickListener(shotDetailClickListener);
        shotViewsCount.setOnClickListener(shotDetailClickListener);
        shotCommentsCount.setOnClickListener(shotDetailClickListener);
        shotShare.setOnClickListener(shotDetailClickListener);
        userAvatar.setTag(R.id.clicked_model, shot.user());
        shotCommentsCount.setTag(R.id.clicked_model, shot);
        shotShare.setTag(R.id.clicked_model, shot);
    }

    @Override
    public void setLikeFabVisibility(boolean visible) {
        fabLike.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void setLikeStatus(boolean like) {
        fabLike.setImageResource(like ? R.drawable.ic_favorite_white_24dp : R.drawable.ic_favorite_border_white_24dp);
    }

    @Override
    public void updateLikesCount(String likesCountText) {
        shotLikesCountText.setText(likesCountText);
    }

    @Override
    public void showSnackbar(int resId) {
        showSnackbar(getString(resId));
    }

    @Override
    public void showSnackbar(String message) {
        if (getView() == null)
            return;

        Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }

    private View.OnClickListener shotDetailClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.fab_like) {
                presenter.toggleLike();
            } else if (v.getId() == R.id.user_avatar) {
                presenter.toUser();
            } else if (v.getId() == R.id.shot_comments_count) {
                presenter.toComments();
            } else if (v.getId() == R.id.shot_share) {
                presenter.toShare();
            }
        }
    };
}
