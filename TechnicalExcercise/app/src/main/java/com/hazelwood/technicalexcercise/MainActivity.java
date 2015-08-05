package com.hazelwood.technicalexcercise;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    int[] arrayOfNumbers;
    int total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        total = 10;
        arrayOfNumbers = new int[100];
        for(int i = 0; i < arrayOfNumbers.length; i++) {
            arrayOfNumbers[i] = (int)(Math.random()*10+1);
        }

        Button button1 = (Button)findViewById(R.id.button1);
        Button button2 = (Button)findViewById(R.id.button2);
        Button button3 = (Button)findViewById(R.id.button3);

        button1.setOnClickListener(findSolution);
        button2.setOnClickListener(findSolution);
        button3.setOnClickListener(findSolution);

    }

    View.OnClickListener findSolution = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();

            switch (id){
                case R.id.button1:
                    solutionOne(arrayOfNumbers, total);
                    break;
                case R.id.button2:
                    solutionTwo(arrayOfNumbers, total);
                    break;
                case R.id.button3:
                    solutionThree(arrayOfNumbers, total);
                    break;
            }
        }
    };

    private void solutionOne(int[] array, int total){
        for(int num : array){
            Log.d("SOLUTION ONE", String.valueOf(num) + " " + String.valueOf(total - num));
        }
    }

    private void solutionTwo(int[] array, int total){
        ArrayList<Integer> check = new ArrayList<>();
        for(int num : array){
            if (!check.contains(num)){
                check.add(num);
                Log.d("SOLUTION TWO", String.valueOf(num) + " " + String.valueOf(total - num));
            }
        }
    }

    private void solutionThree(int[] array, int total){
        ArrayList<Integer> check = new ArrayList<>();
        for(int num : array){
            if (!check.contains(num) && !check.contains(total - num)){
                check.add(num);
                Log.d("SOLUTION TWO", String.valueOf(num) + " " + String.valueOf(total - num));
            }
        }
    }
}
