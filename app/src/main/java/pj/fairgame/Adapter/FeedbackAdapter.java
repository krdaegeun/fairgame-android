package pj.fairgame.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import pj.fairgame.R;
import pj.fairgame.model.User;

import static java.security.AccessController.getContext;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.viewHolder> {

    private boolean feedbackable;
    private int mTeam;
    private int mUserID;
    private int team;
    private List<User> items;

    public FeedbackAdapter(boolean feedbackable, int team, int mUserID){
        this.feedbackable = feedbackable;
        this.team = team;
        this.mUserID = mUserID;
     }

    public List<User> getItems() {
        return items;
    }

    public void setmTeam(int team){
        this.mTeam = team;
     }
    public void sync(List<User> users){
        this.items = users;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FeedbackAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemview;

        if(feedbackable){
            itemview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.feedbackable_player, viewGroup, false);
        } else{
            itemview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.unfeedbackable_player, viewGroup, false);
        }

        return new viewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackAdapter.viewHolder viewHolder, int i) {
        viewHolder.tv_username.setText(items.get(i).getUsername());


        if(feedbackable){
            if (team == mTeam){
                viewHolder.tv_score.setText("DEF : " + items.get(i).getDefencepts());

                if(items.get(i).getId() == mUserID) {
                    viewHolder.thumbsup.setVisibility(View.INVISIBLE);
                    viewHolder.thumbdown.setVisibility(View.INVISIBLE);
                }

            } else {
                viewHolder.tv_score.setText("ATK : " + items.get(i).getAttackpts());

            }
        } else {
            viewHolder.rb_def.setRating(items.get(i).getDefFloat()/2);
            viewHolder.rb_atk.setRating(items.get(i).getAtkFloat()/2);
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        private TextView tv_username, tv_score;
        private RatingBar rb_atk, rb_def;
        private ImageView thumbsup, thumbdown;
        private boolean thumbsup_clicked = false, thumbsdown_clicked = false;

        public viewHolder(View view){
            super(view);


            if(feedbackable){
                tv_username = (TextView) view.findViewById(R.id.feedbackable_username);
                tv_score = (TextView) view.findViewById(R.id.feedbackable_score);
                thumbsup = (ImageView) view.findViewById(R.id.feedbackable_thumbup);
                thumbdown = (ImageView) view.findViewById(R.id.feedbackable_thumbdown);



                thumbsup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(thumbsup_clicked){
                            thumbsup.setImageResource(R.drawable.ic_thumbup);
                            thumbsup_clicked = false;
                            giveFeedback(0, items.get(getAdapterPosition()));

                        } else {
                            thumbsup.setImageResource(R.drawable.ic_thumbup_selected);
                            thumbdown.setImageResource(R.drawable.ic_thumbdown);
                            thumbsup_clicked = true;
                            thumbsdown_clicked = false;
                            giveFeedback(1, items.get(getAdapterPosition()));
                        }
                    }
                });

                thumbdown.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (thumbsdown_clicked) {
                            thumbdown.setImageResource(R.drawable.ic_thumbdown);
                            thumbsdown_clicked = false;
                            giveFeedback(0, items.get(getAdapterPosition()));

                        } else {
                            thumbsup.setImageResource(R.drawable.ic_thumbup);
                            thumbdown.setImageResource(R.drawable.ic_thumbdown_selected);
                            thumbsup_clicked = false;
                            thumbsdown_clicked = true;
                            giveFeedback(-1, items.get(getAdapterPosition()));
                        }

                    }
                });

            } else {
                tv_username = (TextView) view.findViewById(R.id.unfeed_tv_username);
                rb_atk = (RatingBar) view.findViewById(R.id.unfeed_rb_atk);
                rb_def = (RatingBar) view.findViewById(R.id.unfeed_rb_def);
            }
        }

        private void giveFeedback(int number, User target){
            if(mTeam == team)
                target.setFeedbacked(number);
            else
                target.setFeedbacked(number*2);
        }
    }
}
