package pj.fairgame.component;


import java.util.List;

import pj.fairgame.model.Auth;
import pj.fairgame.model.Feedback;
import pj.fairgame.model.Match;
import pj.fairgame.model.State;
import pj.fairgame.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RequestInterface {


    /*
     * @FormUrlEncoded : Point out this method will construct a form submit action.
     * @POST : Point out the form submit will use post method, the form action url is the parameter of @POST annotation.
     * @Field("form_field_name") : Indicate the form filed name, the filed value will be assigned to input parameter userNameValue.
     * */

    @POST("/api/auth/verify")
    Call<Auth> verify(@Body Auth data);

    @POST("/api/auth/login")
    Call<Auth> login(@Body Auth data);

    @POST("/api/auth/register")
    Call<Auth> register(@Body Auth data);

    @GET("/api/match/team")
    Call<List<User>> createTeam(@Query("id") List<Integer> ids, @Query("type") int type);

    @POST("/api/match/add")
    Call<Void> addMatch(@Body Match data);

    @GET("/api/match")
    Call<Match> getMatch(@Query("matchID") int matchID, @Query("userID") int userID);

    @GET("/api/match/list")
    Call<List<Match>> getMatches(@Query("id") int userID);

    @POST("/api/match/feedback/set")
    Call<Feedback> sendFeedback(@Body List<Feedback> data);

    @GET("/api/search")
    Call<User> getUser(@Query("id") int id);

    @GET("/api/search/ranking/state")
    Call<List<State>> getStates();

    @GET("/api/search/autocomplete")
    Call<List<User>> searchUser(@Query("q") String query);

    @GET("/api/search/ranking")
    Call<List<User>> getRanking(@Query("state") int stateCode);

    @GET("/api/search/ranking/me")
    Call<User> getMyRanking(@Query("id") int userID, @Query("state") int stateCode);

    @POST("/api/setting/state")
    Call<Void> changeState(@Body User user);

    @POST("/api/setting/username")
    Call<Auth> changeUsername(@Body Auth data);

    @POST("/api/setting/password")
    Call<Auth> changePassword(@Body Auth data);


}
