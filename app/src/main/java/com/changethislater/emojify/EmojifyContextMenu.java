package com.changethislater.emojify;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.changethislater.emojify.utils.Rule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EmojifyContextMenu extends Activity {

    private LinearLayoutManager optionLayoutManager;
    private RecyclerView optionRecyclerView;
    private OptionAdapter optionListAdapter;
    private TextView sampledTextView;
    private ItemTouchHelper mIth;
    private CharSequence text;

    public static List<Rule> optionList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialiseRuleSet();
        initView();
        //fetchText();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        fetchText();
    }

    private static void initialiseRuleSet() {
        List<Rule> ruleSet = new ArrayList<>();
        ruleSet.add(new Rule("\uD83D\uDC4F", (String s) -> s.replaceAll(" ", "\uD83D\uDC4F")));
        ruleSet.add(new Rule("\uD83C\uDD71", (String s) -> {
            String res = s.replaceAll("g", "\uD83C\uDD71");
            res = res.replaceAll("G", "\uD83C\uDD71");
            res = res.replaceAll("b", "\uD83C\uDD71");
            res = res.replaceAll("B", "\uD83C\uDD71");
            return res;
        }));

        ruleSet.add(new Rule("\uD83D\uDE02", (String s) -> {
            String res = s.replaceAll("lol", "\uD83D\uDE02");
            res = res.replaceAll("LOL", "\uD83D\uDE02");
            res = res.replaceAll("Lol", "\uD83D\uDE02");
            return res;
        }));
        ruleSet.add(new Rule("\uD83E\uDD23",(String s) -> {
            String res = s.replaceAll("rofl", "\uD83E\uDD23");
            res = res.replaceAll("Rofl", "\uD83E\uDD23");
            res = res.replaceAll("ROFL", "\uD83E\uDD23\uD83E\uDD23");
            return res;
        }));
        ruleSet.add(new Rule("I NeEd hEaLtHcArE bEcAuSe", (String s) -> {
            Random random = new Random();
            random.setSeed(System.currentTimeMillis());
            char[] letters = s.toCharArray();
            StringBuilder res = new StringBuilder();
            for (char letter : letters) {
                if (Character.isAlphabetic(letter) && random.nextDouble() < 0.35) {
                    if (Character.isUpperCase(letter)) {
                        res.append((char)(letter + 32));
                    }
                    if (Character.isLowerCase(letter)) {
                        res.append((char)(letter - 32));
                    }
                } else {
                    res.append(letter);
                }
            }
            return res.toString();
        }));
        ruleSet.add(new Rule("UWU", (String s) -> {
            Random random = new Random();
            random.setSeed(System.currentTimeMillis());
            String[] faces = {"(・`ω´・)", "OwO", "owo", "oωo", "òωó", "°ω°", "UwU", ">w<", "^w^", ";;w;;", "uwu", "סשס"};
            s = s.replaceAll("[rl]", "w")
                    .replaceAll("[RL]", "W")
                    .replaceAll("n([aeiou])", "ny$1")
                    .replaceAll("N([aeiou])", "Ny$1")
                    .replaceAll("N([AEIOU])", "Ny$1");

            long count = s.chars().filter(c -> c == '!').count();
            for (int i = 0; i < count; i++) {
                s = s.replaceFirst("!", " " + faces[random.nextInt(faces.length)] + " ");
            }

            return s;
        }));
        EmojifyContextMenu.optionList = ruleSet;
    }

    private void initView() {
        setContentView(R.layout.emojify_menu);

        optionRecyclerView = findViewById(R.id.optionRecyclerView);
        optionRecyclerView.setHasFixedSize(true);
        optionLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        optionRecyclerView.setLayoutManager(optionLayoutManager);
        optionListAdapter = new OptionAdapter(this);
        optionRecyclerView.setAdapter(optionListAdapter);

        mIth = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        0) {
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

                        final int fromPos = viewHolder.getAdapterPosition();
                        final int toPos = target.getAdapterPosition();

                        ((OptionAdapter) optionListAdapter).moveItem(fromPos, toPos);
                        optionListAdapter.notifyItemMoved(fromPos, toPos);
                        updateSampledTextView();
                        return true;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                    }


                    @Override
                    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                        super.onSelectedChanged(viewHolder, actionState);
                        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                            assert viewHolder != null;
                            viewHolder.itemView.setAlpha(0.5f);
                        }
                    }

                    @Override
                    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                        super.clearView(recyclerView, viewHolder);
                        viewHolder.itemView.setAlpha(1.0f);
                    }


                });

        mIth.attachToRecyclerView(optionRecyclerView);
        boolean fetched = fetchText();
        if (fetched) {
            sampledTextView = findViewById(R.id.sampledTextView);
            sampledTextView.setText(text);
        }

    }

    public void startDragging(RecyclerView.ViewHolder viewHolder){
        mIth.startDrag(viewHolder);
    }

    protected boolean fetchText() {
        text = getIntent()
                .getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);

        boolean readonly = getIntent()
                .getBooleanExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, false);

        if (readonly) {
            Context context = getApplicationContext();
            CharSequence text = "Text is read only";
            int duration = Toast.LENGTH_LONG;
            Toast.makeText(context, text, duration).show();
            finish();
            return false;
        } else {
            return true;
        }

    }

    public String applyOptions(@NonNull CharSequence input) {
        String result = input.toString();
        for (int i = 0; i < optionList.size(); i++) {
            if (optionListAdapter.isChecked(i)) {
                result = optionList.get(i).apply(result);
            }
        }
        return result;
    }

    public void cancel(View view) {
        if(view instanceof Button){
            if (((Button) view).getText().toString().toLowerCase().equals("cancel")){
                Intent intent = getIntent();
                intent.putExtra(Intent.EXTRA_PROCESS_TEXT, text);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    public void OKButton(View view) {
        String result = applyOptions(text);
        Intent intent = getIntent();
        intent.putExtra(Intent.EXTRA_PROCESS_TEXT, result);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void updateSampledTextView() {
            this.sampledTextView.setText(this.applyOptions(this.text));
    }

    public void updateSampledTextView(View view) {
        if (view instanceof CheckBox) {
            updateSampledTextView();
        }
    }
}
