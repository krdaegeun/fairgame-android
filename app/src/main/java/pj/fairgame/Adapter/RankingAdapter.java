package pj.fairgame.Adapter;

import android.media.Rating;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pj.fairgame.R;
import pj.fairgame.model.User;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.viewHolder> {
    private List<User> Userlist = new ArrayList<>();

    @NonNull
    @Override
    public RankingAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ranking_item, viewGroup, false);

        return new RankingAdapter.viewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull RankingAdapter.viewHolder viewHolder, int i) {
        viewHolder.ranking.setText(Integer.toString(i+1));
        viewHolder.username.setText(Userlist.get(i).getUsername());
        viewHolder.total.setRating(Userlist.get(i).getTotalFloat()/2);
    }

    @Override
    public int getItemCount() {
        return Userlist.size();
    }

    public void setRankingList(List<User> list){
        this.Userlist = list;
        this.notifyDataSetChanged();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView ranking, username;
        RatingBar total;

        public viewHolder(View itemView){
            super(itemView);
            ranking = (TextView) itemView.findViewById(R.id.ranking_number);
            username = (TextView) itemView.findViewById(R.id.ranking_username);
            total = (RatingBar) itemView.findViewById(R.id.ranking_rating);
        }

    }
}
