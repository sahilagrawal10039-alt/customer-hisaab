
package com.sahil.customerhisaab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Base64;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;

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

            @JavascriptInterface
            public void shareFile(
                String mimeType,
                String fileName,
                String base64
            ) {
                runOnUiThread(() -> {
                    try {
                        byte[] bytes = Base64.decode(
                            base64,
                            Base64.DEFAULT
                        );

                        File shareDir = new File(
                            getCacheDir(),
                            "shared"
                        );

                        if (!shareDir.exists()) {
                            shareDir.mkdirs();
                        }

                        String safeName = new File(fileName).getName();
                        File file = new File(shareDir, safeName);

                        try (FileOutputStream fos =
                                 new FileOutputStream(file)) {
                            fos.write(bytes);
                            fos.flush();
                        }

                        Uri uri = FileProvider.getUriForFile(
                            MainActivity.this,
                            getPackageName() + ".fileprovider",
                            file
                        );

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType(mimeType);
                        intent.putExtra(Intent.EXTRA_STREAM, uri);
                        intent.addFlags(
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                        );

                        startActivity(
                            Intent.createChooser(
                                intent,
                                "Share Customer Hisaab"
                            )
                        );

                    } catch (Exception e) {
                        Toast.makeText(
                            MainActivity.this,
                            "File share nahi ho payi: " + e.getMessage(),
                            Toast.LENGTH_LONG
                        ).show();
                    }
                });
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

        webView.loadUrl(
            "file:///android_asset/index.html"
        );
    }

    private void printWebView() {
        PrintManager printManager =
            (PrintManager) getSystemService(
                Context.PRINT_SERVICE
            );

        if (printManager == null) return;

        PrintDocumentAdapter adapter =
            webView.createPrintDocumentAdapter(
                "Customer Hisaab"
            );

        PrintAttributes attributes =
            new PrintAttributes.Builder()
                .setMediaSize(
                    PrintAttributes.MediaSize.ISO_A5
                )
                .setResolution(
                    new PrintAttributes.Resolution(
                        "Customer_Hisaab",
                        "Customer_Hisaab",
                        300,
                        300
                    )
                )
                .setMinMargins(
                    PrintAttributes.Margins.NO_MARGINS
                )
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
