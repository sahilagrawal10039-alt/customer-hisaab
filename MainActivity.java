package com.sahil.customerhisaab;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.print.PrintDocumentAdapter;
import android.webkit.WebSettings;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        setContentView(webView);

        WebSettings settings = webView.getSettings();

settings.setJavaScriptEnabled(true);
settings.setDomStorageEnabled(true);
settings.setDatabaseEnabled(true);
settings.setAllowFileAccess(true);
settings.setAllowContentAccess(true);

// Desktop mode ON by default
settings.setUseWideViewPort(true);
settings.setLoadWithOverviewMode(true);
settings.setSupportZoom(true);
settings.setBuiltInZoomControls(true);
settings.setDisplayZoomControls(false);

settings.setUserAgentString(
    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 " +
    "(KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
);

        webView.setWebChromeClient(new WebChromeClient());

        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void printA5() {
                runOnUiThread(() -> printWebView());
            }
        }, "AndroidPrint");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                view.evaluateJavascript(
                    "window.print = function() {" +
                    "AndroidPrint.printA5();" +
                    "};",
                    null
                );
            }
        });

        webView.loadUrl("file:///android_asset/index.html");
    }
        private void printWebView() {

        PrintManager printManager =
            (PrintManager) getSystemService(Context.PRINT_SERVICE);

        if (printManager == null) return;

        PrintDocumentAdapter adapter =
            webView.createPrintDocumentAdapter("Customer Hisaab");

        PrintAttributes attributes =
            new PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.ISO_A5)
                .setResolution(
                    new PrintAttributes.Resolution(
                        "customer_hisaab",
                        "Customer Hisaab",
                        300,
                        300
                    )
                )
                .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                .build();

        printManager.print(
            "Customer Hisaab",
            adapter,
            attributes
        );
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
