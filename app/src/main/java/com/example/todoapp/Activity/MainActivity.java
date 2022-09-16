package com.example.todoapp.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.todoapp.Model.FirebaseModel;
import com.example.todoapp.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_viewTask, btn_addTask, btnDone;
    TextInputLayout nameEt, taskEt;
    FirebaseModel firebaseModel;
    Dialog dialog;
    ImageView btn_image;
    String path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn_addTask = findViewById(R.id.addTask);
        btn_viewTask = findViewById(R.id.viewTask);
        btn_viewTask.setOnClickListener(this);
        btn_addTask.setOnClickListener(this);
        firebaseModel = FirebaseModel.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.viewTask:
                Intent i = new Intent(MainActivity.this, viewTask.class);
                startActivity(i);
                break;
            case R.id.addTask:
                showCustomDialog();
                break;
        }
    }

    void showCustomDialog() {

        dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_todo_dialog);

        nameEt = dialog.findViewById(R.id.name);
        taskEt = dialog.findViewById(R.id.task);
        btn_image = dialog.findViewById(R.id.logo);
        btnDone = dialog.findViewById(R.id.btn_done);

        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // proper follow git hub instructions
                ImagePicker.with(MainActivity.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
//               dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri uri = data.getData();

//            Toast.makeText(getActivity(), uri.toString(), Toast.LENGTH_SHORT).show();
//            firebaseModel.uploadImageAccount(accountFragment,uri.toString(),user.id);
            path = uri.toString();
            System.out.println("path :" + uri);
            btn_image.setImageURI(uri);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            System.out.println("Image Picker Error : " + ImagePicker.getError(data));
            Toast.makeText(MainActivity.this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            System.out.println("Image Picker Cancelled");
            Toast.makeText(MainActivity.this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }


    public void validateData() {

        String name = nameEt.getEditText().getText().toString();
        String task = taskEt.getEditText().getText().toString();

        if (name.length() >= 1 && task.length() >= 1) {
            nameEt.setEnabled(false);
            taskEt.setEnabled(false);
            btnDone.setEnabled(false);
            btnDone.setText("Plz wait...");
//            Toast.makeText(this, "uri "+ uri.toString(), Toast.LENGTH_SHORT).show();
            firebaseModel.addTask(this, name, task, path);
        }
        if (name.equals("")) {
            nameEt.setErrorEnabled(true);
            nameEt.setError("Required");

        } else {
            nameEt.setErrorEnabled(false);
        }
        if (task.equals("")) {
            taskEt.setErrorEnabled(true);
            taskEt.setError("Required");
        } else {
            taskEt.setErrorEnabled(false);
        }
    }


    public void updateUI(boolean isSuccess) {
        if (isSuccess) {
            dialog.dismiss();
            Toast.makeText(this, "Todo Added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
        }
    }
}