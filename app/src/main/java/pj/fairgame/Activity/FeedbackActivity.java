package pj.fairgame.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import pj.fairgame.Adapter.FeedbackAdapter;
import pj.fairgame.Adapter.MatchAdapter;
import pj.fairgame.R;
import pj.fairgame.component.RequestInterface;
import pj.fairgame.component.RetrofitClient;
import pj.fairgame.model.Feedback;
import pj.fairgame.model.Match;
import pj.fairgame.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView rv_team1, rv_team2;
    private TextView title;
    private RequestInterface mRESTAPI;
    private FeedbackAdapter feedbackAdapter1, feedbackAdapter2;
    private int matchID, mUserID;
    private Match match;
    private boolean feedbackable;
    private Button btn_give;
    private boolean feedbackSend = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Intent intent = getIntent();
        matchID = intent.getIntExtra("matchID", 0);
        feedbackable = intent.getBooleanExtra("feedbackable", false);

        SharedPreferences pref = getApplication().getSharedPreferences("Auth", MODE_PRIVATE);
        mUserID = pref.getInt("id",0);

        mRESTAPI = RetrofitClient.getInstance().create(RequestInterface.class);
        mToolbar = (Toolbar) findViewById(R.id.feedback_toolbar);
        rv_team1 = (RecyclerView) findViewById(R.id.feedback_rv_team1);
        rv_team2 = (RecyclerView) findViewById(R.id.feedback_rv_team2);
        btn_give = (Button) findViewById(R.id.feedback_btn_give);
        title = (TextView) findViewById(R.id.feedback_tv_title);

        if(!feedbackable)
            title.setText("Match");

        rv_team1.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        rv_team2.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        feedbackAdapter1 = new FeedbackAdapter(feedbackable, 1, mUserID);
        feedbackAdapter2 = new FeedbackAdapter(feedbackable, 2, mUserID);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // perform whatever you want on back arrow click
                if(feedbackSend){
                    setResult(RESULT_OK, null);
                    finish();
                } else{
                    setResult(RESULT_CANCELED, null);
                    finish();
                }

            }
        });

        if(!feedbackable)
            btn_give.setVisibility(View.GONE);

        btn_give.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_give.setEnabled(false);
                feedbackAPI();

            }
        });

        intialList();
    }

    private void intialList(){
        Call<Match> call = mRESTAPI.getMatch(matchID, mUserID);
        call.enqueue(new Callback<Match>() {
            @Override
            public void onResponse(Call<Match> call, Response<Match> response) {
                Log.i("test", "success");
                match = response.body();
                if(match != null){
                    feedbackAdapter1.setmTeam(match.getmTeam());
                    feedbackAdapter2.setmTeam(match.getmTeam());
                    rv_team1.setAdapter(feedbackAdapter1);
                    rv_team2.setAdapter(feedbackAdapter2);

                    feedbackAdapter1.sync(match.getTeamaPlayers());
                    feedbackAdapter2.sync(match.getTeambPlayers());

                }
            }

            @Override
            public void onFailure(Call<Match> call, Throwable t) {
                Log.i("test", "fail");
            }
        });
    }

    private void feedbackAPI(){
        List<User> playerlist = feedbackAdapter1.getItems();
        List<User> playerlist2 = feedbackAdapter2.getItems();
        playerlist.addAll(playerlist2);
        List<Feedback> feedbacks = new ArrayList<>();
        for (int i = 0 ; i<playerlist.size(); i++){
            User player = playerlist.get(i);
            feedbacks.add(new Feedback(matchID, mUserID, player.getId(), player.getFeedbacked()));
        }

        Call<Feedback> call = mRESTAPI.sendFeedback(feedbacks);
        call.enqueue(new Callback<Feedback>() {
            @Override
            public void onResponse(Call<Feedback> call, Response<Feedback> response) {
                Feedback result = response.body();
                if(result.isSuccess()){
                    btn_give.setVisibility(View.GONE);
                    feedbackSend = true;
                } else{
                    btn_give.setEnabled(true);
                    Toast.makeText(getApplicationContext(),"Feedback sending failed!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Feedback> call, Throwable t) {
                btn_give.setEnabled(true);
                Toast.makeText(getApplicationContext(),"Feedback sending failed!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
