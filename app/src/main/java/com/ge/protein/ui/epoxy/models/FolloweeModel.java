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
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.ge.protein.R;
import com.ge.protein.data.model.Followee;
import com.ge.protein.ui.epoxy.BaseEpoxyHolder;

import butterknife.BindView;

@EpoxyModelClass(layout = R.layout.epoxy_followee)
public abstract class FolloweeModel extends EpoxyModelWithHolder<FolloweeModel.FolloweeHolder> {

    @EpoxyAttribute
    View.OnClickListener itemOnClickListener;
    @EpoxyAttribute
    Followee followee;

    private TransitionOptions transitionOptions = DrawableTransitionOptions.withCrossFade();
    private RequestOptions requestOptions = RequestOptions.placeholderOf(R.color.avatar_placeholder)
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    @Override
    protected FolloweeHolder createNewHolder() {
        return new FolloweeHolder();
    }

    @Override
    public void bind(FolloweeHolder holder) {
        super.bind(holder);

        holder.name.setText(followee.followee().name());
        holder.info.setText(holder.itemView.getContext().getString(R.string.user_info, followee.followee().shots_count(),
                followee.followee().followers_count()));

        Glide.with(holder.itemView.getContext())
                .load(followee.followee().avatar_url())
                .transition(transitionOptions)
                .apply(requestOptions)
                .into(holder.avatar);

        holder.itemView.setOnClickListener(itemOnClickListener);
        holder.itemView.setTag(R.id.clicked_model, followee);
    }

    static class FolloweeHolder extends BaseEpoxyHolder {
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
