package moe.mewo;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

// Mewo's window — a fullscreen WebView pointing at her den
// (http://127.0.0.1:8765, served by Termux). Cleartext is loopback-only (ED-13).
public class MainActivity extends Activity {
    private WebView wv;
    private boolean loaded = false;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        wv = new WebView(this);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);
        wv.setBackgroundColor(0xFF000000);
        setContentView(wv);
        load();
    }

    private void load() {
        loaded = false;
        wv.setWebViewClient(new WebViewClient() {
            @Override public void onPageFinished(WebView view, String url) { loaded = true; }
        });
        wv.loadUrl("http://127.0.0.1:8765");
        // the den comes up ~20s after boot; retry gently if Termux isn't ready yet
        wv.postDelayed(() -> { if (!loaded) wv.loadUrl("http://127.0.0.1:8765"); }, 4000);
        wv.postDelayed(() -> { if (!loaded) wv.loadUrl("http://127.0.0.1:8765"); }, 10000);
        wv.postDelayed(() -> { if (!loaded) wv.loadUrl("http://127.0.0.1:8765"); }, 20000);
    }

    @Override
    public void onBackPressed() {
        if (wv.canGoBack()) wv.goBack(); else finish();
    }
}
