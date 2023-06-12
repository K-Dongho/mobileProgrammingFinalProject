package com.my.memorizeapp.viewQuestion;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.my.memorizeapp.DB.DBHelper;
import com.my.memorizeapp.R;

import java.util.ArrayList;
import java.util.Random;

public class ViewNotesActivity extends AppCompatActivity {
    DBHelper dbHelper;
    SQLiteDatabase db;
    ArrayList<String> questionList = new ArrayList<>();
    ArrayList<String> answerList = new ArrayList<>();
    ArrayList<String> dontKnowList = new ArrayList<>();
    ArrayList<String> dontKnowAnswerList = new ArrayList<>();


    Button btnShowAnswer, btnCertain, btnUncertain, btnDontKnow;
    TextView textNote, textAnswer;
    FrameLayout frameAnswer;
    FragmentManager manager = getSupportFragmentManager();
    FragmentTransaction ft = manager.beginTransaction();
    int questionIndex = 0;
    int questionListIndex;


    String folder; //리스트 의 폴더 이름 받아 오는 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notes);
        Intent intent = getIntent();
        folder = intent.getStringExtra("folderName");
        NoteFragment noteFragment = new NoteFragment();
        AnswerFragment answerFragment = new AnswerFragment();
        ft.add(R.id.fragment_note, noteFragment);
        ft.add(R.id.fragment_answer, answerFragment);
        ft.commit();


        dbHelper = DBHelper.getInstance(this, "notes", null, 1);
        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT notes.question, notes.answer FROM folders JOIN notes ON folders._id = notes.folder_id WHERE folders.folder ='" + folder + "';", null);

        if (cursor.moveToFirst()) {
            do {
                String question = cursor.getString(0);
                String answer = cursor.getString(1);
                questionList.add(question);
                answerList.add(answer);
            } while (cursor.moveToNext());
        }


        cursor.close();

        btnShowAnswer = findViewById(R.id.btn_show_answer);
        btnCertain = findViewById(R.id.btn_certain);
        btnUncertain = findViewById(R.id.btn_uncertain);
        btnDontKnow = findViewById(R.id.btn_dont_know);
        frameAnswer = findViewById(R.id.fragment_answer);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                textNote = noteFragment.getView().findViewById(R.id.text_note);
                textAnswer = answerFragment.getView().findViewById(R.id.text_answer);
                textNote.setText(questionList.get(questionIndex).toString());
                textAnswer.setText(answerList.get(questionIndex).toString());
            }
        });


        btnShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnShowAnswer.setVisibility(View.GONE);
                btnCertain.setVisibility(View.VISIBLE);
                btnUncertain.setVisibility(View.VISIBLE);
                btnDontKnow.setVisibility(View.VISIBLE);
                frameAnswer.setVisibility(View.VISIBLE);

            }
        });
        btnCertain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnVisibility();
                if (questionIndex >= questionList.size())
                    if(!dontKnowList.isEmpty())
                        removeDontKnowList();
                setQuestion();

            }
        });
        btnUncertain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnVisibility();
                addDontKnowList();
                addDontKnowList();
                setQuestion();
            }
        });
        btnDontKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnVisibility();
                addDontKnowList();
                addDontKnowList();
                addDontKnowList();
                addDontKnowList();
                setQuestion();
            }
        });

    }

    private void removeDontKnowList(){
        String removeString = dontKnowList.get(questionListIndex);
        for (int i = dontKnowList.size() - 1; i >= 0; i--) { //제거되면서 인덱스가 변경되기 때문에 역순으로 돌면서 제거
            if (removeString.equals(dontKnowList.get(i))) {
                dontKnowList.remove(i);
                dontKnowAnswerList.remove(i);
            }
        }
    }
    private void setQuestion() {
        questionIndex++;
        if (questionIndex >= questionList.size())
            if(dontKnowList.isEmpty())
            {
                showToast("학습이 완료 되었습니다.");
                finish();
            }else{
                RandomQuestion();
            }
        else
            Question();
    }
    private void Question() {
        textNote.setText(questionList.get(questionIndex).toString());
        textAnswer.setText(answerList.get(questionIndex).toString());
    }

    private void RandomQuestion() {
        Random random = new Random();
        questionListIndex = getUniqueQuestionIndex(random);

        textNote.setText(dontKnowList.get(questionListIndex));
        textAnswer.setText(dontKnowAnswerList.get(questionListIndex));
    }

    /**
     * 중복 문제 해결 함수
     */
    private int getUniqueQuestionIndex(Random random) {
        int questionListIndex = -1;
        String previousQuestion = textNote.getText().toString();

        if (allQuestionSame()) {
            questionListIndex = random.nextInt(dontKnowList.size());
        }else{
            questionListIndex = random.nextInt(dontKnowList.size());
            while (dontKnowList.get(questionListIndex).equals(previousQuestion)) {
                questionListIndex = random.nextInt(dontKnowList.size());
            }
        }
        return questionListIndex;
    }
    private boolean allQuestionSame() {
        boolean allQuestionsSame = true;
        int index = 0;
        while (index < dontKnowList.size()) {
            String question = dontKnowList.get(index);
            if (!question.equals(dontKnowList.get(0))) {
                allQuestionsSame = false;
                break;
            }
            index++;
        }
        return allQuestionsSame;
    }


    private void btnVisibility() {
        btnCertain.setVisibility(View.GONE);
        btnUncertain.setVisibility(View.GONE);
        btnDontKnow.setVisibility(View.GONE);
        btnShowAnswer.setVisibility(View.VISIBLE);
        frameAnswer.setVisibility(View.INVISIBLE);
    }

    private void addDontKnowList() {
        if(questionIndex >= questionList.size()){
            dontKnowList.add(dontKnowList.get(questionListIndex));
            dontKnowAnswerList.add(dontKnowAnswerList.get(questionListIndex));
        }else{
            dontKnowList.add(questionList.get(questionIndex));
            dontKnowAnswerList.add(answerList.get(questionIndex));
        }

    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}