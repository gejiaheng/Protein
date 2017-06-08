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
package com.ge.protein.ui.epoxy.models;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ge.protein.R;
import com.ge.protein.data.model.Shot;
import com.ge.protein.ui.epoxy.BaseEpoxyHolder;
import com.ge.protein.ui.widget.BadgedFourThreeImageView;
import com.ge.protein.util.glide.DribbbleTarget;

import butterknife.BindView;

@EpoxyModelClass(layout = R.layout.epoxy_shot_list_simple)
public abstract class ShotListSimpleModel extends EpoxyModelWithHolder<ShotListSimpleModel.ShotListSimpleHolder> {

    @EpoxyAttribute
    Shot shot;
    @EpoxyAttribute
    Context context;
    @EpoxyAttribute
    View.OnClickListener shotOnClickListener;
    @EpoxyAttribute
    View.OnClickListener userOnClickListener;

    int adapterPosition;

    @Override
    protected ShotListSimpleHolder createNewHolder() {
        return new ShotListSimpleHolder();
    }

    @Override
    public void bind(ShotListSimpleHolder holder) {
        super.bind(holder);

        int shotPlaceholder = 0;
        // TODO
        switch (adapterPosition % 6) {
            case 0:
                shotPlaceholder = R.color.shot_image_placeholder_8;
                break;
            case 1:
                shotPlaceholder = R.color.shot_image_placeholder_12;
                break;
            case 2:
                shotPlaceholder = R.color.shot_image_placeholder_14;
                break;
            case 3:
            case 4:
                shotPlaceholder = R.color.shot_image_placeholder_20;
                break;
            case 5:
                shotPlaceholder = R.color.shot_image_placeholder_16;
                break;
        }

        holder.shotTitle.setText(shot.title());

        Glide.with(context)
                .load(shot.user().avatar_url())
                .placeholder(R.color.avatar_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(holder.userAvatar);

        Glide.with(context)
                .load(shot.images().best())
                .placeholder(shotPlaceholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(new DribbbleTarget(holder.shotImage));

        holder.shotImage.showBadge(shot.animated());

        holder.itemView.setOnClickListener(shotOnClickListener);
        holder.avatarWrapper.setOnClickListener(userOnClickListener);
        holder.itemView.setTag(R.id.clicked_model, shot);
        holder.avatarWrapper.setTag(R.id.clicked_model, shot.user());
    }

    static class ShotListSimpleHolder extends BaseEpoxyHolder {
        @BindView(R.id.item_view)
        View itemView;
        @BindView(R.id.user_avatar)
        ImageView userAvatar;
        @BindView(R.id.avatar_wrapper)
        View avatarWrapper;
        @BindView(R.id.shot_title)
        TextView shotTitle;
        @BindView(R.id.shot_image)
        BadgedFourThreeImageView shotImage;
    }
}
