package com.example.darakt.japronto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.darakt.japronto.REST.models.Dish;
import com.example.darakt.japronto.REST.models.Menu;
import com.example.darakt.japronto.REST.models.Order;

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
    private static final int AGAIN = 11;
    private static final int WORKED = 8;
    private static final int FAILED = 9;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==AGAIN){
            if ( resultCode == AGAIN)
                wants = (Order) data.getSerializableExtra("myOrder");
            else
                Log.d(TAG, "onActivityResult: "+wants.getWanted().getDishes().size());
        }
        Toast.makeText(this, "Funcionou, mais comida????", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_menu);

        Intent menu = getIntent();
        myRestaurant = (Restaurant)menu.getSerializableExtra("myRestaurant");
        wants = (Order)menu.getSerializableExtra("myOrder");
        Menu m = new Menu(myRestaurant.getMenu());
        dishes = m.convertToArray();
        grid = (GridView) findViewById(R.id.gridview);
        adapter = new MyAdapterGrid(dishes, this);
        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent next = new Intent(MenuDisplay.this, DishDisplay.class);
                next.putExtra("dish",dishes[position]);
                next.putExtra("myOrder",wants);
                next.putExtra("chef", myRestaurant.getChef());
                startActivityForResult(next, AGAIN);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent toReturn = new Intent();
        toReturn.putExtra("myOrder", wants);
        setResult(WORKED, toReturn);
        super.onBackPressed();
        finish();
    }
}
