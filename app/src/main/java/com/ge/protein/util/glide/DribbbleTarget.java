/*
 * Copyright 2015 Google Inc.
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

package com.ge.protein.util.glide;

import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.ge.protein.R;
import com.ge.protein.ui.widget.BadgedFourThreeImageView;
import com.ge.protein.util.ColorUtils;


/**
 * A Glide {@see ViewTarget} for {@link BadgedFourThreeImageView}s. It applies a badge for animated
 * images, can prevent GIFs from auto-playing & applies a palette generated ripple.
 */
public class DribbbleTarget extends GlideDrawableImageViewTarget {

    public DribbbleTarget(BadgedFourThreeImageView view) {
        super(view);
    }

    @Override
    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
        super.onResourceReady(resource, animation);

        BadgedFourThreeImageView badgedImageView = (BadgedFourThreeImageView) getView();
        if (resource instanceof GifDrawable) {
            Bitmap image = ((GifDrawable) resource).getFirstFrame();
            // look at the corner to determine the gif badge color
            int cornerSize = (int) (56 * getView().getContext().getResources().getDisplayMetrics
                    ().scaledDensity);
            Bitmap corner = Bitmap.createBitmap(image,
                    image.getWidth() - cornerSize,
                    image.getHeight() - cornerSize,
                    cornerSize, cornerSize);
            boolean isDark = ColorUtils.isDark(corner);
            corner.recycle();
            badgedImageView.setBadgeColor(ContextCompat.getColor(getView().getContext(),
                    isDark ? R.color.gif_badge_dark_image : R.color.gif_badge_light_image));
        }
    }

}
