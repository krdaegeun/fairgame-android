package pj.fairgame.Tab;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import pj.fairgame.Activity.FeedbackActivity;
import pj.fairgame.Activity.MainActivity;
import pj.fairgame.Adapter.MatchAdapter;
import pj.fairgame.R;
import pj.fairgame.component.RequestInterface;
import pj.fairgame.component.RetrofitClient;
import pj.fairgame.model.Match;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class Tab_Match extends Fragment {


    private RequestInterface mRESTAPI;
    private RecyclerView recyclerView;
    private MatchAdapter matchAdapter;
    private int mUserID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab__match, container, false);
        // Inflate the layout for this fragment

        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("Auth", MODE_PRIVATE);
        mUserID = pref.getInt("id",0);

        mRESTAPI = RetrofitClient.getInstance().create(RequestInterface.class);
        recyclerView = (RecyclerView) view.findViewById(R.id.tab_f_rv);
        matchAdapter = new MatchAdapter();
        matchAdapter.setListener(new MatchAdapter.OnItemListener() {
            @Override
            public void onCardClick(int position) {
                Intent intent = new Intent(getActivity(), FeedbackActivity.class);
                intent.putExtra("matchID", matchAdapter.getMatches().get(position).getId());
                intent.putExtra("feedbackable", matchAdapter.getMatches().get(position).isFeedbackable());
                startActivityForResult(intent, MainActivity.REQUEST_FEEDBACK );
            }
        });

        recyclerView.setAdapter(matchAdapter);
        initialList();

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {

        } else {
            //do when show
            initialList();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MainActivity.REQUEST_FEEDBACK){
            initialList();
            //getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    private void initialList(){

        Call<List<Match>> call = mRESTAPI.getMatches(mUserID);
        call.enqueue(new Callback<List<Match>>() {
            @Override
            public void onResponse(Call<List<Match>> call, Response<List<Match>> response) {
                List<Match> matches = response.body();
                if(matches != null){
                    matchAdapter.sync(matches);
                }
            }

            @Override
            public void onFailure(Call<List<Match>> call, Throwable t) {

            }
        });
    }
}
