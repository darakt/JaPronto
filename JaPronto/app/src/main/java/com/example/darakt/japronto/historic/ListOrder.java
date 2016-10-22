package com.example.darakt.japronto.historic;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.darakt.japronto.adapter.MyAdapterOld;
import com.example.darakt.japronto.R;
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
        adapter = new MyAdapterOld(this, orders);
        setListAdapter(adapter);

        TextView total = (TextView) findViewById(R.id.totalR);
        total.setVisibility(View.GONE);


    }
}
