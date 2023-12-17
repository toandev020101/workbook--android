package com.example.workbookapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workbookapp.databinding.ItemThemeBinding;

import java.util.ArrayList;

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ThemeViewHolder>{
    private ArrayList<Theme> themes = new ArrayList<>();
    private Context context;
    private int itemChecked = -1;
    public int itemId = 0;

    public ThemeAdapter(Context context, ArrayList<Theme> themes, int itemChecked, int itemId){
        this.context = context;
        this.themes = themes;
        this.itemChecked = itemChecked;
        this.itemId = itemId;
    }

    @NonNull
    @Override
    public ThemeAdapter.ThemeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ThemeAdapter.ThemeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theme, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ThemeAdapter.ThemeViewHolder holder, int position) {
        holder.link(position);
    }

    @Override
    public int getItemCount() {
        return themes.size();
    }

    class ThemeViewHolder extends RecyclerView.ViewHolder{
        ItemThemeBinding binding;

        public ThemeViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemThemeBinding.bind(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Theme theme = themes.get(position);

                        if(itemChecked != -1){
                            setThemeDeActive(binding, themes.get(itemChecked));
                            notifyItemChanged(itemChecked);
                        }

                        if(position != itemChecked){
                            itemChecked = position;
                            setThemeActive(binding, theme);
                        }else {
                            itemChecked = -1;
                        }
                    }

                    if(itemChecked != -1){
                        itemId = themes.get(itemChecked).getId();
                    }
                }
            });
        }

        public void link(int position){
            Theme theme = themes.get(position);
           if(itemChecked != position){
               setThemeDeActive(binding, theme);
           }else {
               setThemeActive(binding, theme);
           }
        }
    }

    private void setThemeDeActive(ItemThemeBinding binding, Theme theme){
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setStroke(context.getResources().getDimensionPixelSize(R.dimen.theme_borderWidth), context.getResources().getColor(R.color.gray_500));
        gradientDrawable.setCornerRadius(context.getResources().getDimensionPixelSize(R.dimen.theme_borderRadius));
        gradientDrawable.setColor(Color.parseColor(theme.getColor()));

        binding.tvTheme.setBackground(gradientDrawable);
    }

    private void setThemeActive(ItemThemeBinding binding, Theme theme){
        // Tạo GradientDrawable cho background
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setStroke(context.getResources().getDimensionPixelSize(R.dimen.theme_borderWidth_active), context.getResources().getColor(R.color.deep_purple_500));
        gradientDrawable.setCornerRadius(context.getResources().getDimensionPixelSize(R.dimen.theme_borderRadius));
        gradientDrawable.setColor(Color.parseColor(theme.getColor()));

        // Tạo Drawable cho dấu tích (ví dụ: sử dụng hình dạng mặc định, bạn có thể thay thế bằng hình dạng tùy chỉnh của bạn)
        Drawable tickDrawable = ContextCompat.getDrawable(context, R.drawable.check_icon);

        // Tạo LayerDrawable và thiết lập inset cho từng layer
        Drawable[] layers = new Drawable[]{gradientDrawable, tickDrawable};
        LayerDrawable layerDrawable = new LayerDrawable(layers);

        // Thiết lập background cho TextView
        binding.tvTheme.setBackground(layerDrawable);
    }
}
