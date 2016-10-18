package com.example.darakt.japronto;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.darakt.japronto.REST.models.Dish;
import com.example.darakt.japronto.REST.models.Menu;
import com.example.darakt.japronto.REST.models.Order;

/**
 * Created by darakt on 17/10/16.
 */

public class ListOldDish extends ListActivity{
    MyAdapterList adapter;
    Order order;
    private final String TAG = "ListOldDish";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_order);

        order = (Order) getIntent().getSerializableExtra("order");
        Menu tmp = order.getWanted();
        Dish[] tp = tmp.convertToArray();
        int i=0;
        for (Dish d : tmp.getDishes()){
            Log.d(TAG, "Menu: "+d.getName());
            Log.d(TAG, "Array: "+tp[i].getName());
            i=+1;
        }
        adapter = new MyAdapterList(this, order.getWanted().convertToArray());
        setListAdapter(adapter);


    }
}
