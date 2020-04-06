package com.changethislater.emojify;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class EmojifyContextMenu extends Activity {

    private LinearLayoutManager optionLayoutManager;
    private RecyclerView optionRecyclerView;
    private RecyclerView.Adapter optionListAdapter;
    private List<String> optionList;
    private CharSequence text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        optionList = new ArrayList<>();
        optionList.add("opt1");
        optionList.add("opt2");
        initView();
        fetchText();
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
    }

    protected void fetchText() {
        text = getIntent()
                .getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);

        boolean readonly = getIntent()
                .getBooleanExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, false);

        if (readonly) {
            //TODO: ADD TOAST
        }
    }

    private void confirm(String result) {
        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_PROCESS_TEXT, result);
        setResult(RESULT_OK, intent);
    }

}
