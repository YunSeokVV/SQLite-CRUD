package com.example.comparewithmvvmrecyclerviewcrud;

import android.view.View;

public interface PeopleItemClickListener {
    public void onItemClick(Adapter.ViewHolder holder, View view, int position);
    public void onItemClickDelete(Adapter.ViewHolder holder, View view, int position);
}
