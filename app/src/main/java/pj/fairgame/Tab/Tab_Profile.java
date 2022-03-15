package pj.fairgame.Tab;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import pj.fairgame.Activity.LoginActivity;
import pj.fairgame.Activity.MainActivity;
import pj.fairgame.Activity.StateSelectActivity;
import pj.fairgame.R;
import pj.fairgame.component.RequestInterface;
import pj.fairgame.component.RetrofitClient;
import pj.fairgame.model.Auth;
import pj.fairgame.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class Tab_Profile extends Fragment {


    private TextView username;
    private RatingBar rb_atk, rb_def;
    private RequestInterface mRESTAPI;
    private Toolbar mToolbar;
    private User user;
    private int mUserID;
    private String newUsername;


    public Tab_Profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab__profile, container, false);

        //RESTAPI
        mRESTAPI = RetrofitClient.getInstance().create(RequestInterface.class);

        //TextViews
        username = (TextView) view.findViewById(R.id.tab_pf_tv_username);
        rb_atk = (RatingBar) view.findViewById(R.id.tab_pf_rb_atk);
        rb_def = (RatingBar) view.findViewById(R.id.tab_pf_rb_def);
        mToolbar = (Toolbar) view.findViewById(R.id.tab_pf_toolbar);

        mToolbar.inflateMenu(R.menu.profile_toolbr_menu);

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.pf_menu_logout)
                {
                    SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("Auth", MODE_PRIVATE);
                    pref.edit().remove("token").commit();
                    pref.edit().remove("id").commit();

                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();

                }
                else if(menuItem.getItemId() == R.id.pf_menu_change_state){
                    Intent intent = new Intent(getActivity(), StateSelectActivity.class);
                    intent.putExtra("stateCode", user.getState());
                    startActivityForResult(intent, MainActivity.REQUEST_STATE );
                    // do something

                }
                else if(menuItem.getItemId()== R.id.pf_menu_change_username)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Change");
                    View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.alert_dialog_username, (ViewGroup) getView(), false);

                    final EditText input = (EditText) viewInflated.findViewById(R.id.input_username);

                    builder.setView(viewInflated);
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            newUsername = input.getText().toString();
                            Auth auth = new Auth(user.getId(), newUsername);
                            Call<Auth> call = mRESTAPI.changeUsername(auth);
                            call.enqueue(new Callback<Auth>() {
                                @Override
                                public void onResponse(Call<Auth> call, Response<Auth> response) {
                                    Auth temp = response.body();
                                    if(temp.getSuccess()){
                                        username.setText(newUsername);
                                        Toast.makeText(getActivity(),"username is changed",Toast.LENGTH_SHORT).show();
                                    } else{
                                        Toast.makeText(getActivity(),"this username exist already",Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onFailure(Call<Auth> call, Throwable t) {

                                }
                            });
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Change");
                    View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.alert_dialog_password, (ViewGroup) getView(), false);

                    final EditText inputPW = (EditText) viewInflated.findViewById(R.id.input_password);
                    final EditText inputNPW = (EditText) viewInflated.findViewById(R.id.input_new_password);

                    builder.setView(viewInflated);
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            String password = inputPW.getText().toString();
                            String newpassword = inputNPW.getText().toString();

                            Auth auth = new Auth(15, password, newpassword);
                            Call<Auth> call = mRESTAPI.changePassword(auth);
                            call.enqueue(new Callback<Auth>() {
                                @Override
                                public void onResponse(Call<Auth> call, Response<Auth> response) {
                                    Auth temp = response.body();
                                    if(temp.getSuccess()){
                                        Toast.makeText(getActivity(),"Password is changed",Toast.LENGTH_SHORT).show();
                                    } else{
                                        Toast.makeText(getActivity(),"Password is wrong",Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onFailure(Call<Auth> call, Throwable t) {

                                }
                            });
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }

                return false;
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == MainActivity.REQUEST_STATE){
            if(resultCode == Activity.RESULT_OK){
                int stateCode = data.getIntExtra("stateCode", 0);
                user.setState(stateCode);
                Call<Void> call = mRESTAPI.changeState(user);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                    Toast.makeText(getActivity(),"State is changed!",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getActivity(),"State changing failed!",Toast.LENGTH_SHORT).show();
                    }
                });
            }


        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("Auth", MODE_PRIVATE);
        mUserID = pref.getInt("id",0);

        Call<User> call = mRESTAPI.getUser(mUserID);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                user = response.body();

                if(user != null){
                    username.setText(user.getUsername());
                    rb_atk.setRating(user.getAtkFloat()/2);
                    rb_def.setRating(user.getDefFloat()/2);

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getActivity(),"Server Connection fail!",Toast.LENGTH_SHORT).show();
            }
        });


    }

}
