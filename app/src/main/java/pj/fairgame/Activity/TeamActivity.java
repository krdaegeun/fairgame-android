package pj.fairgame.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pj.fairgame.Adapter.PlayerAdapter;
import pj.fairgame.R;
import pj.fairgame.component.RequestInterface;
import pj.fairgame.component.RetrofitClient;
import pj.fairgame.model.Match;
import pj.fairgame.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//TODO: Toolbar Arrow color, Option for Teammaking, Play button
public class TeamActivity extends AppCompatActivity {

    private RequestInterface mRESTAPI;
    private RecyclerView rv_left, rv_middle, rv_right;
    private Button btn_play;
    private PlayerAdapter playerAdapter1, playerAdapter2, playerAdapter3;
    private Toolbar mToolbar;
    private List<Integer> playersIDs = new ArrayList<>();
    private boolean making_success = false;
    private List<User> players = new ArrayList<>();
    private int numberOfTeams;
    private boolean add_success = false;
    private String teama = "", teamb = "", teamc = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRESTAPI = RetrofitClient.getInstance().create(RequestInterface.class);

        playerAdapter1 = new PlayerAdapter();
        playerAdapter2 = new PlayerAdapter();
        playerAdapter3 = new PlayerAdapter();
        playersIDs = getIntent().getIntegerArrayListExtra("players");
        numberOfTeams = getIntent().getIntExtra("teamType", 2);

        if(numberOfTeams == 2){
            setContentView(R.layout.activity_team);
            btn_play = (Button) findViewById(R.id.team_btn_play);
            mToolbar = (Toolbar) findViewById(R.id.team_toolbar);
            rv_left = (RecyclerView) findViewById(R.id.team_left_rv);
            rv_right = (RecyclerView) findViewById(R.id.team_right_rv);

            rv_left.setAdapter(playerAdapter1);
            rv_right.setAdapter(playerAdapter2);
        } else {
            setContentView(R.layout.activity_team2);
            btn_play = (Button) findViewById(R.id.team2_btn_play);
            mToolbar = (Toolbar) findViewById(R.id.team2_toolbar);
            rv_left = (RecyclerView) findViewById(R.id.team2_left_rv);
            rv_middle = (RecyclerView) findViewById(R.id.team2_middle_rv);
            rv_right = (RecyclerView) findViewById(R.id.team2_right_rv);

            rv_left.setAdapter(playerAdapter1);
            rv_middle.setAdapter(playerAdapter3);
            rv_right.setAdapter(playerAdapter2);
        }


        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // perform whatever you want on back arrow click
                setResult(RESULT_CANCELED, null);
                finish();
            }
        });

        createTeam();

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchAdd();

            }
        });


    }

    private void matchAdd(){
        btn_play.setEnabled(false);

        if (numberOfTeams == 2){
            matchAddAPI(teama, teamb);
        } else {
            matchAddAPI(teama, teamb);
            matchAddAPI(teama, teamc);
            matchAddAPI(teamb, teamc);
        }

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        if(add_success){
                            Toast.makeText(getApplicationContext(),"This match(es) is added!",Toast.LENGTH_SHORT).show();
                            btn_play.setVisibility(View.GONE);
                        } else{
                            Toast.makeText(getApplicationContext(),"Match adding failed",Toast.LENGTH_SHORT).show();
                            btn_play.setEnabled(true);
                        }

                    }
                }, 2000);

    }

    private void matchAddAPI(String team1, String team2) {
        Match data = new Match(team1, team2);
        Call<Void> call = mRESTAPI.addMatch(data);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                add_success = true;
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

                add_success =false;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        setResult(RESULT_CANCELED, null);
        finish();
    }

    private void createTeam(){

        final ProgressDialog progressDialog = new ProgressDialog(TeamActivity.this, R.style.mProgressDialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Team making...");
        progressDialog.show();
        
        createAPI();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        if(making_success)
                            teamMakingSuccess();
                        else
                            teamMakingFailed();

                        progressDialog.dismiss();
                    }
                }, 2000);
    }

    private void createAPI(){
        Call<List<User>> call = mRESTAPI.createTeam(playersIDs, numberOfTeams);

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                players = response.body();
                int numOfPlayers = players.size();
                for (int i = 0 ; i < numOfPlayers ; i++){
                    User player = players.get(i);
                    switch (player.getTeam()) {
                        case User.TEAM_A:
                            playerAdapter1.addItem(new User(player.getId(), player.getUsername(), player.getAtkFloat(), player.getDefFloat(), player.getTeam()));
                            teama += Integer.toString(player.getId()) + ",";
                            break;
                        case User.TEAM_B:
                            playerAdapter2.addItem(new User(player.getId(), player.getUsername(), player.getAtkFloat(), player.getDefFloat(), player.getTeam()));
                            teamb += Integer.toString(player.getId()) + ",";
                            break;
                        case User.TEAM_C:
                            playerAdapter3.addItem(new User(player.getId(), player.getUsername(), player.getAtkFloat(), player.getDefFloat(), player.getTeam()));
                            teamc += Integer.toString(player.getId()) + ",";
                            break;
                    }}

                making_success = true;
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                making_success = false;
                Toast.makeText(getApplicationContext(),"Server connection failed!",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void teamMakingSuccess(){

    }

    private void teamMakingFailed(){
        setResult(RESULT_CANCELED, null);
        finish();
    }

}
