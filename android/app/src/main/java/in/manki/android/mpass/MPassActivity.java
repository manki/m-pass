package in.manki.android.mpass;

import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MPassActivity extends Activity {
  private static final String APP_HOST = "m-pass.appspot.com";
  private static final String LOG_TAG = "m-pass";

  private WebView web;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    web = (WebView) findViewById(R.id.web);
    configureWebView(web);
  }

  @Override
  protected void onStart() {
    super.onStart();

    if (!isOnLoginPage()) {
      loadApp();
    }
  }

  private boolean isOnLoginPage() {
    try {
      return new URL(web.getUrl()).getAuthority().contains(".google.com");
    } catch (MalformedURLException e) {
      return false;
    }
  }

  private void loadApp() {
    web.loadUrl(String.format("https://%s/?android=yes", APP_HOST));
  }

  private void configureWebView(WebView web) {
    // Open all links within web view (instead of opening the default browser).
    web.setWebViewClient(new WebViewClient());

    WebSettings settings = web.getSettings();
    settings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
    settings.setSaveFormData(false);
    settings.setSavePassword(false);
    settings.setJavaScriptEnabled(true);
    settings.setDomStorageEnabled(true);
    web.addJavascriptInterface(new JavaScriptInterface(this), "AndroidApp");

    web.setWebChromeClient(new WebChromeClient(){
      @Override
      public void onConsoleMessage(String message, int lineNumber,
          String sourceID) {
        super.onConsoleMessage(message, lineNumber, sourceID);
        Log.d(LOG_TAG,
            String.format("%s:%d -- %s", sourceID, lineNumber, message));
      }
    });
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      loadApp();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK && web.canGoBack()) {
      web.goBack();
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }
}