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

@EpoxyModelClass(layout = R.layout.epoxy_shot_grid_simple)
public abstract class ShotGridSimpleModel extends EpoxyModelWithHolder<ShotGridSimpleModel.ShotGridSimpleHolder> {

    @EpoxyAttribute Shot shot;
    @EpoxyAttribute
    Context context;
    @EpoxyAttribute
    View.OnClickListener shotOnClickListener;
    @EpoxyAttribute
    int adapterPosition;

    @Override
    protected ShotGridSimpleHolder createNewHolder() {
        return new ShotGridSimpleHolder();
    }

    @Override
    public void bind(ShotGridSimpleHolder holder) {
        super.bind(holder);

        int placeholder = 0;
        switch (adapterPosition % 6) {
            case 0:
                placeholder = R.color.shot_image_placeholder_8;
                break;
            case 1:
                placeholder = R.color.shot_image_placeholder_12;
                break;
            case 2:
                placeholder = R.color.shot_image_placeholder_14;
                break;
            case 3:
            case 4:
                placeholder = R.color.shot_image_placeholder_20;
                break;
            case 5:
                placeholder = R.color.shot_image_placeholder_16;
                break;
        }

        Glide.with(context)
                .load(shot.images().best())
                .crossFade()
                .placeholder(placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new DribbbleTarget(holder.shotImage));

        holder.shotImage.showBadge(shot.animated());

        holder.shotImage.setTag(R.id.clicked_model, shot);
        holder.shotImage.setOnClickListener(shotOnClickListener);
    }

    static class ShotGridSimpleHolder extends BaseEpoxyHolder {
        @BindView(R.id.shot_image)
        BadgedFourThreeImageView shotImage;
    }
}
