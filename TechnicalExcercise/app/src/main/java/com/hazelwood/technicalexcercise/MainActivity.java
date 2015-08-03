package com.hazelwood.technicalexcercise;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
public class MainActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int total = 10;
        final Integer[] testArray = {1, 2, 3, 4, 5, 5, 6, 7, 7, 7};
        
        Button enter = (Button)findViewById(R.id.outputButton);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pair(testArray, total);
            }
        });


    }

    private void pair(Integer[] array, int total){
        ArrayList<Integer> check = new ArrayList<>();
        ArrayList<Integer> tmpArray = new ArrayList<>();
        for (int i : array) tmpArray.add(i);

        for (Integer num : tmpArray) {
            int sOut = total - num;
            Log.d("First Output", String.valueOf(sOut) + " " + String.valueOf(num));
            if (!check.contains(sOut)){
                check.add(sOut);
            }
        }
        for (int i = 0; i < check.size(); i++)
            Log.d("Second Output", String.valueOf(check.get(i)) + " " + String.valueOf(total - check.get(i)));

        for (int i = 0; i < check.size(); i++) {
            if (check.contains(total - check.get(i))) check.remove(i);
            Log.d("Third Output", String.valueOf(check.get(i)) + " " + String.valueOf(total - check.get(i)));
        }
    }
}
