package pj.fairgame.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import pj.fairgame.R;
import pj.fairgame.component.RequestInterface;
import pj.fairgame.component.RetrofitClient;
import pj.fairgame.model.Auth;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_SIGNUP = 0;
    private RequestInterface mRESTAPI;
    private EditText tx_username, tx_password;
    private TextView link_signup;
    private Button btn_login;
    private boolean login_success = false;
    private String username, password, tokenStr;
    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mRESTAPI = RetrofitClient.getInstance().create(RequestInterface.class);
        tx_username = (EditText) findViewById(R.id.login_et_username);
        tx_password = (EditText) findViewById(R.id.login_et_password);
        btn_login = (Button) findViewById(R.id.login_btn_submit);
        link_signup = (TextView) findViewById(R.id.login_tv_signup);

        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                username  = tx_username.getText().toString();
                password = tx_password.getText().toString();

                if(validate())
                    login();
            }
        });

        //Signup link clicked
        link_signup.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent signupAct = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(signupAct, REQUEST_SIGNUP);
            }
        });
    }

    @Override
    protected void  onActivityResult(int requestCode, int resultCode,Intent data){
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                Intent mainAct = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainAct);
                finish();
            }
        }
    }

    //back button clicked
    @Override
    public void onBackPressed() {
        // disable going back to the root
        moveTaskToBack(true);
    }

    private void login(){
        btn_login.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.mProgressDialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Log in...");
        progressDialog.show();

        loginAPI();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        if(login_success)
                            onLoginSuccess();
                        else
                            onLoginFailed();

                        progressDialog.dismiss();
                    }
                }, 2000);
    }

    private void loginAPI(){
        Auth data = new Auth(username, password);
        Call<Auth> call = mRESTAPI.login(data);
        call.enqueue(new Callback<Auth>() {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response) {
                Auth auth = response.body();

                if(auth.getSuccess()) {
                    login_success = true;
                    tokenStr = auth.getToken();
                    userID = auth.getId();
                }
                else{
                    login_success = false;
                    Toast.makeText(getBaseContext(), "Username or Password is wrong", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Auth> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Server Connection fail!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    // login successful
    private void onLoginSuccess() {
        btn_login.setEnabled(true);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Auth", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("token", tokenStr);
        editor.putInt("id", userID);
        editor.commit();

        Intent mainAct = new Intent(this, MainActivity.class);
        startActivity(mainAct);
        finish();
    }

    private void onLoginFailed() {
        btn_login.setEnabled(true);
    }

    //vaidation for text input
    private boolean validate() {
        boolean valid = true;

        if (username.isEmpty() || username.length() < 5) {
            tx_username.setError("5 characters or more");
            valid = false;
        } else {
            tx_username.setError(null);
        }

        if (password.isEmpty() || password.length() < 8 || password.length() > 16) {
            tx_password.setError("between 8 and 16 characters");
            valid = false;
        } else {
            tx_password.setError(null);
        }

        return valid;
    }


}