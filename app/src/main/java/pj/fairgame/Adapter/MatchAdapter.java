package pj.fairgame.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pj.fairgame.R;
import pj.fairgame.model.Match;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.viewHolder> {

    private List<Match> matches = new ArrayList<>();
    private OnItemListener mOnItemListener;

    public void setListener(OnItemListener listener){
        this.mOnItemListener = listener;
    }

    public void sync(List<Match> matches){
        this.matches = matches;
        this.notifyDataSetChanged();
    }

    public List<Match> getMatches(){
        return  matches;
    }

    @NonNull
    @Override
    public MatchAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.match_item_layout, viewGroup, false);
        return new MatchAdapter.viewHolder(itemview, mOnItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchAdapter.viewHolder viewHolder, int i) {
        viewHolder.date.setText(matches.get(i).getDate().substring(0,10));
        viewHolder.players.setText(matches.get(i).getPlayersUsernames());
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView date, players;
        CardView cardView;
        OnItemListener onItemListener;

        public viewHolder (View Itemview, OnItemListener onItemListener){
            super(Itemview);
            date = (TextView) Itemview.findViewById(R.id.date);
            players = (TextView) Itemview.findViewById(R.id.players);
            cardView = (CardView) Itemview.findViewById(R.id.cv_match);
            this.onItemListener = onItemListener;
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemListener.onCardClick(getAdapterPosition());
        }
    }

    public interface  OnItemListener{
        void onCardClick(int position);
    }
}
