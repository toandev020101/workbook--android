package com.example.workbookapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.workbookapp.databinding.ActivityLoginBinding;
import com.example.workbookapp.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SharedPreferences sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE);

        userId = sharedPreferences.getInt("userId", 0);
        if(userId == 0){
            finish();
        }

        replaceFragment(new TableFragment(userId));

        // Create items
        AHBottomNavigationItem menuItemTable = new AHBottomNavigationItem(R.string.main_menuItemTable_text, R.drawable.table_icon, R.color.deep_purple_500);
        AHBottomNavigationItem menuItemCard = new AHBottomNavigationItem(R.string.main_menuItemCard_text, R.drawable.card_icon, R.color.deep_purple_500);
        AHBottomNavigationItem menuItemNotify = new AHBottomNavigationItem(R.string.main_menuItemNotify_text, R.drawable.notify_icon, R.color.deep_purple_500);
        AHBottomNavigationItem menuItemAccount = new AHBottomNavigationItem(R.string.main_menuItemAccount_text, R.drawable.account_icon, R.color.deep_purple_500);

        // Add items
        binding.bnvNavigate.addItem(menuItemTable);
        binding.bnvNavigate.addItem(menuItemCard);
        binding.bnvNavigate.addItem(menuItemNotify);
        binding.bnvNavigate.addItem(menuItemAccount);

        binding.bnvNavigate.setColored(false);
        binding.bnvNavigate.setDefaultBackgroundColor(getResources().getColor(R.color.white));
        binding.bnvNavigate.setAccentColor(getResources().getColor(R.color.deep_purple_500));
        binding.bnvNavigate.setInactiveColor(getResources().getColor(R.color.gray_500));
        binding.bnvNavigate.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        binding.bnvNavigate.setCurrentItem(0);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        int notifyCount = dbHelper.notifyCount(userId);
        if(notifyCount == 0){
            binding.bnvNavigate.setNotification("", 2);
        }else {
            binding.bnvNavigate.setNotification(String.valueOf(notifyCount), 2);
        }

        // Set listeners
        binding.bnvNavigate.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                navigateClick(position);
                return true;
            }
        });

        AHBottomNavigationHolder.getInstance().setBnvNavigate(binding.bnvNavigate);
    }

    private void navigateClick(int position){
        switch (position){
            case 0:
                replaceFragment(new TableFragment(userId));
                break;
            case 1:
                replaceFragment(new CardFragment(userId));
                break;
            case 2:
                replaceFragment(new NotifyFragment(userId, binding.bnvNavigate));
                break;
            case 3:
                replaceFragment(new AccountFragment(userId));
                break;
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flFragment, fragment);
        fragmentTransaction.commit();
    }
}