package pj.fairgame.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import pj.fairgame.R;
import pj.fairgame.model.Auth;
import pj.fairgame.component.RequestInterface;
import pj.fairgame.component.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private RequestInterface mRESTAPI;
    private boolean loggedIn = false;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //RESTAPI
        mRESTAPI = RetrofitClient.getInstance().create(RequestInterface.class);

        //Get token from shared preferences
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Auth", MODE_PRIVATE);
        token = pref.getString("token",null);

        if(token != null)
            verify();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        navigation();
                    }
                }, 2000);


    }

    private void verify() {
        Auth tokenVerify = new Auth(token);
        Call<Auth> call = mRESTAPI.verify(tokenVerify);
        call.enqueue(new Callback<Auth>() {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response) {
                if(response.body() != null)
                    if(response.body().getSuccess())
                        loggedIn = true;
            }

            @Override
            public void onFailure(Call<Auth> call, Throwable t) {

            }
        });
    }

    private void navigation() {

        //If token is verified
        if (loggedIn){
            Intent mainAct = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(mainAct);
            finish();
        } else {
            Intent loginAct = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(loginAct);
            finish();
        }

        //close activity
        finish();
    }


}
