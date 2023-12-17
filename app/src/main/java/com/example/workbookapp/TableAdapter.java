package com.example.workbookapp;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.workbookapp.databinding.FragmentTableBinding;
import com.example.workbookapp.databinding.ItemTableBinding;
import com.example.workbookapp.databinding.ItemThemeBinding;

import java.util.ArrayList;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder> {
    private ArrayList<Table> tables = new ArrayList<>();
    private DatabaseHelper dbHelper;
    private Context context;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private int userId;
    private Drawable customEditText;
    private Drawable customEditTextError;

    private Table table = new Table();

    public TableAdapter(Context context, DatabaseHelper dbHelper, int userId, FragmentTableBinding binding, ArrayList<Table> tables) {
        this.context = context;
        this.dbHelper = dbHelper;
        this.userId = userId;
        this.tables = tables;

        customEditText = context.getResources().getDrawable(R.drawable.custom_text_bg_active);
        customEditTextError = context.getResources().getDrawable(R.drawable.custom_text_bg_error);

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomDialog(-1);
            }
        });
    }

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TableViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
        viewBinderHelper.setOpenOnlyOne(true);
        Table table = tables.get(position);
        int tableId = table.getId();
        viewBinderHelper.bind(holder.binding.slTableItem, String.valueOf(tableId));
        viewBinderHelper.closeLayout(String.valueOf(tableId));
        holder.link(table);
    }

    @Override
    public int getItemCount() {
        return tables.size();
    }

    class TableViewHolder extends RecyclerView.ViewHolder{
        ItemTableBinding binding;

        public TableViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemTableBinding.bind(itemView);

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
                    table = tables.get(getAdapterPosition());
                    Intent iCard = new Intent(context, CardActivity.class);
                    iCard.putExtra("tableId", table.getId());
                    iCard.putExtra("tableName", table.getName());
                    context.startActivity(iCard);
                }
            });
        }

        public void link(Table table){
            binding.tvName.setText(table.getName());

            int themeId = table.getThemeId();
            if(themeId != 0){
                Theme theme = dbHelper.getThemeById(themeId);
                if(theme != null){
                    binding.tvName.setTextColor(context.getResources().getColor(R.color.white));
                    binding.tvName.setBackgroundColor(Color.parseColor(theme.getColor()));
                }
            }else {
                binding.tvName.setTextColor(context.getResources().getColor(R.color.black));
                binding.tvName.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
        }
    }

    private void showBottomDialog(int position){
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_edit_table);

        ArrayList<Theme> themes = dbHelper.getAllTheme();
        EditText etName = dialog.findViewById(R.id.etName);
        TextView tvNameError = dialog.findViewById(R.id.tvNameError);
        int themeChecked = -1;
        int themeId = 0;

        if (position != - 1){
            table = tables.get(position);
            etName.setText(table.getName());

            Theme themeActive = dbHelper.getThemeById(table.getThemeId());
            if(themeActive != null){
                themeChecked = themes.indexOf(themeActive);
                themeId = themeActive.getId();
            }
        }

        ThemeAdapter themeAdapter = new ThemeAdapter(context, themes, themeChecked, themeId);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        RecyclerView rvThemeList = dialog.findViewById(R.id.rvThemeList);

        rvThemeList.setAdapter(themeAdapter);
        rvThemeList.setLayoutManager(gridLayoutManager);

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
                String nameTable = etName.getText().toString();
                if(nameTable.isEmpty()){
                    tvNameError.setVisibility(View.VISIBLE);
                    tvNameError.setText(R.string.dialog_etName_error_empty);
                    etName.setBackground(customEditTextError);
                }else {
                    int themeId = themeAdapter.itemId;

                    if(position == -1){
                        int newTableId = dbHelper.addTable(nameTable, themeId, userId);
                        if(newTableId != -1){
                            Table table = dbHelper.getTableById(newTableId);
                            if(table != null){
                                tables.add(0, table);
                                notifyItemInserted(0);
                            }
                        }
                    }else {
                        boolean result = dbHelper.updateTable(table.getId(), nameTable, themeId);
                        if(result){
                            Table newTable = table;
                            newTable.setName(nameTable);
                            newTable.setThemeId(themeId);

                            tables.set(position, newTable);
                            notifyItemChanged(position);
                        }
                    }
                    dialog.cancel();
                }
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void showDeleteAlertDialog(int position){
        Table table = tables.get(position);
        AlertDialog.Builder builderCancel = new AlertDialog.Builder(context)
                .setIcon(R.drawable.logo_active_icon)
                .setTitle("Xóa bảng")
                .setMessage("Bạn có chắc chắn muốn xóa bảng '" + table.getName() + "' hay không ?")
                .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean result = dbHelper.removeTable(table.getId());
                        if(result){
                            tables.remove(position);
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
}
