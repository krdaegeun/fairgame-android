package pj.fairgame.model;

import android.widget.LinearLayout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Match {

    @SerializedName("id")
    private int id;

    @SerializedName("myteam")
    @Expose
    private int mTeam;

    @SerializedName("teama")
    @Expose
    private ArrayList<User> teamaPlayers = new ArrayList<>();

    @SerializedName("teamb")
    @Expose
    private ArrayList<User> teambPlayers = new ArrayList<>();

    @SerializedName("team_a")
    private String teama;

    @SerializedName("team_b")
    private String teamb;



    @SerializedName("goals")
    private String goals;

    @SerializedName("result")
    private String result;

    @SerializedName("date")
    private String date;

    @SerializedName("players")
    private String playersUsernames;

    @SerializedName("feedbackable")
    private boolean feedbackable;

    public Match (String teama, String teamb){
        this.teama = teama;
        this.teamb = teamb;
    }

    public boolean isFeedbackable(){
        return feedbackable;
    }

    public String getDate() {
        return date;
    }

    public String getPlayersUsernames() {
        return playersUsernames;
    }

    public int getId() {
        return id;
    }

    public List<User> getTeamaPlayers() {
        return teamaPlayers;
    }

    public int getmTeam() {
        return mTeam;
    }

    public List<User> getTeambPlayers() {
        return teambPlayers;
    }
}
