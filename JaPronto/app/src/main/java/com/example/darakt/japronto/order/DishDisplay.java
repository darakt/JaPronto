package com.example.darakt.japronto.order;

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

import com.bumptech.glide.Glide;
import com.example.darakt.japronto.R;
import com.example.darakt.japronto.REST.models.Chef;
import com.example.darakt.japronto.REST.models.Dish;
import com.example.darakt.japronto.REST.models.Order;
import com.example.darakt.japronto.REST.models.Client;
import com.wefika.horizontalpicker.HorizontalPicker;

import java.util.ArrayList;
import java.util.List;

import static com.example.darakt.japronto.R.id.picker;
import static com.example.darakt.japronto.R.id.when;

/**
 * Created by darakt on 13/10/16.
 */

public class DishDisplay extends AppCompatActivity implements HorizontalPicker.OnItemSelected {

    Dish current;
    TextView name, description, price;
    ImageView it;
    HorizontalPicker np;
    final String TAG = "DishDisplay";
    int num = 0;
    Order wants;
    Chef chef;
    Button yes, no;
    private static final int AGAIN = 11;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Intent past = getIntent();
        current = (Dish) past.getSerializableExtra("dish");
        wants = (Order) past.getSerializableExtra("myOrder");
        chef = (Chef) past.getSerializableExtra("chef");

        List<String> tmp = new ArrayList<String>();
        for (int i = 0; i<=current.getMax(); i++){
            tmp.add(Integer.toString(i));
        }
        Log.d(TAG, "onCreate: "+current.getMax());
        Log.d(TAG, "onCreate: "+tmp.size());
        final CharSequence[] cs = tmp.toArray(new CharSequence[tmp.size()]);
        Log.d(TAG, "onCreate: "+cs.length);
        HorizontalPicker picker = (HorizontalPicker) findViewById(R.id.picker);
        picker.setValues(cs);
        name = (TextView) findViewById(R.id.DishName);
        description = (TextView) findViewById(R.id.DishDescription);
        price = (TextView) findViewById(R.id.total);
        it = (ImageView) findViewById(R.id.DishImage);
        yes = (Button) findViewById(R.id.yes);
        no = (Button) findViewById(R.id.no);
        np = (HorizontalPicker) findViewById(R.id.picker);
        np.setOnItemSelectedListener(this);

        Glide.with(this).load(this.getResources().getString(R.string.url).concat(current.getImage())).into(it);


        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num != 0) {
                    Dish tmp = new Dish(current);
                    tmp.setNumber(num);
                    wants.setId_chef(chef.getId());
                    wants.getWanted().add(tmp);
                    Log.d(TAG, "onClick: "+Integer.toString(wants.getWanted().getDishes().get(0).getNumber()));
                    wants.setTotal(wants.getTotal()+current.getPrice()*num);
                    wants.setChef(chef);
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
    }

    @Override
    public void onItemSelected(int index)    {
        num = index;
        price.setText(String.format("%s     x     %s    =  %d", current.getPrice(), index, (index*current.getPrice()) ));
    }
}
