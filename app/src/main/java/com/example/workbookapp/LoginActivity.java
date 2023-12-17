package com.example.workbookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.workbookapp.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private DatabaseHelper dbHelper;
    private Drawable customEditText;
    private Drawable customEditTextError;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        NotificationHelper.requestOverlayPermission(this);
        SharedPreferences sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE);

        int userId = sharedPreferences.getInt("userId", 0);
        if(userId != 0){
            // Đăng ký theo dõi
            AlarmManagerHelper.setCardExpirationCheckAlarm(getApplicationContext());
            Intent iMain = new Intent(this, MainActivity.class);
            startActivity(iMain);
        }

        dbHelper = new DatabaseHelper(this);
        customEditText = getResources().getDrawable(R.drawable.custom_text_bg_active);
        customEditTextError = getResources().getDrawable(R.drawable.custom_text_bg_error);

        binding.etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Được gọi trước khi văn bản thay đổi.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Được gọi khi văn bản đang thay đổi.
                binding.tvUsernameError.setVisibility(View.GONE);
                binding.etUsername.setBackground(customEditText);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Được gọi sau khi văn bản đã thay đổi.
                // editable: Dữ liệu văn bản đã thay đổi.
            }
        });

        binding.etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Được gọi trước khi văn bản thay đổi.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Được gọi khi văn bản đang thay đổi.
                binding.tvPasswordError.setVisibility(View.GONE);
                binding.etPassword.setBackground(customEditText);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Được gọi sau khi văn bản đã thay đổi.
                // editable: Dữ liệu văn bản đã thay đổi.
            }
        });

        binding.etPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // Xác định xem sự kiện được kích hoạt ở vị trí của drawableRight
                    if (event.getRawX() >= view.getRight() - binding.etPassword.getCompoundDrawables()[2].getBounds().width()) {
                        // Xử lý sự kiện khi nhấn vào drawableRight ở đây
                        togglePasswordVisibility(binding.etPassword);
                        return true;
                    }
                }
                return false;
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginClick();
            }
        });

        binding.tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerViewClick();
            }
        });
    }

    private void togglePasswordVisibility(EditText editText) {
        if (editText.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
            // Hiển thị password
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock_icon, 0, R.drawable.show_icon, 0);
        } else {
            // Ẩn password
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock_icon, 0, R.drawable.hide_icon, 0);
        }
    }

    private void loginClick(){
        String username = binding.etUsername.getText().toString();
        String password = binding.etPassword.getText().toString();

        boolean checkError = false;

        if(username.isEmpty()){
            binding.tvUsernameError.setVisibility(View.VISIBLE);
            binding.tvUsernameError.setText(R.string.login_etUsername_error_empty);
            binding.etUsername.setBackground(customEditTextError);
            checkError = true;
        }

        if(password.isEmpty()){
            binding.tvPasswordError.setVisibility(View.VISIBLE);
            binding.tvPasswordError.setText(R.string.login_etPassword_error_empty);
            binding.etPassword.setBackground(customEditTextError);
            checkError = true;
        }

        if(!checkError){
            User user = dbHelper.getUserByUsernameAndPassword(username, password);
            if(user != null){
                Toast.makeText(this, "Xin chào, " + user.getFullName(), Toast.LENGTH_SHORT).show();
                Intent iMain = new Intent(this, MainActivity.class);

                // Lấy instance của SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE);
                SharedPreferences.Editor sharedEditor = sharedPreferences.edit();

                // Lưu trữ một số nguyên
                sharedEditor.putInt("userId", user.getId());
                // Lưu trữ dữ liệu
                sharedEditor.commit();
                // Đăng ký theo dõi
                AlarmManagerHelper.setCardExpirationCheckAlarm(getApplicationContext());

                startActivity(iMain);
            }else {
                binding.tvUsernameError.setVisibility(View.VISIBLE);
                binding.tvUsernameError.setText(R.string.login_error_equal);
                binding.etUsername.setBackground(customEditTextError);

                binding.tvPasswordError.setVisibility(View.VISIBLE);
                binding.tvPasswordError.setText(R.string.login_error_equal);
                binding.etPassword.setBackground(customEditTextError);

                Toast.makeText(this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerViewClick(){
        Intent iRegister = new Intent(this, RegisterActivity.class);
        startActivity(iRegister);
    }
}