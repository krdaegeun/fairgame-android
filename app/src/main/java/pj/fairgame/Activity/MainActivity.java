package pj.fairgame.Activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;


import pj.fairgame.R;
import pj.fairgame.Tab.Tab_Match;
import pj.fairgame.Tab.Tab_Home;
import pj.fairgame.Tab.Tab_Profile;
import pj.fairgame.Tab.Tab_Ranking;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    final Fragment fragment1 = new Tab_Home();
    final Fragment fragment2 = new Tab_Ranking();
    final Fragment fragment3 = new Tab_Match();
    final Fragment fragment4 = new Tab_Profile();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;

    public static final int REQUEST_FEEDBACK = 20;
    public static final int REQUEST_TEAM = 0;
    public static final int REQUEST_OPTION = 10;
    public static final int REQUEST_STATE = 40;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView =  (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm.beginTransaction().add(R.id.main_container, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.main_container,fragment1, "1").commit();

    }

    @Override
    public void onBackPressed() {
        // disable going back to the root
        moveTaskToBack(true);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.action_home:
                            fm.beginTransaction().hide(active).show(fragment1).commit();
                            active = fragment1;
                            return true;

                        case R.id.action_ranking:
                            fm.beginTransaction().hide(active).show(fragment2).commit();
                            active = fragment2;
                            return true;

                        case R.id.action_match:
                            fm.beginTransaction().hide(active).show(fragment3).commit();
                            active = fragment3;
                            //fm.beginTransaction().detach(active).attach(active).commit();
                            return true;

                        case R.id.action_profile:
                            fm.beginTransaction().hide(active).show(fragment4).commit();
                            active = fragment4;
                            return true;

                    }
                    return false;
                }

        };

}
