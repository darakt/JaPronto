package com.example.darakt.japrontochef.list;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.darakt.japrontochef.AddDish;
import com.example.darakt.japrontochef.LoginActivity;
import com.example.darakt.japrontochef.MenuDisplay;
import com.example.darakt.japrontochef.R;
import com.example.darakt.japrontochef.REST.ApiManager;
import com.example.darakt.japrontochef.REST.ApiService;
import com.example.darakt.japrontochef.REST.models.Chef;
import com.example.darakt.japrontochef.REST.models.Order;
import com.example.darakt.japrontochef.adapter.OrderAdapter;
import com.github.clans.fab.FloatingActionButton;

import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by darakt on 25/10/16.
 */

public class MyListOrdersCurrent extends ListActivity{

    private final int REQUEST_LOGIN = 1;
    ApiService apiservice = null;
    private final String TAG = "MyListOrdersPending";
    Chef chef = new Chef();
    ListView list;
    List<Order> current;
    int lastUsed;
    OrderAdapter adapter;
    private final int MODIFICATION = 10;
    private final int DISPONIBLE = 15;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_orders);

        setTitle("Pedidos esperando resposta");

        FloatingActionButton pending = (FloatingActionButton) findViewById(R.id.order1l);
        FloatingActionButton finished = (FloatingActionButton) findViewById(R.id.order2l);
        FloatingActionButton refused = (FloatingActionButton) findViewById(R.id.order3l);
        FloatingActionButton disponible = (FloatingActionButton) findViewById(R.id.disponibilizarl) ;
        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.addl) ;

        list = getListView();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order tmp = current.get(position);
                lastUsed = tmp.getId();
                Intent intent = new Intent(MyListOrdersCurrent.this, OneOrder.class);
                intent.putExtra("order", tmp);
                intent.putExtra("chef", chef);
                startActivityForResult(intent, MODIFICATION);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyListOrdersPending.this, AddDish.class);
                intent.putExtra("chef", chef);
                startActivity(intent);
            }
        });

        disponible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyListOrdersPending.this, MenuDisplay.class);
                intent.putExtra("chef", chef);
                startActivity(intent);
            }
        });

        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent,REQUEST_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_LOGIN:
                if (resultCode == RESULT_OK) {
                    chef = (Chef) data.getSerializableExtra("chef");
                    apiservice = ApiManager.createService(ApiService.class, chef.getPseudo(), chef.getPassword());
                    Call<List<Order>> call = apiservice.getPendingOrder(chef.getPseudo());
                    call.enqueue(new Callback<List<Order>>() {
                        @Override
                        public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                            pending = response.body();
                            Log.d(TAG, "onResponse: "+pending.get(0).getChef().getPseudo());

                            Order[] tPending = pending.toArray(new Order[pending.size()]);
                            adapter = new OrderAdapter(tPending, MyListOrdersPending.this);
                            setListAdapter(adapter);
                        }

                        @Override
                        public void onFailure(Call<List<Order>> call, Throwable t) {

                        }
                    });
                }
                break;
            case MODIFICATION:
                if (resultCode == RESULT_OK){
                    List<Object> objs;
                    Iterator<Order> i = pending.iterator();
                    while (i.hasNext()) {
                        Order o = i.next();
                        if (o.getId() == lastUsed)
                            i.remove();
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }
}
