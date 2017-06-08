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

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ge.protein.R;
import com.ge.protein.util.AccountManager;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isSelected;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainScreenTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule(MainActivity.class);

    @Test
    public void viewPagerSwipe() {
        onView(withId(R.id.view_pager)).perform(swipeLeft());
        if (AccountManager.getInstance().isLogin()) {
            onView(withText(R.string.home_tab_following)).check(matches(isSelected()));
            onView(withId(R.id.view_pager)).perform(swipeLeft());
            onView(withText(R.string.home_tab_recent)).check(matches(isSelected()));
            onView(withId(R.id.view_pager)).perform(swipeLeft());
            onView(withText(R.string.home_tab_debuts)).check(matches(isSelected()));
        } else {
            onView(withText(R.string.home_tab_recent)).check(matches(isSelected()));
            onView(withId(R.id.view_pager)).perform(swipeLeft());
            onView(withText(R.string.home_tab_debuts)).check(matches(isSelected()));
        }
    }
}
