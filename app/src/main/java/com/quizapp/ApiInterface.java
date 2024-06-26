package com.quizapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("getQuiz")
    Call<QuizModel> getQuizList(@Query("topic") String topic);
}
