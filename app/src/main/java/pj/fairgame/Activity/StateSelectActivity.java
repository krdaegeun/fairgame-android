package pj.fairgame.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.List;

import pj.fairgame.Adapter.StateAdapter;
import pj.fairgame.R;
import pj.fairgame.component.RequestInterface;
import pj.fairgame.component.RetrofitClient;
import pj.fairgame.model.State;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StateSelectActivity extends AppCompatActivity {

    private RequestInterface mRESTAPI;
    private RecyclerView recyclerView;
    private StateAdapter stateAdapter;
    private Toolbar mToolbar;
    private int stateCode;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_select);

        mRESTAPI = RetrofitClient.getInstance().create(RequestInterface.class);
        stateAdapter = new StateAdapter();
        recyclerView = findViewById(R.id.state_rv);
        mToolbar = findViewById(R.id.state_toolbar);

        intent = getIntent();
        stateCode = intent.getIntExtra("stateCode", 0);
        stateAdapter.setStateCode(stateCode);
        recyclerView.setAdapter(stateAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // perform whatever you want on back arrow click
                if (stateAdapter.getItems() != null){
                    if(stateAdapter.getChecked() == -1)
                        stateCode = 0;
                    else
                        stateCode = stateAdapter.getItems().get(stateAdapter.getChecked()).getId();
                    intent.putExtra("stateCode", stateCode);
                    setResult(RESULT_OK, intent);
                    finish();
                }   else {

                    setResult(RESULT_CANCELED, intent);
                    finish();
                }


            }
        });

        loadStatesAPI();
    }

    @Override
    public void onBackPressed() {
        if (stateAdapter.getItems() != null){
            if(stateAdapter.getChecked() == -1)
                stateCode = 0;
            else
                stateCode = stateAdapter.getItems().get(stateAdapter.getChecked()).getId();
            intent.putExtra("stateCode", stateCode);
            setResult(RESULT_OK, intent);
            finish();
        }   else {

            setResult(RESULT_CANCELED, intent);
            finish();
        }

    }

    private void loadStatesAPI(){
        Call<List<State>> call = mRESTAPI.getStates();
        call.enqueue(new Callback<List<State>>() {
            @Override
            public void onResponse(Call<List<State>> call, Response<List<State>> response) {
                List<State> states= response.body();
                if(states != null){
                    stateAdapter.sync(states);
                }
            }

            @Override
            public void onFailure(Call<List<State>> call, Throwable t) {
                Log.i("abcd", "Dfdfdf");
                }
            });
    }
}
