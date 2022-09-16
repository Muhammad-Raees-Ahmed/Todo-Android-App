package com.example.todoapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.Adapter.TodoArrayAdapter;
import com.example.todoapp.Model.Detail;
import com.example.todoapp.Model.FirebaseModel;
import com.example.todoapp.R;

import java.util.ArrayList;
import java.util.List;

public class viewTask extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    public List<Detail> todoList;
    FirebaseModel firebaseModel;
    TodoArrayAdapter todoArrayAdapter;
    ImageView btn_back;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        btn_back = findViewById(R.id.back);
        progressBar = findViewById(R.id.progress);
        btn_back.setOnClickListener(this);

        progressBar.setVisibility(View.VISIBLE);
        initializeData();
        todoList.clear();

        firebaseModel = FirebaseModel.getInstance();
        firebaseModel.getTodos(this, todoList);


    }


    private void initializeData() {
        // Initialize RecyclerView and set

        todoList = new ArrayList<>();
        recyclerView = findViewById(R.id.todo_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        todoArrayAdapter = new TodoArrayAdapter(this, todoList);
        recyclerView.setAdapter(todoArrayAdapter);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                Intent i = new Intent(viewTask.this, MainActivity.class);
                startActivity(i);
        }
    }

    public void updateUI(boolean complete) {

        if (complete) {
            progressBar.setVisibility(View.GONE);
            todoArrayAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(viewTask.this, "Network Error", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteTodoRefresh(int position) {
        try {
            todoList.remove(position);
            todoArrayAdapter.notifyItemRemoved(position);
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            todoArrayAdapter.notifyDataSetChanged();
        }
    }
}