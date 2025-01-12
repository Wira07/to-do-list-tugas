package com.todolist.to_do_list_tugas

import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Path


interface ApiService {

    @GET("get_tasks.php")
    fun getTasks(): Call<List<Task>>

    @FormUrlEncoded
    @POST("add_task.php")
    fun addTask(
        @Field("title") title: String
    ): Call<Map<String, String>>

    @DELETE("tasks/{id}")
    fun deleteTask(@Path("id") id: String): Call<Map<String, String>>

    @DELETE("tasks/{id}")
    fun deleteTask(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<Map<String, String>>

    @GET("task/{taskId}")
    fun getTaskDetail(@Path("taskId") taskId: String): Call<Task>
}