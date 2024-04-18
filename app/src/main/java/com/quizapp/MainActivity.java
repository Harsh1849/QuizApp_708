package com.quizapp;

import static com.quizapp.Extensions.ISQUESTION;
import static com.quizapp.Extensions.QUIZDATA;
import static com.quizapp.Extensions.QUIZPOSITION;
import static com.quizapp.Extensions.USERNAME;
import static com.quizapp.Extensions.hideKeyboard;
import static com.quizapp.Extensions.hideProgress;
import static com.quizapp.Extensions.isConnect;
import static com.quizapp.Extensions.showProgress;
import static com.quizapp.Extensions.showToast;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.quizapp.databinding.ActivityMainBinding;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent() != null) {
            if (getIntent().hasExtra(USERNAME)) {
                binding.nameEditText.setText(getIntent().getStringExtra(USERNAME));
            }
        }
        binding.startButton.setOnClickListener(v -> {
            hideKeyboard(this, binding.getRoot());
            if (binding.nameEditText.getText().toString().trim().isEmpty()) {
                binding.nameTextLayout.setErrorEnabled(true);
                binding.nameTextLayout.setError(getString(R.string.please_enter_your_name));
            } else {
                getQuizList();
            }
        });
    }

    private void getQuizList() {
        if (isConnect(MainActivity.this)) {
            showProgress(MainActivity.this);
            Call<QuizModel> call = RetrofitClient.getInstance().getMyApi().getQuizList("Android%20Development");
            call.enqueue(new Callback<QuizModel>() {
                @Override
                public void onResponse(@NonNull Call<QuizModel> call, @NonNull Response<QuizModel> response) {
                    hideProgress();
                    if (response.code() == 200) {
                        Intent intent = new Intent(MainActivity.this, QuizQuestionActivity.class);
                        intent.putExtra(USERNAME, Objects.requireNonNull(binding.nameEditText.getText()).toString());
                        intent.putExtra(QUIZDATA, new Gson().toJson(response.body(), QuizModel.class));
                        intent.putExtra(QUIZPOSITION, 0);
                        intent.putExtra(ISQUESTION, true);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<QuizModel> call, @NonNull Throwable t) {
                    hideProgress();
                    showToast(MainActivity.this, t.getMessage());
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}