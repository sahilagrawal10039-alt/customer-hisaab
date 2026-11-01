package com.sahil.customerhisaab;

import android.app.Activity;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.content.Context;

public class MainActivity extends Activity {
    private WebView webView;
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        setContentView(webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface public void printA5() { runOnUiThread(() -> printWebView()); }
        }, "AndroidPrint");
        webView.loadUrl("file:///android_asset/index.html");
        webView.setWebViewClient(new WebViewClient() {
            @Override public void onPageFinished(WebView view, String url) {
                view.evaluateJavascript("window.print=function(){if(window.AndroidPrint){AndroidPrint.printA5();}};", null);
            }
        });
    }
    private void printWebView() {
        PrintManager pm = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        if (pm == null) return;
        android.print.PrintDocumentAdapter adapter = webView.createPrintDocumentAdapter("Customer-Hisaab-A5");
        PrintAttributes attrs = new PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.ISO_A5.asLandscape())
                .setResolution(new PrintAttributes.Resolution("hisaab", "Customer Hisaab", 300, 300))
                .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                .build();
        pm.print("Customer Hisaab", adapter, attrs);
    }
    @Override public void onBackPressed() {
        if (webView != null && webView.canGoBack()) webView.goBack(); else super.onBackPressed();
    }
}
