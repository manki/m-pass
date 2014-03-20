package in.manki.android.mpass;

import java.net.MalformedURLException;
import java.net.URL;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.ConsoleMessage;
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
    getActionBar().setHomeButtonEnabled(true);

    loadApp();
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
      public boolean onConsoleMessage(ConsoleMessage message) {
        Log.d(LOG_TAG, String.format(
                "%s:%d -- %s", message.sourceId(), message.lineNumber(), message.message()));
        return super.onConsoleMessage(message);
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    getMenuInflater().inflate(R.menu.activity_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
      case R.id.menu_item_refresh:
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