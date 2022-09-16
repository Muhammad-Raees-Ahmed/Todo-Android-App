package com.example.todoapp.Adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.todoapp.Model.Detail;
import com.example.todoapp.R;


public class TodoViewHolder extends RecyclerView.ViewHolder {

    private static TextView txtName;
    private static TextView txtTask;
    CardView delete_cv,edit_cv;
    static ImageView todoImage;

    public TodoViewHolder(@NonNull View itemView) {
        super(itemView);

        txtName = itemView.findViewById(R.id.name);
        txtTask=itemView.findViewById(R.id.task);
        delete_cv=itemView.findViewById(R.id.delete);
        edit_cv=itemView.findViewById(R.id.edit);
        todoImage=itemView.findViewById(R.id.taskImage);

    }

    public static void setDetails(Detail detail, Activity activity) {
        txtName.setText(detail.getName());
        txtTask.setText(detail.getTask());
        if (detail.getLogourl().equals("")){
            todoImage.setImageResource(R.drawable.ic_icon_placeholder_image);
        }else if (detail.logourl!=""){
            Glide.with(activity).load(detail.getLogourl()).into(todoImage);
//            todoImage.setImageResource(R.drawable.b);

        }
    }
}
