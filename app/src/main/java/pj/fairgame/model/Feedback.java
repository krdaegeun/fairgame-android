package pj.fairgame.model;

import com.google.gson.annotations.SerializedName;

public class Feedback {

    @SerializedName("matchID")
    private int matchID;

    @SerializedName("giverID")
    private int giverID;

    @SerializedName("targetID")
    private int targetID;

    @SerializedName("value")
    private int value;

    @SerializedName("success")
    private boolean success;

    public Feedback(int matchID, int giverID, int targetID, int value){
        this.matchID = matchID;
        this.giverID = giverID;
        this.targetID = targetID;
        this.value = value;
    }

    public boolean isSuccess() {
        return success;
    }
}
