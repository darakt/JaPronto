package com.example.darakt.japronto;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.darakt.japronto.REST.ApiService;
import com.example.darakt.japronto.REST.models.Menu;
import com.example.darakt.japronto.REST.models.Order;
import com.example.darakt.japronto.REST.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by darakt on 14/10/16.
 */

public class Summary extends ListActivity {

    MyAdapterList adapterList;
    Order myOrder;
    User user;
    ApiService apiService;
    private static final String TAG = "Summary";
    private static final int CLEAN = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        Intent intent = getIntent();
        myOrder = (Order) intent.getSerializableExtra("myOrder");
        user = (User) intent.getSerializableExtra("User");

        adapterList = new MyAdapterList(this, myOrder.getWanted().convertToArray());
        setListAdapter(adapterList);

        Button yes = (Button) findViewById(R.id.yesF);
        Button no = (Button) findViewById(R.id.noF);
        Button clean = (Button) findViewById(R.id.cleanF);

        Log.d(TAG, "onCreate: "+user.getPseudo()+"    "+user.getPassword());

         apiService = ApiManager.createService(ApiService.class, user.getPseudo(), user.getPassword());


        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                Call<Order> call = apiService.createOrder(myOrder);
                call.enqueue(new Callback<Order>() {
                    @Override
                    public void onResponse(Call<Order> call, Response<Order> response) {
                        Log.d(TAG, "onResponse: "+response.body());
                    }

                    @Override
                    public void onFailure(Call<Order> call, Throwable t) {
                        Log.d(TAG, "onFailure: "+t.getMessage()+"     "+t.getLocalizedMessage()+"  "+t.getStackTrace());
                    }
                });
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myOrder.setId_chef(0);
                myOrder.setWanted(new Menu());

                Intent toReturn = new Intent();
                toReturn.putExtra("myOrder", myOrder);
                setResult(CLEAN, toReturn);
            }
        });

        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myOrder.setId_chef(0);
                myOrder.setWanted(new Menu());

                Intent toReturn = new Intent();
                toReturn.putExtra("myOrder", myOrder);
                setResult(CLEAN, toReturn);
            }
        });

    }
}
