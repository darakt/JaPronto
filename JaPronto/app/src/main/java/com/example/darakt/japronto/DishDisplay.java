package com.example.darakt.japronto;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.darakt.japronto.REST.models.Dish;
import com.example.darakt.japronto.REST.models.Order;
import com.example.darakt.japronto.REST.models.User;
import com.wefika.horizontalpicker.HorizontalPicker;

/**
 * Created by darakt on 13/10/16.
 */

public class DishDisplay extends AppCompatActivity implements HorizontalPicker.OnItemSelected {

    Dish current;
    TextView name, description, note;
    ImageView it;
    HorizontalPicker np;
    final String TAG = "DishDisplay";
    int num = 0;
    Order wants;
    User chef;
    Button yes, no;
    private static final int AGAIN = 11;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);
        Intent past = getIntent();
        current = (Dish) past.getSerializableExtra("dish");
        wants = (Order) past.getSerializableExtra("myOrder");
        chef = (User) past.getSerializableExtra("chef");

        name = (TextView) findViewById(R.id.DishName);
        description = (TextView) findViewById(R.id.DishDescription);
        note = (TextView) findViewById(R.id.Dishavg);
        it = (ImageView) findViewById(R.id.DishImage);
        yes = (Button) findViewById(R.id.yes);
        no = (Button) findViewById(R.id.no);
        np = (HorizontalPicker) findViewById(R.id.picker);
        np.setOnItemSelectedListener(this);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num != 0) {
                    Dish tmp = new Dish(current);
                    tmp.setNumber(num);
                    wants.setId_chef(chef.getId());
                    wants.getWanted().add(tmp);
                    Log.d(TAG, "onClick: "+wants.getWanted().getDishes().size());
                    Intent toReturn = new Intent();
                    toReturn.putExtra("myOrder", wants);
                    setResult(AGAIN, toReturn);
                    finish();
                }else
                    Toast.makeText(DishDisplay.this, "Vc deve pedir mas do que 0 prato ;)", Toast.LENGTH_LONG).show();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toReturn = new Intent();
                toReturn.putExtra("myOrder", wants);
                setResult(AGAIN, toReturn);
                finish();
            }
        });

        name.setText(current.getName());
        description.setText(current.getDescription());
        note.setText("todo");
    }

    @Override
    public void onItemSelected(int index)    {
        num = index;
    }
}
