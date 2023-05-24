package com.my.memorizeapp.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.my.memorizeapp.R;
import com.my.memorizeapp.addQuestion.AddQuestionActivity;
import com.my.memorizeapp.viewQuestion.ViewNotesActivity;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fabMain, fabQuestion, fabFolder;
    private boolean isFabButtonsVisible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fabMain = findViewById(R.id.fabMain);
        fabQuestion = findViewById(R.id.fabQuestion);
        fabFolder = findViewById(R.id.fabFolder);
        fabFolder.setSize(FloatingActionButton.SIZE_MINI);
        fabQuestion.setSize(FloatingActionButton.SIZE_MINI);

        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFabButtons();

            }
        });
        fabFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewNotesActivity.class);
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
        // 필요한 만큼의 FAB 버튼들을 보여줍니다.

        isFabButtonsVisible = true;
    }

    private void hideFabButtons() {
        fabQuestion.hide();
        fabFolder.hide();
        // 필요한 만큼의 FAB 버튼들을 감춥니다.

        isFabButtonsVisible = false;
    }
}
