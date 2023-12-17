package com.example.workbookapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.example.workbookapp.databinding.FragmentNotifyBinding;

import java.util.ArrayList;

public class NotifyFragment extends Fragment {
    private FragmentNotifyBinding binding;
    private int userId;

    private ArrayList<Notify> notifies = new ArrayList<>();
    private DatabaseHelper dbHelper;
    private NotifyAdapter notifyAdapter;
    private AHBottomNavigation bnvNavigate;

    public NotifyFragment(int userId, AHBottomNavigation bnvNavigate){
        this.userId = userId;
        this.bnvNavigate = bnvNavigate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotifyBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        dbHelper = new DatabaseHelper(requireContext());

        notifies = dbHelper.getNotifies(userId);
        notifyAdapter = new NotifyAdapter(requireContext(), notifies);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL);
        binding.rvNotifyList.setAdapter(notifyAdapter);
        binding.rvNotifyList.setLayoutManager(linearLayoutManager);
        binding.rvNotifyList.addItemDecoration(dividerItemDecoration);

        binding.btnReadAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.readAllNotify(userId);
                notifies.clear();
                notifies.addAll(dbHelper.getNotifies(userId));
                notifyAdapter.notifyDataSetChanged();

                int notifyCount = dbHelper.notifyCount(userId);
                if(notifyCount == 0){
                    bnvNavigate.setNotification("", 2);
                }else {
                    bnvNavigate.setNotification(String.valueOf(notifyCount), 2);
                }
            }
        });


        return rootView;
    }
}