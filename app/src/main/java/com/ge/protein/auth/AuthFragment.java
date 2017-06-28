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
package com.ge.protein.auth;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ge.protein.BuildConfig;
import com.ge.protein.R;
import com.ge.protein.data.api.ApiConstants;
import com.ge.protein.mvp.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuthFragment extends BaseFragment implements AuthContract.View, SwipeRefreshLayout.OnRefreshListener {

    private AuthContract.Presenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private ProgressDialog progressDialog;

    public static AuthFragment newInstance() {
        return new AuthFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auth, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar.setTitle(R.string.label_auth);
        swipeRefreshLayout.setOnRefreshListener(this);
        progressBar.setProgressTintList(
                ResourcesCompat.getColorStateList(getResources(), R.color.colorAccent, getActivity().getTheme()));

        cleanWebView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            webview.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    Uri uri = request.getUrl();
                    if (ApiConstants.DRIBBBLE_AUTHORIZE_CALLBACK_URI_SCHEMA.equals(uri.getScheme())
                            && ApiConstants.DRIBBBLE_AUTHORIZE_CALLBACK_URI_HOST.equals(
                            uri.getHost())) {
                        String code = uri.getQueryParameter("code");
                        String error = uri.getQueryParameter("error");
                        if (!TextUtils.isEmpty(code)) {
                            presenter.getAccessToken(code);
                        } else if (!TextUtils.isEmpty(error)) {
                            Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                        }
                        return true;
                    }
                    return super.shouldOverrideUrlLoading(view, request);
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            webview.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    Uri uri = Uri.parse(url);
                    if (ApiConstants.DRIBBBLE_AUTHORIZE_CALLBACK_URI_SCHEMA.equals(uri.getScheme())
                            && ApiConstants.DRIBBBLE_AUTHORIZE_CALLBACK_URI_HOST.equals(
                            uri.getHost())) {
                        String code = uri.getQueryParameter("code");
                        String error = uri.getQueryParameter("error");
                        if (!TextUtils.isEmpty(code)) {
                            presenter.getAccessToken(code);
                        } else if (!TextUtils.isEmpty(error)) {
                            Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                        }
                        return true;
                    }
                    return super.shouldOverrideUrlLoading(view, url);
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
            }
        });

        webview.loadUrl(ApiConstants.DRIBBBLE_AUTHORIZE_URL
                + "?client_id=" + BuildConfig.DRIBBBLE_CLIENT_ID
                + "&redirect_uri=" + ApiConstants.DRIBBBLE_AUTHORIZE_CALLBACK_URI
                + "&scope=" + ApiConstants.DRIBBBLE_AUTHORIZE_SCOPE);
    }

    @Override
    public void setPresenter(AuthContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onRefresh() {
        webview.reload();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setProgressDialogVisibility(boolean visible) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
        }

        if (visible) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        } else {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showSnackbar(int resId) {
        showSnackbar(getString(resId));
    }

    @Override
    public void showSnackbar(String message) {
        if (getView() == null || TextUtils.isEmpty(message))
            return;

        Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }

    // clear all cookies in case user can't change his account and password.
    private void cleanWebView() {
        CookieManager.getInstance().removeAllCookies(aBoolean -> {
            // do nothing
        });
    }
}
