package pj.fairgame.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import pj.fairgame.R;

public class DialogActivity extends BottomSheetDialogFragment implements  RadioGroup.OnCheckedChangeListener{

    private RadioGroup rg1;
    private RadioButton rb1, rb2, rb3,rb4, rb5;
    private TextView tv_apply;
    private int matchType;

    public static DialogActivity getInstance() { return new DialogActivity(); }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dialog, container,false);

        rg1 = (RadioGroup) view.findViewById(R.id.dialog_rg_type);
        rb1 = (RadioButton) view.findViewById(R.id.dialog_rb_type1);
        rb2 = (RadioButton) view.findViewById(R.id.dialog_rb_type2);
        rb3 = (RadioButton) view.findViewById(R.id.dialog_rb_type3);
        rb4 = (RadioButton) view.findViewById(R.id.dialog_rb_type4);
        rb5 = (RadioButton) view.findViewById(R.id.dialog_rb_type5);

        rg1.setOnCheckedChangeListener(this);

        tv_apply = (TextView) view.findViewById(R.id.dialog_apply);

        matchType = getArguments().getInt("matchType", 0);

        initialList();

        tv_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent()
                        .putExtra("matchType", matchType);

                getTargetFragment().onActivityResult(10, Activity.RESULT_OK, intent);
                dismiss();
            }
        });

        return view;
    }

    private void initialList(){
        switch (matchType){
            case 1:
                rb1.setChecked(true);
                break;
            case 2:
                rb2.setChecked(true);
                break;
            case 3:
                rb3.setChecked(true);
                break;
            case 4:
                rb4.setChecked(true);
                break;
            case 5:
                rb5.setChecked(true);
                break;
            default:
                break;

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.dialog_rb_type1:
                matchType = 1;
                break;
            case R.id.dialog_rb_type2:
                matchType = 2;
                break;
            case R.id.dialog_rb_type3:
                matchType = 3;
                break;
            case R.id.dialog_rb_type4:
                matchType = 4;
                break;
            case R.id.dialog_rb_type5:
                matchType = 5;
                break;

        }

    }
}
