package com.example.workbookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.workbookapp.databinding.ActivityAccountBinding;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AccountActivity extends AppCompatActivity {
    ActivityAccountBinding binding;
    private DatabaseHelper dbHelper;
    private Drawable customEditText;
    private Drawable customEditTextError;

    private Calendar calendar;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    private User user;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

    private Bitmap avatarBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE);

        int userId = sharedPreferences.getInt("userId", 0);
        if(userId != 0){
            dbHelper = new DatabaseHelper(this);
            customEditText = getResources().getDrawable(R.drawable.custom_text_bg_active);
            customEditTextError = getResources().getDrawable(R.drawable.custom_text_bg_error);

            user = dbHelper.getUserById(userId);
            calendar = Calendar.getInstance();
            calendar.set(2002,1,1);

            if(user != null){
                if(user.getAvatar() == null){
                    binding.ivAvatar.setImageResource(R.drawable.account_large_bold_icon);
                }else {
                    binding.ivAvatar.setImageBitmap(convertBase64ToBitMap(user.getAvatar()));
                }

                binding.etFullName.setText(user.getFullName());
                binding.etUsername.setText(user.getUsername());

                Date defaultDate = new Date(0);
                if(user.getBirthDay() != null && user.getBirthDay().getTime() != defaultDate.getTime()){
                    calendar.setTime(user.getBirthDay());
                    binding.etBirthDate.setText(dateFormat.format(calendar.getTime()));
                }
            }

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

            binding.etBirthDate.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                    // Được gọi trước khi văn bản thay đổi.
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    // Được gọi khi văn bản đang thay đổi.
                    binding.tvBirthDateError.setVisibility(View.GONE);
                    binding.etBirthDate.setBackground(customEditText);
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    // Được gọi sau khi văn bản đã thay đổi.
                    // editable: Dữ liệu văn bản đã thay đổi.
                }
            });

            binding.etBirthDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDateTimePicker(binding.etBirthDate);
                }
            });

            binding.btnChangeAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showImagePickerDialog();
                }
            });

            binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateClick();
                }
            });

            binding.btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    private String convertBitMapToBase64(Bitmap bitmap){
        // Chuyển đổi bitmap thành chuỗi Base64
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private Bitmap convertBase64ToBitMap(String encodedBitmap){
        // Giải mã chuỗi thành mảng byte
        byte[] decodedString = Base64.decode(encodedBitmap, Base64.DEFAULT);

        // Tạo đối tượng Bitmap từ mảng byte đã giải mã
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedBitmap;
    }

    public void showDateTimePicker(EditText etDate) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Hiển thị DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth1) -> {
                    calendar.set(year1, monthOfYear, dayOfMonth1);
                    etDate.setText(dateFormat.format(calendar.getTime()));
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    // Phương thức để hiển thị dialog chọn ảnh
    private void showImagePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn ảnh");

        // Thêm các lựa chọn cho dialog
        String[] options = {"Chụp ảnh", "Chọn từ thư viện"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        dispatchTakePictureIntent();
                        break;
                    case 1:
                        pickImageFromGallery();
                        break;
                }
            }
        });

        // Hiển thị dialog
        builder.show();
    }

    // Phương thức để chụp ảnh từ camera
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    // Phương thức để chọn ảnh từ thư viện
    private void pickImageFromGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
                // Ảnh từ camera đã chụp
                Bundle extras = data.getExtras();
                avatarBitmap = (Bitmap) extras.get("data");
            } else if (requestCode == REQUEST_IMAGE_PICK && data != null) {
                // Ảnh từ thư viện đã chọn
                Uri selectedImage = data.getData();
                avatarBitmap = getBitmapFromUri(selectedImage);
            }

            binding.ivAvatar.setImageBitmap(avatarBitmap);
        }
    }

    // Phương thức chuyển đổi Uri thành Bitmap
    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
            if (parcelFileDescriptor != null) {
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                parcelFileDescriptor.close();
                return image;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void updateClick(){
        String fullNameText = binding.etFullName.getText().toString();
        String birthDateText = binding.etBirthDate.getText().toString();
        Date birthday = null;

        boolean checkError = false;

        if(fullNameText.isEmpty()){
            binding.tvFullNameError.setVisibility(View.VISIBLE);
            binding.tvFullNameError.setText(R.string.accountProfile_etFullName_error_empty);
            binding.etFullName.setBackground(customEditTextError);
            checkError = true;
        }

        if(fullNameText.length() < 4){
            binding.tvFullNameError.setVisibility(View.VISIBLE);
            binding.tvFullNameError.setText(R.string.accountProfile_etFullName_error_min);
            binding.etFullName.setBackground(customEditTextError);
            checkError = true;
        }

        try {
            if(!birthDateText.isEmpty()){
                birthday = dateFormat.parse(birthDateText);
            }

            Calendar calendarYear = Calendar.getInstance();
            calendarYear.add(Calendar.YEAR, -10);
            if(birthday != null && birthday.getTime() > calendarYear.getTime().getTime()){
                binding.tvBirthDateError.setVisibility(View.VISIBLE);
                binding.tvBirthDateError.setText(R.string.accountProfile_etBirthDate_error_min);
                binding.etBirthDate.setBackground(customEditTextError);
                checkError = true;
            }

            calendarYear.add(Calendar.YEAR, -100);
            if(birthday != null && birthday.getTime() < calendarYear.getTime().getTime()){
                binding.tvBirthDateError.setVisibility(View.VISIBLE);
                binding.tvBirthDateError.setText(R.string.accountProfile_etBirthDate_error_max);
                binding.etBirthDate.setBackground(customEditTextError);
                checkError = true;
            }
        }catch (ParseException e){
            e.printStackTrace();
        }

        if(!checkError){
            boolean result = dbHelper.updateUser(user.getId(), avatarBitmap == null ? user.getAvatar() : convertBitMapToBase64(avatarBitmap), fullNameText, birthday);
            if(result){
                finish();
            }
        }
    };
}