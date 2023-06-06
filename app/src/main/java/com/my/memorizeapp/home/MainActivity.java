package com.my.memorizeapp.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.my.memorizeapp.DB.DBHelper;
import com.my.memorizeapp.R;
import com.my.memorizeapp.addQuestion.AddQuestionActivity;
import com.my.memorizeapp.recyclerView.RecyclerViewAdapter;
import com.my.memorizeapp.viewQuestion.ViewNotesActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener {
    DBHelper dbHelper;
    SQLiteDatabase db;
    FloatingActionButton fabMain, fabQuestion, fabFolder;
    RecyclerView recyclerView;
    private ArrayList<String> folderList = new ArrayList<>();
    private ArrayList<String> questionList = new ArrayList<>();
    private boolean isFabButtonsVisible = false;
    RecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fabMain = findViewById(R.id.fabMain);
        fabQuestion = findViewById(R.id.fabQuestion);
        fabFolder = findViewById(R.id.fabFolder);
        fabFolder.setSize(FloatingActionButton.SIZE_MINI);
        fabQuestion.setSize(FloatingActionButton.SIZE_MINI);
        recyclerView = findViewById(R.id.recyclerView);

        dbHelper = DBHelper.getInstance(this, "notes", null, 1);
        db = dbHelper.getReadableDatabase();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(folderList, this);
        adapter.setItemClickListener(this);
        recyclerView.setAdapter(adapter);






        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFabButtons();

            }
        });
        fabFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddFolderPopUpActivity.class);
                startActivity(intent);
            }
        });
        fabQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddQuestionActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateRecyclerView();
    }

    private void updateRecyclerView() {
        Cursor cursor = db.rawQuery("SELECT folder FROM folders", null);
        folderList.clear();
        if (cursor.moveToFirst()) {
            do {
                String folder = cursor.getString(0);
                folderList.add(folder);
            } while (cursor.moveToNext());
        }
        cursor.close();

    }

    private void toggleFabButtons() {
        if (isFabButtonsVisible) {
            hideFabButtons();
            fabMain.setRotation(45);
        } else {
            showFabButtons();
            fabMain.setRotation(0);
        }
    }

    private void showFabButtons() {
        fabQuestion.show();
        fabFolder.show();

        isFabButtonsVisible = true;
    }

    private void hideFabButtons() {
        fabQuestion.hide();
        fabFolder.hide();

        isFabButtonsVisible = false;
    }

    @Override
    public void onItemClick(View view, String folderName) {
        setQuestionList(folderName);
        if(questionList.isEmpty()){
            showToast("저장된 문제가 없습니다.");
        }else {
            Intent intent = new Intent(MainActivity.this, ViewNotesActivity.class);
            intent.putExtra("folderName", folderName);
            startActivity(intent);
        }

    }

    private void setQuestionList(String folderName){
        Cursor cursor = db.rawQuery("SELECT notes.question FROM folders JOIN notes ON folders._id = notes.folder_id WHERE folders.folder ='" + folderName + "';", null);

        questionList.clear();
        if (cursor.moveToFirst()) {
            do {
                String question = cursor.getString(0);
                questionList.add(question);
            } while (cursor.moveToNext());
        }
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
