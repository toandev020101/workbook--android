package com.example.workbookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

import com.example.workbookapp.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private DatabaseHelper dbHelper;
    private Drawable customEditText;
    private Drawable customEditTextError;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);
        customEditText = getResources().getDrawable(R.drawable.custom_text_bg_active);
        customEditTextError = getResources().getDrawable(R.drawable.custom_text_bg_error);

        binding.etFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Được gọi trước khi văn bản thay đổi.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Được gọi khi văn bản đang thay đổi.
                binding.tvFullNameError.setVisibility(View.GONE);
                binding.etFullName.setBackground(customEditText);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Được gọi sau khi văn bản đã thay đổi.
                // editable: Dữ liệu văn bản đã thay đổi.
            }
        });

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

        binding.etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Được gọi trước khi văn bản thay đổi.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Được gọi khi văn bản đang thay đổi.
                binding.tvConfirmPasswordError.setVisibility(View.GONE);
                binding.etConfirmPassword.setBackground(customEditText);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Được gọi sau khi văn bản đã thay đổi.
                // editable: Dữ liệu văn bản đã thay đổi.
            }
        });

        binding.etConfirmPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // Xác định xem sự kiện được kích hoạt ở vị trí của drawableRight
                    if (event.getRawX() >= view.getRight() - binding.etConfirmPassword.getCompoundDrawables()[2].getBounds().width()) {
                        // Xử lý sự kiện khi nhấn vào drawableRight ở đây
                        togglePasswordVisibility(binding.etConfirmPassword);
                        return true;
                    }
                }
                return false;
            }
        });


        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerClick();
            }
        });

        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginViewClick();
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

    private void registerClick(){
        String fullName = binding.etFullName.getText().toString();
        String username = binding.etUsername.getText().toString();
        String password = binding.etPassword.getText().toString();
        String confirmPassword = binding.etConfirmPassword.getText().toString();

        boolean checkError = false;

        if(fullName.isEmpty()){
            binding.tvFullNameError.setVisibility(View.VISIBLE);
            binding.tvFullNameError.setText(R.string.register_etFullName_error_empty);
            checkError = true;
        }else if(fullName.length() < 4){
            binding.tvFullNameError.setVisibility(View.VISIBLE);
            binding.tvFullNameError.setText(R.string.register_etFullName_error_min);
            checkError = true;
        }

        if(username.isEmpty()){
            binding.tvUsernameError.setVisibility(View.VISIBLE);
            binding.tvUsernameError.setText(R.string.register_etUsername_error_empty);
            checkError = true;
        }else if(username.length() < 4){
            binding.tvUsernameError.setVisibility(View.VISIBLE);
            binding.tvUsernameError.setText(R.string.register_etUsername_error_min);
            checkError = true;
        }

        if(password.isEmpty()){
            binding.tvPasswordError.setVisibility(View.VISIBLE);
            binding.tvPasswordError.setText(R.string.register_etPassword_error_empty);
            checkError = true;
        }else if(password.length() < 4){
            binding.tvPasswordError.setVisibility(View.VISIBLE);
            binding.tvPasswordError.setText(R.string.register_etPassword_error_min);
            checkError = true;
        }

        if(confirmPassword.isEmpty()){
            binding.tvConfirmPasswordError.setVisibility(View.VISIBLE);
            binding.tvConfirmPasswordError.setText(R.string.register_etConfirmPassword_error_empty);
            checkError = true;
        }else if(!confirmPassword.equals(password)){
            binding.tvConfirmPasswordError.setVisibility(View.VISIBLE);
            binding.tvConfirmPasswordError.setText(R.string.register_etConfirmPassword_error_match);
            checkError = true;
        }

        if(!checkError){
            boolean checkUsername = dbHelper.checkUsername(username);

            if (checkUsername){
                binding.tvUsernameError.setVisibility(View.VISIBLE);
                binding.tvUsernameError.setText(R.string.register_etUsername_error_exists);
                return;
            }

            int newUserId = dbHelper.addUser(fullName, username, password);
            if(newUserId != -1){
                Toast.makeText(this, "Xin chào, " + fullName, Toast.LENGTH_SHORT).show();
                Intent iMain = new Intent(this, MainActivity.class);

                // Lấy instance của SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE);
                SharedPreferences.Editor sharedEditor = sharedPreferences.edit();

                // Lưu trữ một số nguyên
                sharedEditor.putInt("userId", newUserId);
                // Lưu trữ dữ liệu
                sharedEditor.commit();

                startActivity(iMain);
            }else {
                Toast.makeText(this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loginViewClick(){
        Intent iLogin = new Intent(this, LoginActivity.class);
        startActivity(iLogin);
    }
}