package com.example.workbookapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.workbookapp.databinding.FragmentTableBinding;

import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;


public class TableFragment extends Fragment {
    private FragmentTableBinding binding;
    private int userId;
    private ArrayList<Table> tables = new ArrayList<>();
    private DatabaseHelper dbHelper;
    private TableAdapter tableAdapter;

    private Handler handler = new Handler();
    private Runnable runnable;

    public TableFragment(int userId){
        this.userId = userId;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTableBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        dbHelper = new DatabaseHelper(requireContext());

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
                        updateTables(searchTerm);
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

        tables = dbHelper.getTables(userId);
        tableAdapter = new TableAdapter(requireContext(), dbHelper, userId, binding, tables);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL);
        binding.rvTableList.setAdapter(tableAdapter);
        binding.rvTableList.setLayoutManager(linearLayoutManager);
        binding.rvTableList.addItemDecoration(dividerItemDecoration);

        return rootView;
    }

    private void updateTables(String searchTerm){
        if(searchTerm.length() > 0){
            binding.etSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search_icon, 0, R.drawable.x_icon, 0);

            tables.clear();
            tables.addAll(dbHelper.getTablesBySearchTerm(userId, searchTerm));
            tableAdapter.notifyDataSetChanged();
        }else {
            binding.etSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search_icon, 0, 0, 0);

            tables.clear();
            tables.addAll(dbHelper.getTables(userId));
            tableAdapter.notifyDataSetChanged();
        }
    }

    private void searchCancelClick(){
        binding.etSearch.setText("");
        binding.etSearch.clearFocus();
        binding.btnSearchCancel.setVisibility(View.GONE);

        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
        }
    }
}