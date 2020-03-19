package com.changethislater.emojify;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

public class EmojifyContextMenu extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleText();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleText();
    }

    protected void handleText() {
        CharSequence text = getIntent()
                .getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
        setContentView(R.layout.emojify_menu);
        String res = text.toString().replace(" ", "\uD83D\uDC4F");
        boolean readonly = getIntent()
                .getBooleanExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, false);

        if (!readonly) {
            Intent intent = new Intent();
            intent.putExtra(Intent.EXTRA_PROCESS_TEXT, res);
            setResult(RESULT_OK, intent);
        }
    }

}
