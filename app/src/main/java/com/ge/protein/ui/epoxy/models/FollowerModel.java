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

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ge.protein.R;
import com.ge.protein.data.model.Follower;
import com.ge.protein.ui.epoxy.BaseEpoxyHolder;

import butterknife.BindView;

@EpoxyModelClass(layout = R.layout.epoxy_follower)
public abstract class FollowerModel extends EpoxyModelWithHolder<FollowerModel.FollowerHolder> {

    @EpoxyAttribute
    View.OnClickListener itemOnClickListener;
    @EpoxyAttribute
    Follower follower;

    @Override
    protected FollowerHolder createNewHolder() {
        return new FollowerHolder();
    }

    @Override
    public void bind(FollowerHolder holder) {
        super.bind(holder);

        holder.name.setText(follower.follower().name());
        holder.info.setText(holder.itemView.getContext().getString(R.string.user_info, follower.follower().shots_count(),
                follower.follower().followers_count()));

        Glide.with(holder.itemView.getContext())
                .load(follower.follower().avatar_url())
                .crossFade()
                .placeholder(R.color.avatar_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.avatar);

        holder.itemView.setOnClickListener(itemOnClickListener);
        holder.itemView.setTag(R.id.clicked_model, follower);
    }

    static class FollowerHolder extends BaseEpoxyHolder {
        @BindView(R.id.item_view)
        View itemView;
        @BindView(R.id.avatar)
        ImageView avatar;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.info)
        TextView info;
    }
}
