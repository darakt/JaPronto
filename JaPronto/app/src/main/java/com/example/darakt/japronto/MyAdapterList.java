package com.example.darakt.japronto;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.darakt.japronto.REST.models.Dish;

import java.util.List;

/**
 * Created by darakt on 14/10/16.
 */

public class MyAdapterList extends BaseAdapter {

    Dish[] dishes;
    LayoutInflater inflater;
    Context context;
    final String TAG = "MyAdapterList";

    public MyAdapterList(Context context, Dish[] dish) {
        this.dishes = dish;
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return dishes.length;
    }

    @Override
    public Object getItem(int position) {
        return this.dishes[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_row, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        Dish tmp = dishes[position];
        mViewHolder.name.setText(tmp.getName());
        //mViewHolder.note.setText(tmp.getNumber());
        //mViewHolder.it.setImageBitmap(tmp.getImage());

        return convertView;
    }

    private class MyViewHolder{
        TextView name, note,description;
        ImageView it;

        public MyViewHolder(View view){
            name = (TextView) view.findViewById(R.id.nameD);
            note = (TextView) view.findViewById(R.id.AVGD);
            description = (TextView) view.findViewById(R.id.descriptionD);
            it = (ImageView) view.findViewById(R.id.imageD);
        }
    }
}