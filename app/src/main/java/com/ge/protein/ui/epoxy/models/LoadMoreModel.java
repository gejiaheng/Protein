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

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.ge.protein.R;
import com.ge.protein.ui.epoxy.BaseEpoxyHolder;

import butterknife.BindView;

@EpoxyModelClass(layout = R.layout.epoxy_load_more)
public abstract class LoadMoreModel extends EpoxyModelWithHolder<LoadMoreModel.LoadMoreHolder>{

    private AnimatorSet animatorSet;

    @Override
    protected LoadMoreHolder createNewHolder() {
        return new LoadMoreHolder();
    }

    @Override
    public void bind(LoadMoreHolder holder) {
        super.bind(holder);
        if (animatorSet == null) {
            ObjectAnimator outerXAnimator = ObjectAnimator.ofFloat(holder.outer, "scaleX", 0.78f);
            outerXAnimator.setRepeatCount(ValueAnimator.INFINITE);
            outerXAnimator.setRepeatMode(ValueAnimator.REVERSE);

            ObjectAnimator outerYAnimator = ObjectAnimator.ofFloat(holder.outer, "scaleY", 0.78f);
            outerYAnimator.setRepeatCount(ValueAnimator.INFINITE);
            outerYAnimator.setRepeatMode(ValueAnimator.REVERSE);

            ObjectAnimator innerXAnimator = ObjectAnimator.ofFloat(holder.inner, "scaleX", 1.33f);
            innerXAnimator.setRepeatCount(ValueAnimator.INFINITE);
            innerXAnimator.setRepeatMode(ValueAnimator.REVERSE);

            ObjectAnimator innerYAnimator = ObjectAnimator.ofFloat(holder.inner, "scaleY", 1.33f);
            innerYAnimator.setRepeatCount(ValueAnimator.INFINITE);
            innerYAnimator.setRepeatMode(ValueAnimator.REVERSE);

            animatorSet = new AnimatorSet();
            animatorSet.playTogether(outerXAnimator, outerYAnimator, innerXAnimator, innerYAnimator);
            animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
            animatorSet.setDuration(400);
        }
        animatorSet.start();
    }

    @Override
    public void unbind(LoadMoreHolder holder) {
        super.unbind(holder);

        if (animatorSet != null) {
            animatorSet.cancel();
        }
    }

    @Override
    public int getSpanSize(int totalSpanCount, int position, int itemCount) {
        return 2;
    }

    static class LoadMoreHolder extends BaseEpoxyHolder {
        @BindView(R.id.outer)
        View outer;
        @BindView(R.id.inner)
        View inner;
    }
}
