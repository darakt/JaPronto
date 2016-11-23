package com.example.darakt.japronto.myOrders;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.darakt.japronto.R;
import com.example.darakt.japronto.REST.ApiManager;
import com.example.darakt.japronto.REST.ApiService;
import com.example.darakt.japronto.REST.models.Chef;
import com.example.darakt.japronto.REST.models.Dish;
import com.example.darakt.japronto.REST.models.Order;
import com.example.darakt.japronto.adapter.OneOrderAdapter;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by darakt on 23/10/16.
 */

public class OneOrder extends AppCompatActivity {
    Order myOrder;
    Chef chef;
    OneOrderAdapter adapter;
    public final static String TAG = "OneOrder";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_order);

        myOrder = (Order) getIntent().getSerializableExtra("order");
        chef = (Chef) getIntent().getSerializableExtra("chef");

        Log.d(TAG, "onCreate: "+myOrder.getState());

        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        if (myOrder.getState() == 3 || (myOrder.getState() == 2 && myOrder.getState_client() == 2) ){
            ll.setVisibility(View.GONE);
            if (myOrder.getState() == 3 )
                getSupportActionBar().setTitle("Ja recusados");
            else
                getSupportActionBar().setTitle("Ja entregado");

        }else if (myOrder.getState() == 2 && myOrder.getState_client() == 0) {
            ll.setVisibility(View.GONE);
            getSupportActionBar().setTitle("Esperando a confirmação do cliente");
        }else{
            Button yes = (Button) findViewById(R.id.yes);
            Button no = (Button) findViewById(R.id.no);

            final ApiService apiService = ApiManager.createService(ApiService.class, chef.getPseudo(), chef.getPassword());

            switch (myOrder.getState()){

                case 0:
                    if (myOrder.getState_client() == 2){
                        yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Call<Order> call = apiService.devileredOrder(Integer.toString(myOrder.getId()));
                                call.enqueue(new Callback<Order>() {
                                    @Override
                                    public void onResponse(Call<Order> call, Response<Order> response) {
                                        Log.d(TAG, "onResponse: ");
                                        Toast.makeText(OneOrder.this, "Pedido entregado", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onFailure(Call<Order> call, Throwable t) {
                                        Log.d(TAG, "onFailure: ");
                                    }
                                });
                                finish();

                            }
                        });
                        no.setVisibility(View.GONE);
                        getSupportActionBar().setTitle("Você ja entregou esse pedido?");
                    }else {
                        getSupportActionBar().setTitle("Você deseja entregar esse pedido?");
                        yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Call<Order> call = apiService.acceptOrder(Integer.toString(myOrder.getId()));
                                call.enqueue(new Callback<Order>() {
                                    @Override
                                    public void onResponse(Call<Order> call, Response<Order> response) {
                                        Log.d(TAG, "onResponse: ");
                                        Toast.makeText(OneOrder.this, "Pedido aceitado", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onFailure(Call<Order> call, Throwable t) {
                                        Log.d(TAG, "onFailure: ");
                                    }
                                });
                                Log.d(TAG, "onClick: before intent");
                                Intent intent = new Intent();
                                intent.putExtra("lastUsed", myOrder.getId_chef());
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        });

                        no.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Call<Order> call = apiService.refuseOrder(Integer.toString(myOrder.getId()));
                                call.enqueue(new Callback<Order>() {
                                    @Override
                                    public void onResponse(Call<Order> call, Response<Order> response) {
                                        Log.d(TAG, "onResponse: ");
                                        Toast.makeText(OneOrder.this, "Pedido refusado", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onFailure(Call<Order> call, Throwable t) {
                                        Log.d(TAG, "onFailure: ");
                                    }
                                });
                                finish();
                            }
                        });
                    }
                    break;

                case 1:
                    getSupportActionBar().setTitle("Você ja entregou esse pedido?");
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Call<Order> call = apiService.devileredOrder(Integer.toString(myOrder.getId()));
                            call.enqueue(new Callback<Order>() {
                                @Override
                                public void onResponse(Call<Order> call, Response<Order> response) {
                                    Log.d(TAG, "onResponse: ");
                                    Toast.makeText(OneOrder.this, "Pedido entregado", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onFailure(Call<Order> call, Throwable t) {
                                    Log.d(TAG, "onFailure: ");
                                }
                            });
                            finish();
                        }
                    });
                    no.setVisibility(View.GONE);
                    break;
            }

        }



        
        try{
            Geocoder geocoder = new Geocoder(this);
            TextView gd = (TextView) findViewById(R.id.gd);
            List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(myOrder.getLat()), Double.parseDouble(myOrder.getLng()), 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getAddressLine(1);
            String country = addresses.get(0).getAddressLine(2);
            int tot = 0;
            for (Dish d: myOrder.getWanted().getDishes()){
                tot += d.getNumber();
                Log.d(TAG, "onCreate: "+d.getName());
            }
            gd.setText(String.format("O pedido é para ser entregada no %s em %s. \nPara o %s a %s \nVocẽ vai ser pagado %s R$ para %s pratos", address, city
                    , myOrder.getFor_the_date(), myOrder.getFor_the_time(), myOrder.getTotal(), tot));
        } catch (IOException e){
            e.printStackTrace();
            Log.d(TAG, "getView: "+e.getMessage());
        }
        ImageButton map = (ImageButton) findViewById(R.id.map);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(String.format("http://maps.google.com/maps?daddr=%s,%s", myOrder.getLat(), myOrder.getLng())));
                startActivity(intent);
            }
        });

        ListView lv = (ListView) findViewById(R.id.listViewOr);
        adapter = new OneOrderAdapter(myOrder.getWanted().getDishes(), this);
        lv.setAdapter(adapter);
    }
}
