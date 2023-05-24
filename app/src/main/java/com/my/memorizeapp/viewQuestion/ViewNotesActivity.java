package com.my.memorizeapp.viewQuestion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.my.memorizeapp.R;

import java.util.ArrayList;
import java.util.Random;

public class ViewNotesActivity extends AppCompatActivity {
    ArrayList<String> questionList = new ArrayList<>();
    ArrayList<String> dontKnowList = new ArrayList<>();

    Button btnShowAnswer, btnCertain, btnUncertain, btnDontKnow;
    TextView textNote;
    FrameLayout frameAnswer;
    FragmentManager manager = getSupportFragmentManager();
    FragmentTransaction ft = manager.beginTransaction();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notes);

        btnShowAnswer = findViewById(R.id.btn_show_answer);
        btnCertain = findViewById(R.id.btn_certain);
        btnUncertain = findViewById(R.id.btn_uncertain);
        btnDontKnow = findViewById(R.id.btn_dont_know);
        frameAnswer = findViewById(R.id.fragment_answer);

        NoteFragment noteFragment = new NoteFragment();
        AnswerFragment answerFragment = new AnswerFragment();
        ft.add(R.id.fragment_note, noteFragment);
        ft.add(R.id.fragment_answer, answerFragment);
        ft.commit();


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
                btnCertain.setVisibility(View.GONE);
                btnUncertain.setVisibility(View.GONE);
                btnDontKnow.setVisibility(View.GONE);
                btnShowAnswer.setVisibility(View.VISIBLE);
                textNote = noteFragment.getView().findViewById(R.id.text_note);
                frameAnswer.setVisibility(View.INVISIBLE);

            }
        });
        btnUncertain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCertain.setVisibility(View.GONE);
                btnUncertain.setVisibility(View.GONE);
                btnDontKnow.setVisibility(View.GONE);
                btnShowAnswer.setVisibility(View.VISIBLE);
                frameAnswer.setVisibility(View.INVISIBLE);



            }
        });
        btnDontKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCertain.setVisibility(View.GONE);
                btnUncertain.setVisibility(View.GONE);
                btnDontKnow.setVisibility(View.GONE);
                btnShowAnswer.setVisibility(View.VISIBLE);
                frameAnswer.setVisibility(View.INVISIBLE);

            }
        });

    }
        public String getRandomQuestion() {
            Random random = new Random();
            if (!dontKnowList.isEmpty() && random.nextInt(10) < 7) {
                return dontKnowList.get(random.nextInt(dontKnowList.size()));
            } else {
                return questionList.get(random.nextInt(questionList.size()));
            }
        }
}