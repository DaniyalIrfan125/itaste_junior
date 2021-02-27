package com.techbayportal.itaste.ui.fragments.webviewfragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Message
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.Toast
import androidx.core.view.isNotEmpty
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentWebViewBinding
import com.techbayportal.itaste.databinding.LayoutWelcomefragmentBinding
import com.techbayportal.itaste.ui.fragments.welcomefragment.WelcomeViewModel
import com.techbayportal.itaste.utils.NetworkHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_web_view.*
import timber.log.Timber

@AndroidEntryPoint
class WebViewFragment : BaseFragment<FragmentWebViewBinding, WebViewViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_web_view
    override val viewModel: Class<WebViewViewModel>
        get() = WebViewViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    lateinit var mView: View
    lateinit var networkHelper: NetworkHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        networkHelper = NetworkHelper(requireContext())
        initialising()

    }

    private fun initialising() {

        webViewInit()


        swipeRefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            mWebView.reload()
        })
    }

    private fun webViewInit(){
        CookieSyncManager.createInstance(requireContext());
        val webSettings: WebSettings = mWebView.settings
        webSettings.javaScriptEnabled = true
        webSettings.setUserAgentString("Mozilla/5.0 (Linux; Android 4.4.4; One Build/KTU84L.H4) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/33.0.0.0 Mobile Safari/537.36 [FB_IAB/FB4A;FBAV/28.0.0.20.16;]")
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        webSettings.domStorageEnabled = true
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.setAppCacheEnabled(true)
        webSettings.loadsImagesAutomatically = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        mWebView.clearView()
        mWebView.measure(100, 100)

        webSettings.pluginState = WebSettings.PluginState.ON
        mWebView.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
                //Required functionality here
                return super.onJsAlert(view, url, message, result)
            }

            override fun onCreateWindow(view: WebView, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message): Boolean {
                val newWebView = WebView(requireContext())
                newWebView.settings.javaScriptEnabled = true
                newWebView.settings.setSupportZoom(true)
                newWebView.settings.builtInZoomControls = true
                newWebView.settings.pluginState = WebSettings.PluginState.ON
                //  newWebView.getSettings().setSupportMultipleWindows(true);
                view.addView(newWebView)
                val transport = resultMsg.obj as WebView.WebViewTransport
                transport.webView = newWebView
                resultMsg.sendToTarget()
                newWebView.webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView,
                        url: String
                    ): Boolean {
                        view.loadUrl(url)
                        return true
                    }
                }
                return true
            }
        }
        mWebView.webViewClient = object : WebViewClient() {
            @SuppressLint("NewApi")
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return if (!networkHelper.isNetworkConnected()) {
                    Toast.makeText(requireContext(), "No Internet Connection", Toast.LENGTH_LONG)
                        .show()
                    true
                } else {
                    if (URLUtil.isNetworkUrl(url)) {
                        view.loadUrl(url)
                    } else {
                        return try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            view.context.startActivity(intent)
                            true
                        } catch (e: Exception) {
                            Timber.d("error" +"shouldOverrideUrlLoading Exception:$e")
                            true
                        }
                    }
                    false
                }
            }

            override fun onPageFinished(view: WebView, url: String) {
                if(swipeRefreshLayout.isNotEmpty() || swipeRefreshLayout != null){
                    swipeRefreshLayout.isRefreshing = false
                }

                super.onPageFinished(view, url)
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                CookieSyncManager.getInstance().sync()
            }
        }

        if(networkHelper.isNetworkConnected()){
            when {
                sharedViewModel.isFromAboutUs -> {
                    tv_webViewTitle.text = sharedViewModel.aboutUsWebViewTitle
                    mWebView.loadUrl(sharedViewModel.aboutUsWebViewURL)

                }
                sharedViewModel.isFromTAndC -> {
                    tv_webViewTitle.text = sharedViewModel.tAndCTitle
                    mWebView.loadUrl(sharedViewModel.tAndCWebViewURL)

                }
                sharedViewModel.isFromHelpAndFaqs -> {
                    tv_webViewTitle.text = sharedViewModel.helpAndFaqsTitle
                    mWebView.loadUrl(sharedViewModel.helpAndFaqsWebViewURL)
                }
            }

        }else{
            Toast.makeText(requireContext(), "No Internet Connection", Toast.LENGTH_LONG).show()
        }
    }

    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onBackButtonClicked.observe(this, Observer {
             Navigation.findNavController(mView).popBackStack()
        })
        mViewModel.onGoBackButtonClicked.observe(this, Observer {
             Navigation.findNavController(mView).popBackStack()
        })

    }


}