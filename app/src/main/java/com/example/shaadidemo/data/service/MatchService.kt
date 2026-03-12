package com.example.shaadidemo.data.service

import com.example.shaadidemo.model.ProfilesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MatchService {

    @GET("api/")
    suspend fun getMatches(
        @Query("results") results: Int = 20
    ): Response<ProfilesResponse>
}