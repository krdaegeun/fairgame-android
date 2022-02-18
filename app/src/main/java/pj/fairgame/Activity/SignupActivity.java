package pj.fairgame.Activity;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import pj.fairgame.R;
import pj.fairgame.component.RequestInterface;
import pj.fairgame.component.RetrofitClient;
import pj.fairgame.model.Auth;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity implements  RadioGroup.OnCheckedChangeListener{

    private RequestInterface mRESTAPI;
    private Toolbar mToolbar;
    private EditText tx_username, tx_password;
    private RadioGroup rg_experience, rg_often, rg_position, rg_level;
    private Button btn_create;
    private int radioId1 = 0;
    private int radioId2 = 0;
    private int radioId3 = 0;
    private int radioId4 = 0;
    private String username, password;
    private float atk = 0f;
    private float def = 0f;
    private boolean signup_success = false;
    private String tokenStr;
    private int userID;

    //TODO: Server와 클라이언트 만들기 회원가입 모듈, numberpicker 이용 state 선택

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mRESTAPI = RetrofitClient.getInstance().create(RequestInterface.class);
        mToolbar = (Toolbar) findViewById(R.id.signup_toolbar);
        tx_username = (EditText) findViewById(R.id.signup_et_username);
        tx_password = (EditText) findViewById(R.id.signup_et_password);
        rg_experience = (RadioGroup) findViewById(R.id.signup_rg_exp);
        rg_often = (RadioGroup) findViewById(R.id.signup_rg_often);
        rg_position = (RadioGroup) findViewById(R.id.signup_rg_position);
        rg_level = (RadioGroup) findViewById(R.id.signup_rg_level);
        btn_create = (Button) findViewById(R.id.signup_btn_submit);

        rg_experience.setOnCheckedChangeListener(this);
        rg_often.setOnCheckedChangeListener(this);
        rg_position.setOnCheckedChangeListener(this);
        rg_level.setOnCheckedChangeListener(this);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username  = tx_username.getText().toString();
                password = tx_password.getText().toString();

                if(validate())
                    signup();
            }
        });

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // perform whatever you want on back arrow click
                setResult(RESULT_CANCELED, null);
                finish();
            }
        });

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i){

        if (radioGroup == rg_experience){
            radioId1 = i;
        } else if (radioGroup == rg_often){
            radioId2 = i;
        } else if (radioGroup == rg_position){
            radioId3 = i;
        } else if (radioGroup == rg_level){
            radioId4 = i;
        }
    }

    private void signup() {

        btn_create.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this, R.style.mProgressDialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating account...");
        progressDialog.show();

        calculatePoints();
        signupAPI();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if(signup_success)
                            onSignupSuccess();
                        else
                            onSignupFailed();

                        progressDialog.dismiss();
                    }
                }, 2000);
    }

    private void calculatePoints(){
        if(radioId1 == R.id.signup_rd_exp_1){
            atk += 1.5f;
            def += 1.5f;
        } else if (radioId1 == R.id.signup_rd_exp_2){
            atk += 2.0f;
            def += 2.0f;
        } else if (radioId1 == R.id.signup_rd_exp_3){
            atk += 2.5f;
            def += 2.5f;
        }

        if(radioId2 == R.id.signup_rd_often_1){

        } else if (radioId2 == R.id.signup_rd_often_2){
            atk += 0.3f;
            def += 0.3f;
        } else if (radioId2 == R.id.signup_rd_exp_3){
            atk += 0.5f;
            def += 0.5f;
        }

        if(radioId3 == R.id.signup_rd_position_1)
            def -= 0.3f;
        else if (radioId3 == R.id.signup_rd_position_2)
            atk -= 0.3f;

        if(radioId4 == R.id.signup_rd_level_1){

        } else if (radioId4 == R.id.signup_rd_level_2){
            if(radioId1 == R.id.signup_rd_exp_1){
                atk += 0.2f;
                def += 0.2f;
            } else if (radioId1 == R.id.signup_rd_exp_2){
                atk += 0.3f;
                def += 0.3f;
            } else if (radioId1 == R.id.signup_rd_exp_3){
                atk += 0.4f;
                def += 0.4f;
            }
        } else if (radioId4 == R.id.signup_rd_level_3) {
            if(radioId1 == R.id.signup_rd_exp_1){
                atk += 0.4f;
                def += 0.4f;
            } else if (radioId1 == R.id.signup_rd_exp_2){
                atk += 0.6f;
                def += 0.6f;
            } else if (radioId1 == R.id.signup_rd_exp_3){
                atk += 0.8f;
                def += 0.8f;
            }
        }
    }

    private void signupAPI(){
        Log.i("signuptest", "atk: "+Float.toString(atk));
        Log.i("signuptest", "def: "+Float.toString(def));
        Auth data = new Auth(username, password, atk*2, def*2);
        Call<Auth> call = mRESTAPI.register(data);
        call.enqueue(new Callback<Auth>() {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response) {
                Auth auth = response.body();

                if(auth.getSuccess()) {
                    signup_success = true;
                    tokenStr = auth.getToken();
                    userID = auth.getId();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Username exists already!",Toast.LENGTH_SHORT).show();
                    signup_success = false;
                }

            }

            @Override
            public void onFailure(Call<Auth> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Server Connection fail!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onSignupSuccess() {
        btn_create.setEnabled(true);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Auth", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("token", tokenStr);
        editor.putInt("id", userID);
        editor.commit();

        setResult(RESULT_OK, null);
        finish();
    }

    private void onSignupFailed() {
        btn_create.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        if (username.isEmpty() || username.length() < 5) {
            tx_username.setError("5 characters or more");
            valid = false;
        } else {
            tx_username.setError(null);
        }

        if (password.isEmpty() || password.length() < 8 || password.length() > 16) {
            tx_password.setError("Between 8 and 16 characters");
            valid = false;
        } else {
            tx_password.setError(null);
        }

        if (radioId1 == 0 || radioId2 == 0 || radioId3 == 0 || radioId4 == 0){
            Toast.makeText(getApplicationContext(), "Please select for all questions!",Toast.LENGTH_SHORT).show();
            valid = false;
        }



        return valid;
    }
}
