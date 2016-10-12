package com.example.darakt.japronto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by darakt on 11/10/16.
 */

public class MenuDisplay extends AppCompatActivity {

    Restaurant myRestaurant;

    @BindView(R.id.test) TextView _textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_menu);
        ButterKnife.bind(this);

        Intent menu = getIntent();
        myRestaurant = (Restaurant)menu.getSerializableExtra("myRestaurant");

        _textView.setText(myRestaurant.getChef().getPseudo());

        Log.d("bla", "onCreate: "+myRestaurant.getChef().getPseudo());
    }

}
