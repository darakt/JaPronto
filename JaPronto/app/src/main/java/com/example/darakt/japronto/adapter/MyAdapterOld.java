package com.example.darakt.japronto.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.darakt.japronto.R;
import com.example.darakt.japronto.REST.models.Dish;
import com.example.darakt.japronto.REST.models.Menu;
import com.example.darakt.japronto.REST.models.Order;
import com.example.darakt.japronto.historic.ListOldDish;

/**
 * Created by darakt on 17/10/16.
 */

public class MyAdapterOld  extends BaseAdapter {

    Order[] wanted;
    LayoutInflater inflater;
    Context context;
    final String TAG = "MyAdapterOld";

    private class MyViewHolder{
        TextView response;

        public MyViewHolder(View view){
            response = (TextView) view.findViewById(R.id.response);
        }
    }

    public MyAdapterOld(Context context, Order[] order) {
        Log.d(TAG, "MyAdapterOld: "+order[0].getCustomer_pseudo());
        this.wanted = order;
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
        for( int j=0; j<wanted.length; j++){
            Menu tmp = wanted[j].getWanted();
            Dish[] tp = tmp.convertToArray();
            int i=0;
            for (Dish d : tmp.getDishes()){
                Log.d(TAG, "Menu: "+d.getName());
                Log.d(TAG, "Array: "+tp[i].getName());
                Log.d(TAG, "MyAdapterOld: "+tp[i].getNumber());
                Log.d(TAG, "MyAdapterOld: "+tp[i].getPrice());
                i=+1;
            }
        }
    }

    @Override
    public int getCount() {
        return wanted.length;
    }

    @Override
    public Object getItem(int position) {
        return this.wanted[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean areAllItemsEnabled()
    {
        return true;
    }

    @Override
    public boolean isEnabled(int arg0)
    {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyAdapterOld.MyViewHolder mViewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_old_row, parent, false);
            mViewHolder = new MyAdapterOld.MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyAdapterOld.MyViewHolder) convertView.getTag();
        }
        final int pos = position;
        Order tmp = wanted[position];
        String state = null;
        switch (tmp.getState()) {
            case 0: state = "esperando approvação";
                break;
            case 1: state = "aceitada, espera a entrega";
                break;
            case 2: state = "ja entregada";
                break;
            case 3: state = "ja recusada";
        }
        String response = String.format("O pedido numero %d do chefe %s está %s, é para o %s a %s, nessa posição GPS (%s,%s)", tmp.getId(), tmp.getChef().getPseudo(), state, tmp.getFor_the_date(), tmp.getFor_the_time(), tmp.getLat(), tmp.getLng());
        mViewHolder.response.setText(response);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onItemClick: ");
                Intent intent = new Intent(context, ListOldDish.class);
                intent.putExtra("order", wanted[pos]);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

}
