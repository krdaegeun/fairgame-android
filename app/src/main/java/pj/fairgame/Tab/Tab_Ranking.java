package pj.fairgame.Tab;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Rating;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;

import org.w3c.dom.Text;

import java.util.List;

import pj.fairgame.Activity.StateSelectActivity;
import pj.fairgame.Adapter.RankingAdapter;
import pj.fairgame.R;
import pj.fairgame.component.RequestInterface;
import pj.fairgame.component.RetrofitClient;
import pj.fairgame.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class Tab_Ranking extends Fragment {

    //TODO: 랭킹에서 나의 순위 db row num 으로 찾기, city bottom sheet 연동 개발
    private static final int REQUEST_STATE = 3;
    private Toolbar mToolbar;
    private RecyclerView recycler_view;
    private RankingAdapter adapter;
    private RequestInterface mRESTAPI;
    private TextView tv_Ranking, tv_Username;
    private RatingBar rb_Rating;
    private LinearLayout bot_ll;
    private int mUserID = 0;
    private int stateCode = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tab__ranking, container, false);

        //REST API
        mRESTAPI = RetrofitClient.getInstance().create(RequestInterface.class);
        tv_Ranking = (TextView) view.findViewById(R.id.tab_r_mRanking);
        tv_Username = (TextView) view.findViewById(R.id.tab_r_mUsername);
        rb_Rating = (RatingBar) view.findViewById(R.id.tab_r_mRating);
        bot_ll = (LinearLayout) view.findViewById(R.id.tab_r_bot_ll);
                    mToolbar = (Toolbar) view.findViewById(R.id.tab_r_toolbar);
                    //Recycler view
                    recycler_view = (RecyclerView) view.findViewById(R.id.recyler_view_ranking);

                    SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("Auth", MODE_PRIVATE);
                    mUserID = pref.getInt("id",0);

                    adapter = new RankingAdapter();
                    recycler_view.setAdapter(adapter);

                    mToolbar.inflateMenu(R.menu.ranking_toolbar_menu);

                    mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if(menuItem.getItemId()==R.id.rk_menu_setting){
                                Intent intent = new Intent(getActivity(), StateSelectActivity.class);
                                intent.putExtra("stateCode", stateCode);
                    startActivityForResult(intent, REQUEST_STATE );

                }

                return false;
            }


        });

        mRankingAPI();
        rankingListAPI();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_STATE){
            if(resultCode == Activity.RESULT_OK){
                stateCode = data.getIntExtra("stateCode", 0);
                mRankingAPI();
                rankingListAPI();
            }


        }
    }

    private void mRankingAPI(){

        if(mUserID!= 0){
            if(bot_ll.getVisibility() == View.GONE)
                bot_ll.setVisibility(View.VISIBLE);

            Call<User> call = mRESTAPI.getMyRanking(mUserID, stateCode);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User user = response.body();

                    if(user.getRanking() == 0)
                        tv_Ranking.setVisibility(View.INVISIBLE);
                     else
                         tv_Ranking.setVisibility(View.VISIBLE);
                    tv_Username.setText(user.getUsername());
                    rb_Rating.setRating(user.getTotalFloat()/2);
                    tv_Ranking.setText(Integer.toString(user.getRanking()));
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    bot_ll.setVisibility(View.GONE);
                    Toast.makeText(getActivity(),"Server Connection fail!",Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void rankingListAPI(){
        Call<List<User>> call = mRESTAPI.getRanking(stateCode);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                Log.i("rankingtab", "3");
                List<User> temp = response.body();
                adapter.setRankingList(temp);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.i("rankingtab", "4");
                Toast.makeText(getActivity(),"Server Connection fail!",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
