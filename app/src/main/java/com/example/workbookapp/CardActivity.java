package com.example.workbookapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.workbookapp.databinding.ActivityCardBinding;
import com.example.workbookapp.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class CardActivity extends AppCompatActivity {
    private ActivityCardBinding binding;
    private int tableId;
    private String tableName;
    private ArrayList<Card> cards = new ArrayList<>();
    private DatabaseHelper dbHelper;
    private CardAdapter cardAdapter;
    private Handler handler = new Handler();
    private Runnable runnable;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent iTableData = getIntent();
        tableId = iTableData.getIntExtra("tableId", 0);
        tableName = iTableData.getStringExtra("tableName");

        dbHelper = new DatabaseHelper(this);

        binding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.tvTitle.setText(getResources().getString(R.string.card_tvTitle_text) + " " + tableName);

        binding.etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                binding.btnSearchCancel.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
            }
        });

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchTerm = binding.etSearch.getText().toString();

                if (runnable != null) {
                    handler.removeCallbacks(runnable);
                }

                runnable = new Runnable() {
                    @Override
                    public void run() {
                        updateCards(searchTerm);
                    }
                };

                handler.postDelayed(runnable, 500);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.etSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // Kiểm tra xem EditText có drawableRight hay không
                    if (binding.etSearch.getCompoundDrawables()[2] != null) {
                        // Xác định xem sự kiện được kích hoạt ở vị trí của drawableRight
                        if (event.getRawX() >= view.getRight() - binding.etSearch.getCompoundDrawables()[2].getBounds().width()) {
                            // Xử lý sự kiện khi nhấn vào drawableRight ở đây
                            binding.etSearch.setText("");
                            return true;
                        }
                    }
                }
                return false;
            }
        });

        binding.btnSearchCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchCancelClick();
            }
        });

        cards = dbHelper.getCards(tableId);
        cardAdapter = new CardAdapter(this, dbHelper, tableId, binding, cards);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        binding.rvCardList.setAdapter(cardAdapter);
        binding.rvCardList.setLayoutManager(linearLayoutManager);
        binding.rvCardList.addItemDecoration(dividerItemDecoration);
    }

    private void updateCards(String searchTerm){
        if(searchTerm.length() > 0){
            binding.etSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search_icon, 0, R.drawable.x_icon, 0);

            cards.clear();
            cards.addAll(dbHelper.getCardsBySearchTerm(tableId, searchTerm));
            cardAdapter.notifyDataSetChanged();
        }else {
            binding.etSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search_icon, 0, 0, 0);

            cards.clear();
            cards.addAll(dbHelper.getCards(tableId));
            cardAdapter.notifyDataSetChanged();
        }
    }

    private void searchCancelClick(){
        binding.etSearch.setText("");
        binding.etSearch.clearFocus();
        binding.btnSearchCancel.setVisibility(View.GONE);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
        }
    }
}