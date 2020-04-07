package com.changethislater.emojify;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.changethislater.emojify.utils.ReplacementRule;

import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EmojifyContextMenu extends Activity {

    private LinearLayoutManager optionLayoutManager;
    private RecyclerView optionRecyclerView;
    private RecyclerView.Adapter optionListAdapter;
    private TextView sampledTextView;
    //private Map<String, Function<String,String>> optionList;
    private List<ReplacementRule> optionList;
    private CharSequence text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        optionList = new ArrayList<>();
        optionList.add(new ReplacementRule("\uD83D\uDC4F",(String s) -> s.replaceAll(" ","\uD83D\uDC4F")));
        optionList.add(new ReplacementRule("\uD83C\uDD71",(String s) -> s.replaceAll("nigg","ni\uD83C\uDD71\uD83C\uDD71")));
        optionList.add(new ReplacementRule("\uD83D\uDE02",(String s) -> {
            String res = s.replaceAll("lol","\uD83D\uDE02");
            res = res.replaceAll("LOL","\uD83D\uDE02");
            return res;
        }));
        optionList.add(new ReplacementRule("\uD83E\uDD23",(String s) -> s.replaceAll("rofl","\uD83E\uDD23")));
        initView();
        //fetchText();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        fetchText();
    }

    private void initView() {
        setContentView(R.layout.emojify_menu);

        optionRecyclerView = findViewById(R.id.optionRecyclerView);
        optionRecyclerView.setHasFixedSize(true);
        optionLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL, false);
        optionRecyclerView.setLayoutManager(optionLayoutManager);
        optionListAdapter = new OptionAdapter(this.optionList);
        optionRecyclerView.setAdapter(optionListAdapter);

        boolean fetched = fetchText();
        if(fetched) {
            sampledTextView = findViewById(R.id.sampledTextView);
            sampledTextView.setText(text);
        }
    }

    protected boolean fetchText() {
        text = getIntent()
                .getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);

        boolean readonly = getIntent()
                .getBooleanExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, false);

//        if (readonly) {
//            Context context = getApplicationContext();
//            CharSequence text = "Text is read only";
//            int duration = Toast.LENGTH_SHORT;
//            Toast.makeText(context, text, duration).show();
//            finish();
//            return false;
//        }else{
//            return true;
//        }
        return true;
    }

    private void confirm(String result) {
        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_PROCESS_TEXT, result);
        setResult(RESULT_OK, intent);
    }

    public void OKButton(View view) {
        String result = ((OptionAdapter) optionListAdapter).switcheroo(text);
        //String result = ((OptionAdapter) Objects.requireNonNull(optionRecyclerView.getAdapter())).switcheroo(text);
        Log.d("result",result);

        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_PROCESS_TEXT, result);
        setResult(RESULT_OK, intent);
        //finish();
    }
}
