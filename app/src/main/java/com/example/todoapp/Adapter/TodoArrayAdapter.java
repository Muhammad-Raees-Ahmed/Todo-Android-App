package com.example.todoapp.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.Activity.MainActivity;
import com.example.todoapp.Activity.viewTask;
import com.example.todoapp.Model.Detail;
import com.example.todoapp.Model.FirebaseModel;
import com.example.todoapp.R;


import java.util.List;


public class TodoArrayAdapter extends RecyclerView.Adapter<TodoViewHolder> {

    private List<Detail> arrayOfTodos;
    private Context context;
    Activity activity;
    private Detail detail;


    public TodoArrayAdapter(Activity activity, List<Detail> arrayOfTodos) {
        this.activity = activity;
        this.arrayOfTodos = arrayOfTodos;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.items, parent, false);

        return new TodoViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, @SuppressLint("RecyclerView") int position) {
        detail = arrayOfTodos.get(position);
        holder.setDetails(detail, activity);
        holder.delete_cv.setOnClickListener(view -> {
            CustomDialogClassDeleteTodo cdd = new CustomDialogClassDeleteTodo(activity, detail, position);
            cdd.show();
            Window window = cdd.getWindow();

            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels - 130;

            window.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        });
    }

    @Override
    public int getItemCount() {
        return arrayOfTodos.size();
    }

    public static class CustomDialogClassDeleteTodo extends Dialog {

        private final Detail detail;
        Activity a;
        int position;
        FirebaseModel firebaseModel;

        public CustomDialogClassDeleteTodo(Activity a, Detail detail, int position) {
            super(a);
            this.a = a;
            this.detail = detail;
            this.position = position;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.delete_dialog);

            firebaseModel = FirebaseModel.getInstance();
            // raees did
            Button btn_delete = findViewById(R.id.delete_item);
            Button btn_cancel = findViewById(R.id.cancel_delete);

            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btn_delete.setEnabled(false);
                    btn_delete.setClickable(false);
                    try {
                        firebaseModel.deleteTask(CustomDialogClassDeleteTodo.this, detail.getId(), detail);
                    } catch (Exception e) {
                        Toast.makeText(a, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

        }

        public void updateUI(boolean isSuccess) {
            if (isSuccess) {
                Toast.makeText(a, "Todo deleted successfully", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(a, MainActivity.class);
                a.startActivity(i);
                ((viewTask) a).deleteTodoRefresh(position);
            }
            dismiss();
        }
    }
}
