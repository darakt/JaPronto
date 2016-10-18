package com.example.darakt.japronto;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.example.darakt.japronto.REST.models.Dish;
import com.example.darakt.japronto.REST.models.Menu;
import com.example.darakt.japronto.REST.models.Order;

/**
 * Created by darakt on 17/10/16.
 */

public class ListOrder extends ListActivity {

    MyAdapterOld adapter;
    Order[] orders;
    private final String TAG = "ListOrder";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_order);
        orders = (Order[]) getIntent().getSerializableExtra("orders");
        Log.d(TAG, "OnCreate: "+orders[0].getCustomer_pseudo() );
        adapter = new MyAdapterOld(this, orders);
        setListAdapter(adapter);


    }
}
