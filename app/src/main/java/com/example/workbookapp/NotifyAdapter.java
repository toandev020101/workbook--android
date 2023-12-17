package com.example.workbookapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workbookapp.databinding.ItemNotifyBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.NotifyViewHolder> {
    private ArrayList<Notify> notifies = new ArrayList<>();
    private Context context;
    public NotifyAdapter(Context context, ArrayList<Notify> notifies) {
        this.context = context;
        this.notifies = notifies;
    }

    @NonNull
    @Override
    public NotifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotifyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notify, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotifyViewHolder holder, int position) {
        holder.link(notifies.get(position));
    }

    @Override
    public int getItemCount() {
        return notifies.size();
    }

    class NotifyViewHolder extends RecyclerView.ViewHolder{
        ItemNotifyBinding binding;

        public NotifyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemNotifyBinding.bind(itemView);
        }

        public void link(Notify notify){
            binding.tvTitle.setText(notify.getTitle());
            binding.tvMessage.setText(notify.getMessage());
            binding.tvDuration.setText(getTimeAgo(notify.getCreatedAt()));

            int colorId;
            if(notify.getIsRead() == 0){
                colorId = R.color.blue_50;
            }else {
                colorId = R.color.white;
            }

            binding.llNotifyItem.setBackgroundColor(context.getResources().getColor(colorId));
        }
    }

    public static String getTimeAgo(Date createdDate) {
        long timeDifference = System.currentTimeMillis() - createdDate.getTime();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeDifference);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDifference);
        long hours = TimeUnit.MILLISECONDS.toHours(timeDifference);
        long days = TimeUnit.MILLISECONDS.toDays(timeDifference);

        if (seconds < 60) {
            return "Vừa xong";
        } else if (minutes < 60) {
            return minutes + " phút trước";
        } else if (hours < 24) {
            return hours + " giờ trước";
        } else {
            return days + " ngày trước";
        }
    }
}

