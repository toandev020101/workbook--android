package com.example.workbookapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.workbookapp.databinding.ActivityCardBinding;
import com.example.workbookapp.databinding.FragmentCardBinding;
import com.example.workbookapp.databinding.FragmentTableBinding;

import java.util.ArrayList;


public class CardFragment extends Fragment {
    private FragmentCardBinding binding;
    private int userId;

    private ArrayList<Card> cards = new ArrayList<>();
    private DatabaseHelper dbHelper;
    private CardAdapter cardAdapter;

    public CardFragment(int userId){
        this.userId = userId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCardBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        dbHelper = new DatabaseHelper(requireContext());

        int day = 24 * 60 * 60 * 1000; // 1 day
        int timeAlmost = 3 * day;
        cards = dbHelper.getCardExpireLessByUserId(userId, timeAlmost);
        cardAdapter = new CardAdapter(requireContext(), dbHelper, 0, null, cards);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL);
        binding.rvCardList.setAdapter(cardAdapter);
        binding.rvCardList.setLayoutManager(linearLayoutManager);
        binding.rvCardList.addItemDecoration(dividerItemDecoration);

        return rootView;
    }
}