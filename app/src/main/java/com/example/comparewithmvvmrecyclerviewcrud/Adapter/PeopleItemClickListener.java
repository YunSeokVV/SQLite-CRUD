package com.example.comparewithmvvmrecyclerviewcrud.Adapter;

import android.view.View;

import com.example.comparewithmvvmrecyclerviewcrud.Adapter.Adapter;

public interface PeopleItemClickListener {
    public void onItemClick(Adapter.ViewHolder holder, View view, int position);
    public void onItemClickDelete(Adapter.ViewHolder holder, View view, int position);
}
