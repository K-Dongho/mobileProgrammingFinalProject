package com.my.memorizeapp.addQuestion;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.my.memorizeapp.DB.DBHelper;
import com.my.memorizeapp.R;

import java.util.ArrayList;

public class AddQuestionActivity extends AppCompatActivity {
    private ArrayList<String> folderList = new ArrayList<>();
    DBHelper dbHelper;
    SQLiteDatabase db;
    Spinner spinner;
    EditText addQuestion, addAnswer;
    Button btnAdd, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        spinner = findViewById(R.id.spinnerFolder);
        addQuestion = findViewById(R.id.editQuestion);
        addAnswer = findViewById(R.id.editAnswer);
        btnAdd = findViewById(R.id.addButton);
        btnCancel = findViewById(R.id.cancelButton);


        dbHelper = DBHelper.getInstance(this, "notes", null, 1);
        db = dbHelper.getWritableDatabase();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        spinnerAdapter.addAll(getFolderDataFromDB());
        spinner.setAdapter(spinnerAdapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedFolder = spinner.getSelectedItem().toString();
                String question = addQuestion.getText().toString();
                String answer = addAnswer.getText().toString();

                if(selectedFolder.isEmpty() || question.isEmpty() || answer.isEmpty()){
                    showToast("입력 사항을 전부 입력해 주세요");
                }else{
                    Cursor cursor = db.rawQuery("SELECT _id FROM folders WHERE folder = '" + selectedFolder + "'", null);

                    int folderId = -1;
                    if (cursor.moveToFirst()) {
                        folderId = cursor.getInt(0);
                    }
                    cursor.close();

                    db.execSQL("INSERT INTO notes (folder_id, question, answer) VALUES (" + folderId + ", '" + question + "', '" + answer + "')");
                    showToast("문제가 추가되었습니다.");
                    finish();
                }


            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuestion.setText(null);
                addAnswer.setText(null);
                finish();
            }
        });
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private ArrayList<String> getFolderDataFromDB() {
        Cursor cursor = db.rawQuery("SELECT folder FROM folders", null);
        folderList.clear();
        if (cursor.moveToFirst()) {
            do {
                String folder = cursor.getString(0);
                folderList.add(folder);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return folderList;
    }
}
