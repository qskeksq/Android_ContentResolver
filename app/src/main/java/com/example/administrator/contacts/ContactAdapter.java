package com.example.administrator.contacts;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.contacts.domain.PhoneBook;

import java.util.List;

/**
 * Created by Administrator on 2017-06-01.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.Holder> {

    List<PhoneBook> datas;

    public ContactAdapter(List<PhoneBook> datas){
        this.datas = datas;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new Holder(view);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        PhoneBook pb = datas.get(position);
        holder.id.setText(pb.getId());
        holder.name.setText(pb.getName());
        holder.tel.setText(pb.getTel());
    }

    class Holder extends RecyclerView.ViewHolder{

        TextView id, name, tel;

        public Holder(View view){
            super(view);
            id = (TextView) view.findViewById(R.id.id);
            name = (TextView) view.findViewById(R.id.name);
            tel = (TextView) view.findViewById(R.id.tel);
        }

    }

}
