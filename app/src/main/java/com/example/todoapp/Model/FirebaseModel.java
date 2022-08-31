package com.example.todoapp.Model;

import static android.content.ContentValues.TAG;
import static com.example.todoapp.Model.Immutable.COLLECTION_USER;
import static com.example.todoapp.Model.Immutable.COLLECTION_USER_CREATED_DATE;
import static com.example.todoapp.Model.Immutable.STORAGE_TODO_IMAGE;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.todoapp.Activity.MainActivity;
import com.example.todoapp.Activity.viewTask;
import com.example.todoapp.Adapter.TodoArrayAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class FirebaseModel {
    // private variables
    private static FirebaseModel single_instance = null;
    private final FirebaseFirestore db;
    private final StorageReference storageRef;
    private final FirebaseStorage firebaseStorage;

    // check  instance
    public FirebaseModel() {
        db = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageRef = firebaseStorage.getReference();
    }

    public static FirebaseModel getInstance() {
        if (single_instance == null)
            single_instance = new FirebaseModel();
        return single_instance;
    }

    public void getTodos(viewTask viewtask, List<Detail> arrayOfTodos) {
        // always use query snap shot for getting multiple document
        // if we want to fetch data another class and show in other class,activity,fragment so we must use query snapshot
        // Document Refrence can do this all but can't pass data or show

        // Query.Direction.DESCENDING // ye nahi likha ha ye lihk ny sy issue arha ha

        // Task<QuerySnapshot> querySnapshotQuery= we can also write this when we are not using snapshot listeners (is ke jaga )  db.collection(COLLECTION_USER)
        Task<QuerySnapshot> task2 = db.collection(COLLECTION_USER)
                .orderBy(COLLECTION_USER_CREATED_DATE, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        arrayOfTodos.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            arrayOfTodos.add(document.toObject(Detail.class));
                        }
                        viewtask.updateUI(true);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    // add task method
    public void addTask(MainActivity mainActivity, String name, String task, String imagePath) {
        // firebase storage (bucket)
        String uploadedPath = STORAGE_TODO_IMAGE + "/" + UUID.randomUUID().toString();

        if (imagePath.equals("")) {
            DocumentReference doc = FirebaseFirestore.getInstance().collection(COLLECTION_USER).document();
            doc.set(new Detail(doc.getId(), name, task, "", "", new Date().getTime()))
                    .addOnCompleteListener(task1 -> mainActivity.updateUI(task1.isSuccessful()));
        } else if (imagePath != "") {
            StorageReference storageReference = storageRef.child(uploadedPath);

            storageReference.putFile(Uri.parse(imagePath))
                    .addOnSuccessListener(
                            taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                // Firestore firebase
                                // imagePath..getPath() will get & save image path where yr image save in phone
                                DocumentReference doc = FirebaseFirestore.getInstance().collection(COLLECTION_USER).document();
                                doc.set(new Detail(doc.getId(), name, task, storageReference.getPath(), uri.toString(), new Date().getTime()))
                                        .addOnCompleteListener(task1 -> mainActivity.updateUI(task1.isSuccessful()));
                            }));


        }

    }

    // delete task
    public void deleteTask(TodoArrayAdapter.CustomDialogClassDeleteTodo customDialogClassDeleteTodo, String Id, Detail detail) {
        db.collection(COLLECTION_USER).document(Id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("task delete ", "successful");
                            customDialogClassDeleteTodo.updateUI(task.isSuccessful());
                            try {
                                storageRef.child(detail.getLogopath()).delete();
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                        } else {
                            Log.d("task status", "not deleted");
                        }
                    }
                });

    }

}
