package com.todolist.to_do_list_tugas

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded


interface ApiService {

    @GET("get_tasks.php")
    fun getTasks(): Call<List<Task>>

    @FormUrlEncoded
    @POST("add_task.php")
    fun addTask(
        @Field("title") title: String
    ): Call<Map<String, String>>
}