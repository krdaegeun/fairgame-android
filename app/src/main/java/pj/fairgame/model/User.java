package pj.fairgame.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User  {


    public static final int TEAM_A = 2;
    public static final int TEAM_B = 1;
    public static final int TEAM_C = 3;
    public static final int No_TEAM = 0;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("team")
    private int team;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("state")
    private int state;
    @SerializedName("atk")
    @Expose
    private float attackpts;

    @SerializedName("def")
    @Expose
    private float defencepts;

    @SerializedName("total")
    @Expose
    private float totalpts;

    @SerializedName("ranking")
    private int ranking;

    private int feedbacked = 0;

    public User(int id, String username, float attackpts, float defencepts, int Team){
        this.id = id;
        this.username = username;
        this.attackpts = attackpts;
        this.defencepts = defencepts;
        this.team = Team;
        this.totalpts += attackpts + defencepts;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setFeedbacked(int feedbacked) {
        this.feedbacked = feedbacked;
    }

    public int getFeedbacked() {
        return feedbacked;
    }

    public int getId() {
        return id;
    }

    public int getTeam(){
        return team;
    }

    public float getAtkFloat() {
        return attackpts;
    }

    public  float getDefFloat(){
        return  defencepts;
    }

    public  float getTotalFloat(){
        return  totalpts;
    }
    public String getUsername(){
        return this.username;
    }

    public String getAttackpts(){
        return String.valueOf(this.attackpts);
    }

    public String getDefencepts(){
        return String.valueOf(this.defencepts);
    }
    public String getTotalpts(){
        return String.valueOf(this.totalpts);
    }

    public int getRanking() {
        return ranking;
    }
}
