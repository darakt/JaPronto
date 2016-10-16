package com.example.darakt.japronto;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.darakt.japronto.REST.models.Dish;

/**
 * Created by darakt on 12/10/16.
 */

public class MyAdapterGrid extends BaseAdapter {

    Dish[] dishes;
    LayoutInflater inflater;
    Context context;
    final String TAG = "MyAdapterGrid";

    public MyAdapterGrid(Dish[] dish, Context context) {
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
            convertView = inflater.inflate(R.layout.activity_grid_item, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        Dish tmp = dishes[position];
        mViewHolder.name.setText(tmp.getName());
        mViewHolder.note.setText("8/10");
        //mViewHolder.it.setImageBitmap(tmp.getImage());

        return convertView;
    }

    private class MyViewHolder{
        TextView name, note;
        ImageView it;

        public MyViewHolder(View view){
            name = (TextView) view.findViewById(R.id.nameDish);
            note = (TextView) view.findViewById(R.id.avg);
            it = (ImageView) view.findViewById(R.id.image);
        }
    }
}
