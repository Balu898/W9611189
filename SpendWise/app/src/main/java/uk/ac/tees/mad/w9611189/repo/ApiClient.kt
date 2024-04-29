package uk.ac.tees.mad.w9611189.repo

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("v6/434906a25fbf5f444947a294/pair/{from}/{to}")
    fun getExchangeValue(
        @Path("from") from: String,
        @Path("to") to: String
    ): Call<ConversionResult>
}