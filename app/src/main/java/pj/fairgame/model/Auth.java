package pj.fairgame.model;

import com.google.gson.annotations.SerializedName;

public class Auth {

    @SerializedName("success")
    private boolean success;

    @SerializedName("token")
    private String token;

    @SerializedName("id")
    private int id;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("state")
    private int state;

    @SerializedName("atk")
    private float atk;

    @SerializedName("def")
    private float def;

    @SerializedName("newPassword")
    private String newPassword;

    public Auth(String username, String password, float atk, float def){
        this.username = username;
        this.password = password;
        this.atk = atk;
        this.def = def;
    }

    public Auth(String username, String password){
        this.username = username;
        this.password = password;
    }

    public Auth(int id, String username){
        this.id = id;
        this.username = username;
    }

    public Auth(int id, String password, String newPassword){
        this.id = id;
        this.password = password;
        this.newPassword = newPassword;
    }

    public Auth(String token){
        this.token = token;
    }

    public boolean getSuccess(){
        return success;
    }

    public int getId() {
        return id;
    }

    public String getUsername(){
        return username;
    }

    public String getToken(){
        return token;
    }

}

