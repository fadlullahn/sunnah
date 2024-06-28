package com.example.sunnah.Api;

import com.example.sunnah.Model.Kategori.ResponseModelKategori;
import com.example.sunnah.Model.Sunnah.ResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {
    @GET("sunnah/retrieve.php")
    Call<ResponseModel> ardRetrieveData();

    @FormUrlEncoded
    @POST("sunnah/get.php")
    Call<ResponseModel> ardGetData(
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("sunnah/update.php")
    Call<ResponseModel> ardUpdateData(
            @Field("id") int id,
            @Field("judul") String judul,
            @Field("desk") String desk,
            @Field("kategori") String kategori,
            @Field("sub") String sub,
            @Field("favorit") String favorit

    );

    @GET("kategori/retrieve.php")
    Call<ResponseModelKategori> ardRetrieveDataKategori();

    @FormUrlEncoded
    @POST("kategori/get.php")
    Call<ResponseModelKategori> ardGetDataKategori(
            @Field("id") int id
    );
    @FormUrlEncoded
    @POST("kategori/update.php")
    Call<ResponseModelKategori> ardUpdateDataKategori(
            @Field("id") int id,
            @Field("namakategori") String namakategori
    );
}
