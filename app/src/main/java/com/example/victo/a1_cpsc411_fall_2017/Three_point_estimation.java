package com.example.victo.a1_cpsc411_fall_2017;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class Three_point_estimation extends AppCompatActivity
implements TextView.OnEditorActionListener {

    private EditText nominalEditText;
    private EditText optimisticEditText;
    private EditText pessimisticEditText;
    private TextView meanTextView;
    private TextView stdDevTextView;

    private float mean;
    private float stdDev;

    //define SharedPreferences object
    private SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_point_estimation);

        nominalEditText = (EditText) findViewById(R.id.nominalEditText);
        optimisticEditText = (EditText) findViewById(R.id.optimisticEditText);
        pessimisticEditText = (EditText) findViewById(R.id.pessimisticEditText);

        meanTextView = (TextView) findViewById(R.id.meanTextView);
        stdDevTextView = (TextView) findViewById(R.id.stdDevTextView);

        nominalEditText.setOnEditorActionListener(this);
        optimisticEditText.setOnEditorActionListener(this);
        pessimisticEditText.setOnEditorActionListener(this);

        //get SharedPreferences object
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }

    public void updateResult(){
        //get nominal estimate
        String nominalString = nominalEditText.getText().toString();
        //get optimistic estimate
        String optimisticString = optimisticEditText.getText().toString();
        //get pessimisticString
        String pessimisticString = pessimisticEditText.getText().toString();

        //setput float variables
        float nominal;
        float optimistic;
        float pessimistic;

        //check nominalString
        if (nominalString.equals("")){
            nominal = 0;
        }
        else{
            nominal = Float.parseFloat(nominalString);
        }

        //check optimisticString
        if (optimisticString.equals("")){
            optimistic = 0;
        }
        else {
            optimistic = Float.parseFloat(optimisticString);
        }

        //check pessimisticString
        if (pessimisticString.equals("")){
            pessimistic = 0;
        }
        else{
            pessimistic = Float.parseFloat(pessimisticString);
        }

        //do the mean and std deviation calculations
        mean = (optimistic + 4 * nominal + pessimistic) / 6;
        stdDev = (pessimistic - optimistic) / 6;

        meanTextView.setText(new DecimalFormat("###.##").format(mean));
        stdDevTextView.setText(new DecimalFormat("###.##").format(stdDev));

    }

    @Override
    public void onPause(){
        //save instance variables
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putFloat("mean", mean);
        editor.putFloat("stdDev", stdDev);
        editor.commit();

        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();

        //get the instance variables
        mean = savedValues.getFloat("mean", 0);
        stdDev = savedValues.getFloat("stdDev", 0);

        updateResult();
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_DONE ||
                i == EditorInfo.IME_ACTION_UNSPECIFIED)
        {
            //tipNoTextView.setText("$10.00");
            //totalNoTextView.setText("$110.00");
            updateResult();
        }

        return false;
    }
}
