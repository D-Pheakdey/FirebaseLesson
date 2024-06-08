package com.example.firebasedatabase2024;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    private ArrayList<User> arraylist;
    private Activity myContext;

    public MyAdapter(Activity context, ArrayList<User> arraylist) {
        this.arraylist = arraylist;
        this.myContext = context;
    }
    @Override
    public int getCount() {
        return arraylist.size();
    }

    @Override
    public Object getItem(int position) {
        return arraylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return  position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View single_row = convertView;
        if (single_row == null) {
            LayoutInflater layoutInflater = myContext.getLayoutInflater();
            single_row = layoutInflater.inflate(R.layout.single_row, null);
        }

        TextView id = single_row.findViewById(R.id.txtUid);
        TextView name = single_row.findViewById(R.id.txtUname);
        TextView pass = single_row.findViewById(R.id.txtUpass);

        User myUser = arraylist.get(position);
        id.setText(myUser.getId());
        name.setText(myUser.getName());
        pass.setText(myUser.getPass());
        
        return single_row;
    }
    public  void textFilter(ArrayList<User>results){
        arraylist=new ArrayList<>();
        arraylist.addAll(results);
        notifyDataSetChanged();
    }
}
