package pj.fairgame.Adapter;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pj.fairgame.R;
import pj.fairgame.model.User;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.MyViewHolder> {

    private List<User> PlayerList = new ArrayList<>();

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.player_item_layout, viewGroup, false);
        return new MyViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@android.support.annotation.NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.username.setText(PlayerList.get(i).getUsername());
        myViewHolder.ratingBarAtk.setRating(PlayerList.get(i).getAtkFloat()/2);
        myViewHolder.ratingBarDef.setRating(PlayerList.get(i).getDefFloat()/2);

        switch (PlayerList.get(i).getTeam()) {
            case User.TEAM_A:
                myViewHolder.root_view.setCardBackgroundColor(Color.parseColor("#DD7079"));
                myViewHolder.delete.setVisibility(View.GONE);
                break;
            case User.TEAM_B:
                myViewHolder.root_view.setCardBackgroundColor(Color.parseColor("#99BDD5"));
                myViewHolder.delete.setVisibility(View.GONE);
                break;
            case User.TEAM_C:
                myViewHolder.root_view.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                myViewHolder.delete.setVisibility(View.GONE);
                break;
            case User.No_TEAM:
                myViewHolder.root_view.setCardBackgroundColor(Color.WHITE);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return PlayerList.size();
    }

    public List<User> getPlayerList(){
        return  PlayerList;
    }

    public void addItem(User user){
        PlayerList.add(user);
        this.notifyDataSetChanged();
    }

    public void deleteItem(int index){
        PlayerList.remove(index);
        this.notifyDataSetChanged();
    }

    public void sync(List<User> playerList){
        this.PlayerList = playerList;
        this.notifyDataSetChanged();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder {
        CardView root_view;
        TextView username;
        ImageView delete;
        RatingBar ratingBarAtk, ratingBarDef;

        public MyViewHolder (View itemView) {
            super(itemView);
            root_view = (CardView) itemView.findViewById(R.id.card_view);
            delete = (ImageView) itemView.findViewById(R.id.iv_delete);
            username = (TextView) itemView.findViewById(R.id.username);
            ratingBarAtk = (RatingBar) itemView.findViewById(R.id.atk_ratingbar);
            ratingBarDef = (RatingBar) itemView.findViewById(R.id.def_ratingbar);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(getAdapterPosition());
                }
            });
        }


    }
}
