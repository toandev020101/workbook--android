package com.example.workbookapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.workbookapp.databinding.FragmentAccountBinding;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AccountFragment extends Fragment {
    private FragmentAccountBinding binding;
    private int userId;

    private DatabaseHelper dbHelper;
    private User user;

    public AccountFragment(int userId){
        this.userId = userId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        dbHelper = new DatabaseHelper(requireContext());
        user = dbHelper.getUserById(userId);

        if(user != null){
            binding.tvName.setText(user.getFullName());
            binding.tvUsername.setText("@" + user.getUsername());

            if(user.getAvatar() == null){
                binding.ivAvatar.setImageResource(R.drawable.account_large_icon);
            }else {
                binding.ivAvatar.setImageBitmap(convertBase64ToBitMap(user.getAvatar()));
            }

            String shortDate = formatMonthYear(user.getCreatedAt());
            binding.tvCreateAt.setText(requireContext().getResources().getString(R.string.account_tvCreateAt_text)  + " " + shortDate);
        }

        binding.tvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileClick();
            }
        });

        binding.tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutClick();
            }
        });

        return rootView;
    }

    private Bitmap convertBase64ToBitMap(String encodedBitmap){
        // Giải mã chuỗi thành mảng byte
        byte[] decodedString = Base64.decode(encodedBitmap, Base64.DEFAULT);

        // Tạo đối tượng Bitmap từ mảng byte đã giải mã
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedBitmap;
    }

    private String formatMonthYear(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", new Locale("vi", "VN"));
        return dateFormat.format(date);
    }

    private void profileClick(){
        Intent iProfile = new Intent(requireContext(), AccountActivity.class);
        startActivity(iProfile);
    }

    private void logoutClick(){
        AlarmManagerHelper.cancelCardExpirationCheckAlarm(requireContext());
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("auth", requireContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("userId");
        editor.commit();

        Intent iLogin = new Intent(requireContext(), LoginActivity.class);
        startActivity(iLogin);
    }

    @Override
    public void onStart() {
        super.onStart();
        user = dbHelper.getUserById(userId);

        if(user != null){
            binding.tvName.setText(user.getFullName());
            binding.tvUsername.setText("@" + user.getUsername());

            if(user.getAvatar() == null){
                binding.ivAvatar.setImageResource(R.drawable.account_large_icon);
            }else {
                binding.ivAvatar.setImageBitmap(convertBase64ToBitMap(user.getAvatar()));
            }
        }
    }
}