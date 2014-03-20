package in.manki.android.mpass;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

public class JavaScriptInterface {

  private final Context context;

  public JavaScriptInterface(Context context) {
    this.context = context;
  }

  public void showToast(String toast) {
    Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
  }

  public void copy(String text) {
    ClipboardManager cm =
        (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    cm.setPrimaryClip(ClipData.newPlainText("password", text));
  }
}
