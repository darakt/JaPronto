package com.example.darakt.japronto;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.darakt.japronto.REST.models.Dish;
import com.example.darakt.japronto.REST.models.Menu;
import com.example.darakt.japronto.REST.models.Order;

/**
 * Created by darakt on 17/10/16.
 */

public class MyAdapterOld  extends BaseAdapter {

    Order[] wanted;
    LayoutInflater inflater;
    Context context;
    final String TAG = "MyAdapterOld";

    private class MyViewHolder{
        TextView name, lat, lng, state, date, time;

        public MyViewHolder(View view){
            name = (TextView) view.findViewById(R.id.nameC);
            lat = (TextView) view.findViewById( R.id.lat);
            lng = (TextView) view.findViewById(R.id.lng);
            state = (TextView) view.findViewById(R.id.state);
            date = (TextView) view.findViewById(R.id.date);
            time = (TextView) view.findViewById(R.id.time);
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
        Log.d(TAG, "getView: "+ tmp.getId_chef());
        mViewHolder.name.setText(Integer.toString(tmp.getId_chef()));
        mViewHolder.lat.setText(tmp.getLat());
        mViewHolder.lng.setText(tmp.getLng());
        mViewHolder.state.setText(Integer.toString(tmp.getState()));
        mViewHolder.date.setText(tmp.getFor_the_date());
        mViewHolder.time.setText(tmp.getFor_the_time());
        //mViewHolder.note.setText(tmp.getNumber());
        //mViewHolder.it.setImageBitmap(tmp.getImage());

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
