package com.quizapp;

import static com.quizapp.Extensions.ISQUESTION;
import static com.quizapp.Extensions.QUIZDATA;
import static com.quizapp.Extensions.QUIZPOSITION;
import static com.quizapp.Extensions.USERNAME;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.quizapp.databinding.ActivityQuizResultBinding;

import java.util.ArrayList;

public class QuizResultActivity extends AppCompatActivity {

    private ActivityQuizResultBinding binding;
    private QuizModel quizModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        if (getIntent() != null) {
            String userName = getIntent().getStringExtra(USERNAME);
            quizModel = new Gson().fromJson(getIntent().getStringExtra(QUIZDATA), QuizModel.class);

            binding.userNameTextView.setText("Congratulations " + userName + "!");
            setData();

            binding.newQuizCardView.setOnClickListener(v -> {
                Intent intent = new Intent(QuizResultActivity.this, MainActivity.class);
                intent.putExtra(USERNAME, userName.toString());
                startActivity(intent);
            });

            binding.finishCardView.setOnClickListener(v -> {
                finishAffinity();
            });
        }
    }

    public void setData() {
        if (quizModel != null) {
            ArrayList<Option> quiz = new ArrayList<>();
            for (int i = 0; i < quizModel.quiz.size(); i++) {
                for (int j = 0; j < quizModel.quiz.get(i).options.size(); j++) {
                    if (quizModel.quiz.get(i).options.get(j).correct_answer == 1 && quizModel.quiz.get(i).options.get(j).isAnswered == 1) {
                        quiz.add(quizModel.quiz.get(i).options.get(j));
                    }
                }
            }
            binding.scoreTextview.setText(quiz.size() + "/" + quizModel.quiz.size());

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}