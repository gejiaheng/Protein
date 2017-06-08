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
package com.ge.protein.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.ge.protein.R;
import com.ge.protein.auth.AuthActivity;
import com.ge.protein.util.RxUtils;
import com.jakewharton.rxbinding2.view.RxView;

public class LoginGuideDialog extends DialogFragment {

    public static LoginGuideDialog newInstance() {
        return new LoginGuideDialog();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogLoginGuide;
    }

    @NonNull
    @Override
    @SuppressLint("InflateParams")
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_login_guide, null);
        View loginButton = root.findViewById(R.id.login_button);
        RxView.clicks(loginButton)
                .throttleFirst(RxUtils.WINDOW_DURATION, RxUtils.TIME_UNIT)
                .subscribe(view -> {
                    dismiss();
                    startActivity(new Intent(getContext(), AuthActivity.class));
                });
        builder.setView(root);
        return builder.create();
    }

}
