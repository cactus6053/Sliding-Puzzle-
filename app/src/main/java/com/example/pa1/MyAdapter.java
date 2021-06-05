package com.example.pa1;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.ListIterator;

public class MyAdapter extends BaseAdapter {
    Context c;
    Bitmap puzzles[];

    MyAdapter(Context c, Bitmap arr[])
    {
        this.c = c;
        puzzles = arr;
    }

    @Override
    public int getCount()
    {
        return puzzles.length;
    }

    @Override
    public Object getItem(int i)
    {
        return null;
    }

    @Override
    public long getItemId(int i)
    {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        if (view == null)
        {
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.sub_layout, null);
        }
        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setImageBitmap(puzzles[i]);
        return view;
    }
    public void changePos(Bitmap arr[]){
        this.puzzles = arr;
    }
}
