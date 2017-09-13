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

import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
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
import com.ge.protein.data.model.Comment;
import com.ge.protein.ui.epoxy.BaseEpoxyHolder;
import com.ge.protein.util.StringUtils;

import butterknife.BindView;

@EpoxyModelClass(layout = R.layout.epoxy_shot_comment)
public abstract class ShotCommentModel extends EpoxyModelWithHolder<ShotCommentModel.ShotCommentHolder> {

    @EpoxyAttribute
    Comment comment;
    @EpoxyAttribute
    View.OnClickListener shotCommentOnClickListener;

    private TransitionOptions transitionOptions = DrawableTransitionOptions.withCrossFade();
    private RequestOptions requestOptions = RequestOptions.placeholderOf(R.color.avatar_placeholder)
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    @Override
    protected ShotCommentHolder createNewHolder() {
        return new ShotCommentHolder();
    }

    @Override
    public void bind(ShotCommentHolder holder) {
        super.bind(holder);

        Glide.with(holder.avatar.getContext())
                .load(comment.user().avatar_url())
                .transition(transitionOptions)
                .apply(requestOptions)
                .into(holder.avatar);
        holder.userName.setText(comment.user().name());
        holder.commentBody.setText(TextUtils.isEmpty(comment.body()) ?
                "" : StringUtils.trimTrailingWhitespace(Html.fromHtml(comment.body())));
        holder.commentBody.setMovementMethod(LinkMovementMethod.getInstance());

        holder.avatar.setOnClickListener(shotCommentOnClickListener);
        holder.avatar.setTag(R.id.clicked_model, comment.user());
    }

    static class ShotCommentHolder extends BaseEpoxyHolder {
        @BindView(R.id.item_view)
        View itemView;
        @BindView(R.id.avatar)
        ImageView avatar;
        @BindView(R.id.user_name)
        TextView userName;
        @BindView(R.id.comment_body)
        TextView commentBody;
    }
}
