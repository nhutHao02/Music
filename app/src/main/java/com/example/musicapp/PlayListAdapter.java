package com.example.musicapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
public class PlayListAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<PlayList> playList;

    public PlayListAdapter(Context context, int layout, List<PlayList> playList) {
        this.context = context;
        this.layout = layout;
        this.playList = playList;
    }

    @Override
    public int getCount() {
        return playList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    private class ViewHolder{
        ImageView img;
        TextView name;
        TextView size;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      ViewHolder holder=new ViewHolder();
        if(view==null){

            view=inflater.inflate(layout,null);

            //anh xa
            holder.img=(ImageView) view.findViewById(R.id.imgList);
            holder.name=(TextView) view.findViewById(R.id.nameList);
            holder.size=(TextView) view.findViewById(R.id.sizeList);
            view.setTag(holder);
        }else {
            holder= (ViewHolder) view.getTag();
        }

        //gan gia tri
        PlayList list =playList.get(i);
        holder.name.setSelected(true);
        holder.name.setText(list.getName());
        holder.size.setText(list.getSize() + " bài hát");
        Picasso.get().load(list.getImg()).into(holder.img);

        return view;
    }
}
