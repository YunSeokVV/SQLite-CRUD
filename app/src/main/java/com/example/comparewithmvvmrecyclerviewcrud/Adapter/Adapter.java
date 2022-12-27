package com.example.comparewithmvvmrecyclerviewcrud.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comparewithmvvmrecyclerviewcrud.Model.People;
import com.example.comparewithmvvmrecyclerviewcrud.R;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{
    public ArrayList<People> items = new ArrayList<>();
    public Context context;
    PeopleItemClickListener peopleItemClickListener;

    public Adapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.recylcerview_item,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        People item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mName;
        private TextView mAge;
        private ImageView delete_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.nameText);
            mAge = itemView.findViewById(R.id.ageText);
            delete_item = itemView.findViewById(R.id.delete_item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.v("Adapter","Adpater 전체 누름");
                    if(peopleItemClickListener != null){
                        peopleItemClickListener.onItemClick(ViewHolder.this, v, getBindingAdapterPosition());
                    }
                }
            });

            delete_item.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    //Log.v("Adapter","Adpater 삭제 누름");
                    if(peopleItemClickListener != null){
                        peopleItemClickListener.onItemClickDelete(ViewHolder.this, v, getBindingAdapterPosition());
                    }
                }
            });

        }

        public void setItem(People item){
            mName.setText(item.getName());
            mAge.setText(item.getAge());
        }
    }


    public void addItem(People item){
        items.add(item);
    }

    public void InitItem(ArrayList<People> arrayList){
        items = arrayList;
    }

    public void setOnItemClicklistener(PeopleItemClickListener listener){
        this.peopleItemClickListener = listener;
    }

}
