package com.example.workbookapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.workbookapp.databinding.ActivityCardBinding;
import com.example.workbookapp.databinding.ItemCardBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private ArrayList<Card> cards = new ArrayList<>();
    private DatabaseHelper dbHelper;
    private Context context;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private int tableId;

    private Card card = new Card();
    private Calendar calendar;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd-MM-yyyy");

    private Drawable customEditText;
    private Drawable customEditTextError;

    public CardAdapter(Context context, DatabaseHelper dbHelper, int tableId, ActivityCardBinding binding, ArrayList<Card> cards) {
        this.context = context;
        this.dbHelper = dbHelper;
        this.tableId = tableId;
        this.cards = cards;

        customEditText = context.getResources().getDrawable(R.drawable.custom_text_bg_active);
        customEditTextError = context.getResources().getDrawable(R.drawable.custom_text_bg_error);

        if(binding != null){
            binding.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showBottomDialog(-1);
                }
            });
        }
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CardViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        viewBinderHelper.setOpenOnlyOne(true);
        Card card = cards.get(position);
        int cardId = card.getId();
        viewBinderHelper.bind(holder.binding.slCardItem, String.valueOf(cardId));
        viewBinderHelper.closeLayout(String.valueOf(cardId));
        holder.link(card);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    class CardViewHolder extends RecyclerView.ViewHolder{
        ItemCardBinding binding;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCardBinding.bind(itemView);

            binding.tvFinish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    showFinishAlertDialog(getAdapterPosition());
                }
            });

            binding.tvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showBottomDialog(getAdapterPosition());
                }
            });

            binding.tvDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteAlertDialog(getAdapterPosition());
                }
            });

            binding.tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(getAdapterPosition());
                }
            });
        }

        public void link(Card card){
            binding.tvName.setText(card.getName());

            int status = card.getStatus();
            if(status == 0){
                binding.tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0,0, 0);
                binding.tvName.setTextColor(context.getResources().getColor(R.color.black));
                binding.tvName.setBackgroundColor(context.getResources().getColor(R.color.white));
                binding.tvEdit.setVisibility(View.VISIBLE);
            }else if(status == 1){
                binding.tvName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.check_square_icon, 0,0, 0);
                binding.tvName.setTextColor(context.getResources().getColor(R.color.white));
                binding.tvName.setBackgroundColor(context.getResources().getColor(R.color.success));

                binding.tvEdit.setVisibility(View.GONE);
                binding.tvFinish.setText("Chưa hoàn thành");
                binding.tvFinish.setBackgroundColor(context.getResources().getColor(R.color.info));
            }else{
                binding.tvName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.x_circle_icon, 0,0, 0);
                binding.tvName.setTextColor(context.getResources().getColor(R.color.white));
                binding.tvName.setBackgroundColor(context.getResources().getColor(R.color.error));
                binding.tvEdit.setVisibility(View.VISIBLE);
            }
        }
    }

    private void showDialog(int position){
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_show_card);

        Card card = cards.get(position);

        EditText etName = dialog.findViewById(R.id.etName);
        EditText etDescription = dialog.findViewById(R.id.etDescription);
        EditText etStartDate = dialog.findViewById(R.id.etStartDate);
        EditText etEndDate = dialog.findViewById(R.id.etEndDate);
        EditText etStatus = dialog.findViewById(R.id.etStatus);
        calendar = Calendar.getInstance();

        etName.setText(card.getName());
        etDescription.setText(card.getDescription());

        calendar.setTime(card.getStartDate());
        etStartDate.setText(dateFormat.format(calendar.getTime()));
        calendar.setTime(card.getEndDate());
        etEndDate.setText(dateFormat.format(calendar.getTime()));

        if(card.getStatus() == 0){
            etStatus.setText("Chưa hoàn thành");
            etStatus.setTextColor(context.getResources().getColor(R.color.black));
        }else if(card.getStatus() == 1){
            etStatus.setText("Hoàn thành");
            etStatus.setTextColor(context.getResources().getColor(R.color.success));
        }else {
            etStatus.setText("Đã hết hạn");
            etStatus.setTextColor(context.getResources().getColor(R.color.error));
        }

        Button btnClose = dialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void showBottomDialog(int position){
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_edit_card);

        EditText etName = dialog.findViewById(R.id.etName);
        TextView tvNameError = dialog.findViewById(R.id.tvNameError);
        EditText etDescription = dialog.findViewById(R.id.etDescription);
        EditText etStartDate = dialog.findViewById(R.id.etStartDate);
        TextView tvStartDateError = dialog.findViewById(R.id.tvStartDateError);
        EditText etEndDate = dialog.findViewById(R.id.etEndDate);
        TextView tvEndDateError = dialog.findViewById(R.id.tvEndDateError);
        calendar = Calendar.getInstance();

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Được gọi trước khi văn bản thay đổi.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Được gọi khi văn bản đang thay đổi.
                tvNameError.setVisibility(View.GONE);
                etName.setBackground(customEditText);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Được gọi sau khi văn bản đã thay đổi.
                // editable: Dữ liệu văn bản đã thay đổi.
            }
        });

        etStartDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Được gọi trước khi văn bản thay đổi.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Được gọi khi văn bản đang thay đổi.
                tvStartDateError.setVisibility(View.GONE);
                etStartDate.setBackground(customEditText);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Được gọi sau khi văn bản đã thay đổi.
                // editable: Dữ liệu văn bản đã thay đổi.
            }
        });

        etEndDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Được gọi trước khi văn bản thay đổi.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Được gọi khi văn bản đang thay đổi.
                tvEndDateError.setVisibility(View.GONE);
                etEndDate.setBackground(customEditText);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Được gọi sau khi văn bản đã thay đổi.
                // editable: Dữ liệu văn bản đã thay đổi.
            }
        });

        if (position != - 1){
            card = cards.get(position);
            etName.setText(card.getName());
            etDescription.setText(card.getDescription());
            calendar.setTime(card.getStartDate());
            etStartDate.setText(dateFormat.format(calendar.getTime()));
            calendar.setTime(card.getEndDate());
            etEndDate.setText(dateFormat.format(calendar.getTime()));
        }

        Button btnClose = dialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        Button btnAddOrEdit = dialog.findViewById(R.id.btnAddOrEdit);
        if(position != -1){
            btnAddOrEdit.setText("Lưu lại");
        }
        btnAddOrEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String name = etName.getText().toString();
                    String description = etDescription.getText().toString();
                    String startDateText = etStartDate.getText().toString();
                    String endDateText = etEndDate.getText().toString();
                    Date startDate = null;
                    Date endDate = null;

                    boolean checkError = false;

                    if(name.isEmpty()){
                        tvNameError.setVisibility(View.VISIBLE);
                        tvNameError.setText(R.string.dialog_etName_error_empty);
                        etName.setBackground(customEditTextError);
                        checkError = true;
                    }

                    if(startDateText.isEmpty()){
                        tvStartDateError.setVisibility(View.VISIBLE);
                        tvStartDateError.setText(R.string.dialog_etStartDate_error_empty);
                        etStartDate.setBackground(customEditTextError);
                        checkError = true;
                    }else {
                        startDate = dateFormat.parse(startDateText);
                    }

                    if(!endDateText.isEmpty()){
                        endDate = dateFormat.parse(endDateText);
                    }

                    if(endDate  != null && startDate != null && endDate.getTime() <= startDate.getTime()){
                        tvEndDateError.setVisibility(View.VISIBLE);
                        tvEndDateError.setText(R.string.dialog_etEndDate_error_less);
                        etEndDate.setBackground(customEditTextError);
                        checkError = true;
                    }

                    if(!checkError){
                        if(position == -1){
                            int newCardId = dbHelper.addCard(name, description, 0, tableId, startDate, endDate);
                            if(newCardId != -1){
                                Card card = dbHelper.getCardById(newCardId);
                                if(card != null){
                                    cards.add(0, card);
                                    notifyItemInserted(0);
                                }
                            }
                        }else {
                            boolean result = dbHelper.updateCard(card.getId(), name, description, startDate, endDate);
                            if(result){
                                Card newCard = card;
                                newCard.setName(name);
                                newCard.setDescription(description);
                                newCard.setStatus(endDate.getTime() <= startDate.getTime() ? 2 : 0);
                                newCard.setStartDate(startDate);
                                newCard.setEndDate(endDate);

                                cards.set(position, newCard);
                                notifyItemChanged(position);
                            }
                        }
                        dialog.cancel();
                    }
                }catch (ParseException e){
                    e.printStackTrace();
                }
            }
        });

        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker(etStartDate);
            }
        });

        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker(etEndDate);
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    public void showDateTimePicker(EditText etDate) {
        try {
            calendar = Calendar.getInstance();
            String etDateText = etDate.getText().toString();
            if(!etDateText.isEmpty()){
                Date date = dateFormat.parse(etDateText);
                calendar.setTime(date);
            }else {
                calendar.setTime(new Date());
            }

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            // Hiển thị DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                    (view, year1, monthOfYear, dayOfMonth1) -> {
                        // Sau khi chọn ngày, hiển thị TimePickerDialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                                (view1, hourOfDay1, minute1) -> {
                                    // Sau khi chọn ngày và giờ, cập nhật EditText
                                    calendar.set(year1, monthOfYear, dayOfMonth1, hourOfDay1, minute1);
                                    etDate.setText(dateFormat.format(calendar.getTime()));
                                }, hourOfDay, minute, true);
                        timePickerDialog.show();
                    }, year, month, dayOfMonth);
            datePickerDialog.show();
        }catch (ParseException e){
            e.printStackTrace();
        }
    }

    private void showDeleteAlertDialog(int position){
        Card card = cards.get(position);
        AlertDialog.Builder builderCancel = new AlertDialog.Builder(context)
                .setIcon(R.drawable.logo_active_icon)
                .setTitle("Xóa thẻ")
                .setMessage("Bạn có chắc chắn muốn xóa thẻ '" + card.getName() + "' hay không ?")
                .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean result = dbHelper.removeCard(card.getId());
                        if(result){
                            cards.remove(position);
                            notifyItemRemoved(position);
                        }
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dlgDelete = builderCancel.create();
        dlgDelete.show();
    }

    private void showFinishAlertDialog(int position){
        Card card = cards.get(position);
        String msg = card.getStatus() != 1 ? "Bạn có chắc chắn đã hoàn thành thẻ '" + card.getName() + "' ?" :
                "Bạn có chắc chắn chưa hoàn thành thẻ '" + card.getName() + "' ?";

        AlertDialog.Builder builderCancel = new AlertDialog.Builder(context)
                .setIcon(R.drawable.logo_active_icon)
                .setTitle("Hoàn thành thẻ")
                .setMessage(msg)
                .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int status = card.getStatus() != 1 ? 1 : card.getEndDate().getTime() <= new Date().getTime() ? 2 : 0;
                        boolean result = dbHelper.finishCard(card.getId(), status);
                        if(result){
                            card.setStatus(status);
                            cards.set(position, card);
                            notifyItemChanged(position);
                        }
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dlgFinish = builderCancel.create();
        dlgFinish.show();
    }
}
