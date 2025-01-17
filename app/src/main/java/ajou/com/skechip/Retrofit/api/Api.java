package ajou.com.skechip.Retrofit.api;

import ajou.com.skechip.Retrofit.models.DefaultResponse;
import ajou.com.skechip.Retrofit.models.TimeTableResponse;
import ajou.com.skechip.Retrofit.models.TimeTablesResponse;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @FormUrlEncoded
    @POST("createuser")
    Call<DefaultResponse> createUser(
            @Field("kakaoId") long kakaoId,
            @Field("name") String name,
            @Field("member") int member
    );

    @FormUrlEncoded
    @POST("createfriend")
    Call<DefaultResponse> createFriend(
            @Field("userKakakoId") long userKakakoId,
            @Field("friendKakakoId") long friendKakakoId
    );

    @FormUrlEncoded
    @POST("createtimetable")
    Call<TimeTablesResponse> createTimeTable(
            @Field("kakaoId") long kakaoId,
            @Field("type") Character type,
            @Field("title") String title,
            @Field("place") String place,
            @Field("cellPosition") int cellPosition
    );

    @GET("gettimetables")
    Call<TimeTablesResponse> getTimeTables(
            @Query("kakaoId") long kakaoId
    );

    @FormUrlEncoded
    @PUT("updatetimetable/{kakaoId}")
    Call<TimeTableResponse> updateTimeTable(
            @Path("kakaoId") int kakaoId,
            @Field("type") Character type,
            @Field("title") String title,
            @Field("place") String place,
            @Field("cellPosition") int cellPosition
    );

    @DELETE("deletetimetable/{kakaoId}/{cellPosition}")
    Call<TimeTableResponse> deleteTimeTable(
            @Path("kakaoId") int kakaoId,
            @Path("cellPosition") int cellPosition
    );

}