package com.quizapp;

import static com.quizapp.Extensions.ISQUESTION;
import static com.quizapp.Extensions.QUIZDATA;
import static com.quizapp.Extensions.QUIZPOSITION;
import static com.quizapp.Extensions.USERNAME;
import static com.quizapp.Extensions.showToast;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.quizapp.databinding.ActivityQuizQuestionBinding;

import java.util.ArrayList;
import java.util.Objects;

public class QuizQuestionActivity extends AppCompatActivity {

    private ActivityQuizQuestionBinding binding;
    private QuizModel quizModel;
    private Integer quizPosition;
    private Boolean isQuestion;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        if (getIntent() != null) {
            userName = getIntent().getStringExtra(USERNAME);
            quizPosition = getIntent().getIntExtra(QUIZPOSITION, 0);
            isQuestion = getIntent().getBooleanExtra(ISQUESTION, false);
            quizModel = new Gson().fromJson(getIntent().getStringExtra(QUIZDATA), QuizModel.class);

            Log.e("quizModel", "init: " + quizModel.toString());
            Log.e("quizPosition", "init: " + quizPosition.toString());
            setData();
            binding.userNameTextView.setText("Welcome " + userName + "!");

            if (isQuestion) {
                binding.mainCardView.setVisibility(View.VISIBLE);
                binding.nextCardView.setVisibility(View.GONE);
            } else {
                binding.mainCardView.setVisibility(View.GONE);
                binding.nextCardView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setData() {
        if (quizModel != null) {
            binding.questionTextview.setText(quizModel.quiz.get(quizPosition).question);
            binding.stepTextView.setText((quizPosition + 1) + "/" + quizModel.quiz.size());
            binding.optionsRecyclerView.setAdapter(new OptionsAdapter(this, quizModel.quiz.get(quizPosition).options, isQuestion));

            binding.progressBar.setProgress(calculateProgress(quizPosition + 1, quizModel.quiz.size()));

            binding.nextCardView.setOnClickListener(v -> {
                if (quizPosition == quizModel.quiz.size() - 1) {
                    Intent intent = new Intent(QuizQuestionActivity.this, QuizResultActivity.class);
                    intent.putExtra(USERNAME, Objects.requireNonNull(userName).toString());
                    intent.putExtra(QUIZDATA, new Gson().toJson(quizModel, QuizModel.class));
                    startActivity(intent);
                } else {
                    int newPosition = quizPosition + 1;
                    Intent intent = new Intent(QuizQuestionActivity.this, QuizQuestionActivity.class);
                    intent.putExtra(USERNAME, Objects.requireNonNull(userName).toString());
                    intent.putExtra(QUIZDATA, new Gson().toJson(quizModel, QuizModel.class));
                    intent.putExtra(QUIZPOSITION, newPosition);
                    intent.putExtra(ISQUESTION, true);
                    startActivity(intent);
                }
            });

            binding.mainCardView.setOnClickListener(v -> {
                ArrayList<Integer> quizIdArray = new ArrayList();

                for (int j = 0; j < quizModel.quiz.get(quizPosition).options.size(); j++) {
                    if (quizModel.quiz.get(quizPosition).options.get(j).isAnswered == 1) {
                        quizIdArray.add(quizModel.quiz.get(quizPosition).options.get(j).isAnswered);
                    }
                }

                if (quizIdArray.isEmpty()) {
                    showToast(QuizQuestionActivity.this, "PLEASE SELECT OPTION FIRST");
                    return;
                }

                Intent intent = new Intent(QuizQuestionActivity.this, QuizQuestionActivity.class);
                intent.putExtra(USERNAME, Objects.requireNonNull(userName).toString());
                intent.putExtra(QUIZDATA, new Gson().toJson(quizModel, QuizModel.class));
                intent.putExtra(QUIZPOSITION, quizPosition);
                intent.putExtra(ISQUESTION, false);
                startActivity(intent);
            });
        }
    }

    // Method to calculate progress based on the size of the ArrayList
    private int calculateProgress(int size, int maxProgress) {
        // Define the maximum progress (e.g., 100)
        // Calculate progress based on the size of the ArrayList
        return (int) (((double) size / maxProgress) * 100);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}