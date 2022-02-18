package pj.fairgame.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;

import java.util.List;

import pj.fairgame.R;
import pj.fairgame.model.User;

public class CustomSuggestionsAdapter extends SuggestionsAdapter<User, CustomSuggestionsAdapter.SuggestionHolder> {

    private SuggestionsAdapter.OnItemViewClickListener listener;
    private int maxSuggestions = 20;

    public CustomSuggestionsAdapter(LayoutInflater inflater) {
        super(inflater);
    }

    public void setListener(SuggestionsAdapter.OnItemViewClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public SuggestionHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.suggestion_layout, viewGroup, false);
        return new SuggestionHolder(view);
    }
    
    @Override
    public void onBindSuggestionHolder(User suggestion, SuggestionHolder holder, int position) {
        holder.username.setText(suggestion.getUsername());
    }

    @Override
    public void setSuggestions(List<User> suggestions) {
        if(suggestions.size()>maxSuggestions)
            super.setSuggestions(suggestions.subList(0,maxSuggestions));
        else
            super.setSuggestions(suggestions);
    }

//    public interface OnItemViewClickListener {
//        void OnItemClickListener(int position, View v);
//
//        void OnItemDeleteListener(int position, View v);
//    }

    @Override
    public int getSingleViewHeight() {
        return 600;
    }

    class SuggestionHolder extends  RecyclerView.ViewHolder{
        private final TextView username;

        public SuggestionHolder (@NonNull View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.txt_suggest_keyword);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //v.setTag(getSuggestions().get(getAdapterPosition()).getUsername());
                    listener.OnItemClickListener(getAdapterPosition(), v);
                }
            });

        }


    }




}



