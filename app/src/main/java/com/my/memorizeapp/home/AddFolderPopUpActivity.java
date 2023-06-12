package com.my.memorizeapp.home;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.my.memorizeapp.DB.DBHelper;
import com.my.memorizeapp.R;

import java.util.ArrayList;

public class AddFolderPopUpActivity extends AppCompatActivity {

    DBHelper dbHelper;
    SQLiteDatabase db;

    EditText editFolderName;
    Button btnAdd, btnCancel;
    ArrayList<String> folderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView(R.layout.activity_add_folder_pop_up);

        editFolderName = findViewById(R.id.editAddFolder);
        btnAdd=findViewById(R.id.buttonFolderAdd);
        btnCancel=findViewById(R.id.buttonFolderCancel);

        dbHelper = DBHelper.getInstance(this, "notes", null, 1);
        db = dbHelper.getWritableDatabase();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String folderText = editFolderName.getText().toString();
                if(checkUniqueFolder(folderText)){
                    db.execSQL("INSERT INTO folders VALUES (null, '"+folderText+"')");
                    showToast("노트가 생성되었습니다.");
                    finish();
                }else{
                    showToast("중복된 노트 이름 입니다.");
                }


            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editFolderName.setText(null);
                finish();
            }
        });

    }

    private void setFolderList() {
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
    private boolean checkUniqueFolder(String newFolderName){
        setFolderList();
        boolean isUnique = true;
        for (int i = 0; i <folderList.size() ; i++) {
            String oldFolderName = folderList.get(i);
            if(oldFolderName.equals(newFolderName)){
                isUnique = false;
                break;
            }
        }
        return isUnique;
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}