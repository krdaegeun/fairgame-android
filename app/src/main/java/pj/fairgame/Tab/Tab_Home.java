package pj.fairgame.Tab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;

import java.util.ArrayList;
import java.util.List;

import pj.fairgame.Activity.DialogActivity;
import pj.fairgame.Activity.MainActivity;
import pj.fairgame.Activity.TeamActivity;
import pj.fairgame.Adapter.CustomSuggestionsAdapter;
import pj.fairgame.Adapter.PlayerAdapter;
import pj.fairgame.R;
import pj.fairgame.component.RequestInterface;
import pj.fairgame.component.RetrofitClient;
import pj.fairgame.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class Tab_Home extends Fragment {


    private RequestInterface mRESTAPI;
    private MaterialSearchBar searchBar;
    private CustomSuggestionsAdapter suggestionsAdapter;
    private RecyclerView recycler_view;
    private PlayerAdapter playerAdapter;
    private Button btn_create;
    private Button btn_option;
    private List<User> suggestion;
    private List<User> players;
    private int matchType = -1;
    private int teamType = 2;
    private int mUserID;

    public Tab_Home(){}

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab__home, container, false);

        mRESTAPI = RetrofitClient.getInstance().create(RequestInterface.class);
        searchBar = (MaterialSearchBar) view.findViewById(R.id.tab_h_search_bar);
        recycler_view = (RecyclerView) view.findViewById(R.id.tab_h_rv);
        btn_create = (Button) view.findViewById(R.id.tab_h_btn_create);
        btn_option = (Button) view.findViewById(R.id.tab_h_btn_option);

        suggestionsAdapter = new CustomSuggestionsAdapter(inflater);
        searchBar.setCustomSuggestionAdapter(suggestionsAdapter);
        playerAdapter = new PlayerAdapter();
        recycler_view.setAdapter(playerAdapter);

        searchBar.setOnSearchActionListener(onSearchActionListener);
        /*
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString();
                autocompleteAPI(query);
            }
        });
        */
        ((CustomSuggestionsAdapter) suggestionsAdapter).setListener(new SuggestionsAdapter.OnItemViewClickListener() {
            @Override
            public void OnItemClickListener(int position, View v) {
                addPlayer(position);
            }

            @Override
            public void OnItemDeleteListener(int position, View v) {

            }
        });

        btn_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogActivity bottomSheetDialog = new DialogActivity();
                Bundle bundle = new Bundle();
                bundle.putInt("matchType", matchType);
                bottomSheetDialog.setArguments(bundle);
                bottomSheetDialog.setTargetFragment(getActivity().getSupportFragmentManager().findFragmentById(R.id.main_container), MainActivity.REQUEST_OPTION);
                bottomSheetDialog.show(getFragmentManager().beginTransaction(), "bottomSheet");


            }
        });

        btn_create.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(validation()){
                    List<Integer> playersIDs = new ArrayList<>();
                    for (int i = 0 ; i< players.size() ; i++){
                        playersIDs.add(players.get(i).getId());
                    }

                    Intent intent = new Intent(getActivity(), TeamActivity.class);
                    intent.putIntegerArrayListExtra("players", (ArrayList<Integer>) playersIDs);
                    intent.putExtra("teamType", teamType);
                    startActivityForResult(intent,MainActivity.REQUEST_TEAM );
                }

            }
        });

        initialList();

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            initialList();
            Log.i("abcd", "11111");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MainActivity.REQUEST_OPTION){
            if(resultCode == Activity.RESULT_OK){
                Bundle bundle = data.getExtras();
                matchType = bundle.getInt("matchType", -1);
            }
        }
    }

    private void addPlayer(int position){
        User player = (User) suggestionsAdapter.getSuggestions().get(position);
        boolean existInList = false;
        for(User user : playerAdapter.getPlayerList()){
            if (player.getId() == user.getId()){
                //If this User exists in added List
                existInList = true;
                Toast.makeText(getActivity(),"Already in your list!",Toast.LENGTH_SHORT).show();
                break;
            }

        }

        //If this User doesn't exist in added List
        if(!existInList){
            playerAdapter.addItem(player);
            Toast.makeText(getActivity(),"Added!",Toast.LENGTH_SHORT).show();
        }
    }

    private void autocompleteAPI(String query){
        searchBar.hideSuggestionsList();
        if(query.length()>= 3 ) {
            Call<List<User>> call = mRESTAPI.searchUser(query);
            call.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                    suggestion = response.body();
                    if(suggestion != null){
                        suggestionsAdapter.setSuggestions(suggestion);
                        searchBar.showSuggestionsList();
                    }

                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    suggestionsAdapter.clearSuggestions();
                    searchBar.showSuggestionsList();

                    Toast.makeText(getActivity(),"Server Connection fail!",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private MaterialSearchBar.OnSearchActionListener onSearchActionListener = new MaterialSearchBar.OnSearchActionListener() {
        @Override
        public void onSearchStateChanged(boolean enabled) {

            if(enabled){
                recycler_view.setAlpha(0);
            } else {
                searchBar.hideSuggestionsList();
                suggestionsAdapter.clearSuggestions();
                recycler_view.setAlpha(1);
            }
        }

        @Override
        public void onSearchConfirmed(CharSequence text) {
            searchBar.hideSuggestionsList();
            Call<List<User>> call = mRESTAPI.searchUser(text.toString());
            call.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    suggestion = response.body();
                    if(suggestion.size()== 0)
                        Toast.makeText(getActivity(),"No User found!",Toast.LENGTH_SHORT).show();
                    else {
                        suggestionsAdapter.setSuggestions(suggestion);
                        searchBar.showSuggestionsList();
                    }

                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {

                    suggestionsAdapter.clearSuggestions();
                    searchBar.showSuggestionsList();

                    Toast.makeText(getActivity(),"Server Connection fail!",Toast.LENGTH_SHORT).show();

                }
            });
        }

        @Override
        public void onButtonClicked(int buttonCode) {
            Toast.makeText(getActivity(),"Button Clicked",Toast.LENGTH_SHORT).show();
            suggestionsAdapter.clearSuggestions();

            searchBar.showSuggestionsList();
        }
    };

    private void initialList(){

            SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("Auth", MODE_PRIVATE);
            mUserID = pref.getInt("id",0);

            if(mUserID != 0) {
                Call<User> call = mRESTAPI.getUser(mUserID);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        User temp = response.body();
                        playerAdapter.addItem(temp);
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(getActivity(), "Server Connection fail!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
    }

    private boolean validation(){
        players = playerAdapter.getPlayerList();
        int numOfPlayers = players.size();
        if(numOfPlayers < 6){
            Toast.makeText(getActivity(), "Please add at least 6 players", Toast.LENGTH_SHORT).show();
            return false;
        }


        switch (matchType){
            case 1:
                teamType = 2;
                break;
            case 2:
                if(numOfPlayers<10){
                    Toast.makeText(getActivity(), "Please add 10 or more players for 5vs5", Toast.LENGTH_SHORT).show();
                    return false;
                }else if(numOfPlayers<15)
                    teamType = 2;
                else if(numOfPlayers>=15 && numOfPlayers<=21)
                    teamType = 3;
                else{
                    Toast.makeText(getActivity(), "Maximal 21 players for 5vs5", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
            case 3:
                if(numOfPlayers<12){
                    Toast.makeText(getActivity(), "Please add 12 or more players for 6vs6", Toast.LENGTH_SHORT).show();
                    return false;
                }else if(numOfPlayers<18)
                    teamType = 2;
                else if(numOfPlayers>=18 && numOfPlayers<=24)
                    teamType = 3;
                else{
                    Toast.makeText(getActivity(), "Maximal 24 players for 6vs6", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;

            case 4:
                if(numOfPlayers<14){
                    Toast.makeText(getActivity(), "Please add 14 or more players for 7vs7", Toast.LENGTH_SHORT).show();
                    return false;
                }else if(numOfPlayers<21)
                    teamType = 2;
                else if(numOfPlayers>=21 && numOfPlayers<=30)
                    teamType = 3;
                else{
                    Toast.makeText(getActivity(), "Maximal 30 players for 7vs7", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
            case 5:
                if(numOfPlayers<16){
                    Toast.makeText(getActivity(), "Please add 16 or more players for 8vs8", Toast.LENGTH_SHORT).show();
                    return false;
                }else if(numOfPlayers<24)
                    teamType = 2;
                else if(numOfPlayers>=24 && numOfPlayers<=33)
                    teamType = 3;
                else{
                    Toast.makeText(getActivity(), "Maximal 33 players for 8vs8", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
            default:
                Toast.makeText(getActivity(), "Please select team type", Toast.LENGTH_SHORT).show();
                return false;
        }

        return true;
    }


}
