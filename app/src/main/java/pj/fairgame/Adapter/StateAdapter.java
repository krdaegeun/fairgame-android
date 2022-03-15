package pj.fairgame.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pj.fairgame.R;
import pj.fairgame.model.State;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.viewHolder>  {

    private List<State> items = new ArrayList<>();
    private int checked = -1;
    private int stateCode = 0;

    public void setStateCode(int stateCode){
        this.stateCode = stateCode;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public int getChecked() {
        return checked;
    }

    public List<State> getItems() {
        return items;
    }

    public void sync(List<State> items){
        this.items = items;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.state_item, viewGroup, false);
        return new StateAdapter.viewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {
        if(checked == i || stateCode == items.get(i).getId()){
            if(stateCode == items.get(i).getId())
                checked = i;
            viewHolder.check.setVisibility(View.VISIBLE);
        } else {
            viewHolder.check.setVisibility(View.INVISIBLE);
        }

        viewHolder.name.setText(items.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView check;
        LinearLayout linearLayout;

        public viewHolder(View Itemview){
            super(Itemview);
            name = (TextView) Itemview.findViewById(R.id.state_name);
            check = (ImageView) Itemview.findViewById(R.id.state_check);
            linearLayout = (LinearLayout) Itemview.findViewById(R.id.state_ll);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(checked != getAdapterPosition()){
                        int temp = checked;
                        checked = getAdapterPosition();
                        stateCode = items.get(getAdapterPosition()).getId();
                        notifyItemChanged(checked);
                        notifyItemChanged(temp);
                    } else {
                        int temp = checked;
                        checked = -1;
                        stateCode = 0;
                        notifyItemChanged(temp);
                    }


                }
            });
        }
    }


}
