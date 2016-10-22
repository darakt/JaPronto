package com.example.darakt.japronto.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.darakt.japronto.REST.models.Chef;
import com.example.darakt.japronto.adapter.MyAdapterGrid;
import com.example.darakt.japronto.R;
import com.example.darakt.japronto.REST.models.Dish;
import com.example.darakt.japronto.REST.models.Menu;
import com.example.darakt.japronto.REST.models.Order;
import com.example.darakt.japronto.REST.models.Restaurant;

import butterknife.BindView;

/**
 * Created by darakt on 11/10/16.
 */

public class MenuDisplay extends AppCompatActivity{

    Restaurant myRestaurant;
    GridView grid;
    private static MyAdapterGrid adapter;
    final String TAG = "MenuDisplay";
    Dish[] dishes;
    Order wants;
    Chef chef;
    private static final int AGAIN = 11;
    private static final int WORKED = 8;
    private static final int FAILED = 9;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==AGAIN){
            if ( resultCode == AGAIN) {
                wants = (Order) data.getSerializableExtra("myOrder");
            }
            else
                Log.d(TAG, "onActivityResult: "+wants.getWanted().getDishes().size());
        }
        Toast.makeText(this, "Funcionou, mais comida????", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        Intent menu = getIntent();
        myRestaurant = (Restaurant)menu.getSerializableExtra("myRestaurant");
        wants = (Order)menu.getSerializableExtra("myOrder");
        chef = myRestaurant.getChef();
        Menu m = new Menu(myRestaurant.getMenu());
        dishes = m.convertToArray();
        grid = (GridView) findViewById(R.id.gridview);
        adapter = new MyAdapterGrid(dishes, this);
        grid.setAdapter(adapter);
        ImageView imgChef = (ImageView) findViewById(R.id.chefImage);
        TextView pseudo = (TextView) findViewById(R.id.chefPseudo);
        TextView description = (TextView) findViewById(R.id.description);
        Glide.with(this).load(this.getResources().getString(R.string.url).concat(chef.getImage())).into(imgChef);
        pseudo.setText(chef.getPseudo());
        description.setText(chef.getDescription());

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent next = new Intent(MenuDisplay.this, DishDisplay.class);
                next.putExtra("dish",dishes[position]);
                next.putExtra("myOrder",wants);
                next.putExtra("chef", chef);
                startActivityForResult(next, AGAIN);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent toReturn = new Intent();
        Log.d(TAG, "onClick: "+Integer.toString(wants.getWanted().getDishes().get(0).getNumber()));
        toReturn.putExtra("myOrder", wants);
        setResult(WORKED, toReturn);
        super.onBackPressed();
        finish();
    }
}
