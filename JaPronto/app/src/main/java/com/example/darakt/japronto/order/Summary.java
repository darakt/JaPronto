package com.example.darakt.japronto.order;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.darakt.japronto.REST.models.Dish;
import com.example.darakt.japronto.adapter.MyAdapterList;
import com.example.darakt.japronto.R;
import com.example.darakt.japronto.REST.ApiService;
import com.example.darakt.japronto.REST.ApiManager;
import com.example.darakt.japronto.REST.models.Menu;
import com.example.darakt.japronto.REST.models.Order;
import com.example.darakt.japronto.REST.models.Client;
import com.google.android.gms.vision.text.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by darakt on 14/10/16.
 */

public class Summary extends ListActivity {

    MyAdapterList adapterList;
    Order myOrder;
    Client client;
    ApiService apiService;
    Context context;
    private static final String TAG = "Summary";
    private static final int CLEAN = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        context = this;
        Intent intent = getIntent();
        myOrder = (Order) intent.getSerializableExtra("myOrder");
        client = (Client) intent.getSerializableExtra("Client");
        Log.d(TAG, "onClick: wants number " + myOrder.getWanted().getDishes().get(0).getNumber());

        Button yes = (Button) findViewById(R.id.yesF);
        Button no = (Button) findViewById(R.id.noF);
        Button clean = (Button) findViewById(R.id.cleanF);
        TextView total = (TextView) findViewById(R.id.totalS);
        TextView pseudo = (TextView) findViewById(R.id.chefPseudoS);
        TextView description = (TextView) findViewById(R.id.descriptionS);
        ImageView it = (ImageView) findViewById(R.id.chefImageS);
        TextView forThe = (TextView) findViewById(R.id.forThe);

        total.setText("A conta é de "+Integer.toString(myOrder.getTotal())+" R$,  "+"para entregar o "+myOrder.getFor_the_date()+" a "+myOrder.getFor_the_time());
        pseudo.setText(myOrder.getChef().getPseudo());
        Log.d(TAG, "onCreate: "+myOrder.getChef().getImage());
        // // TODO: 22/10/16
        Glide.with(context).load(context.getResources().getString(R.string.url).concat("mulher.png")).into(it);
        description.setText("blabla");

        adapterList = new MyAdapterList(this, myOrder.getWanted().convertToArray());

        Dish tmp = myOrder.getWanted().convertToArray()[0];
        Log.d(TAG, Integer.toString(tmp.getPrice())+"  *   "+Integer.toString(tmp.getNumber())+"   = "+Integer.toString(tmp.getNumber()*tmp.getPrice()));

        setListAdapter(adapterList);



        Log.d(TAG, "onCreate: "+ client.getPseudo()+"    "+ client.getPassword());

         apiService = ApiManager.createService(ApiService.class, client.getPseudo(), client.getPassword());


        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                Call<Order> call = apiService.createOrder(myOrder);
                call.enqueue(new Callback<Order>() {
                    @Override
                    public void onResponse(Call<Order> call, Response<Order> response) {
                        Log.d(TAG, "onResponse: "+response.body());
                        myOrder.clean();
                        Intent toReturn = new Intent();
                        toReturn.putExtra("myOrder", myOrder);
                        Summary.this.setResult(CLEAN, toReturn);
                        finish();
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
                finish();
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
                finish();
            }
        });

    }
}
