package com.example.comparewithmvvmrecyclerviewcrud.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.amitshekhar.DebugDB;
import com.example.comparewithmvvmrecyclerviewcrud.Adapter.Adapter;
import com.example.comparewithmvvmrecyclerviewcrud.Model.People;
import com.example.comparewithmvvmrecyclerviewcrud.Adapter.PeopleItemClickListener;
import com.example.comparewithmvvmrecyclerviewcrud.R;
import com.example.comparewithmvvmrecyclerviewcrud.SQLite.FeedReaderContract;
import com.example.comparewithmvvmrecyclerviewcrud.SQLite.FeedReaderDbHelper;
import com.example.comparewithmvvmrecyclerviewcrud.ViewModel.MainActivityViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button button;
    String name ="";
    String age ="";
    String TAG = "MainActivity";
    private MainActivityViewModel mMainActivityViewModel;


    RecyclerView recyclerView;
    Adapter adapter;
    int current_position;
    ArrayList<People> item = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mMainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        button = findViewById(R.id.button);
        adapter = new Adapter(MainActivity.this);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        adapter.InitItem(mMainActivityViewModel.SearchAllData());
        //adapter.notifyItemInserted(adapter.items.size());


        mMainActivityViewModel.getMutable_item().observe(this, new Observer<ArrayList<People>>() {
            @Override
            public void onChanged(ArrayList<People> people) {
                adapter.notifyDataSetChanged();
            }
        });

        Log.v(TAG,"You can Check your DB here");
        DebugDB.getAddressLog();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.putExtra("status","추가");
                startActivityForResult(intent,0);

            }
        });



        adapter.setOnItemClicklistener(new PeopleItemClickListener() {
            @Override
            public void onItemClick(Adapter.ViewHolder holder, View view, int position) {
                Log.v(TAG,"position "+"전체 누름");

                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.putExtra("status","수정");
                intent.putExtra("name",adapter.items.get(position).getName());
                intent.putExtra("age",adapter.items.get(position).getAge());
                startActivityForResult(intent,1);
                current_position = position;
            }

            @Override
            public void onItemClickDelete(Adapter.ViewHolder holder, View view, int position) {
                Log.v(TAG,"position "+"삭제 누름");

                mMainActivityViewModel.DeleteValue(position, adapter.items.get(position));
                adapter.notifyItemRemoved(position);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data!=null) {
            Log.v(TAG, "null 아님");

            name = data.getStringExtra("name");
            age = data.getStringExtra("age");


            //아이템 추가하는 경우
            if (requestCode == 0) { // 결과가 OK
                Log.v(TAG, "아이템 추가");

                mMainActivityViewModel.AddValue(new People(name, age));

            }

            //아이템 수정하는 경우
            else {
                mMainActivityViewModel.UpdateValue(new People(name, age), current_position, data.getStringExtra("choosen_user"));
                Log.v(TAG, "아이템 수정");

            }
        }

        //이전화면에서 뒤로가기 한 경우
        else{
            Log.v(TAG, "null 임");
        }


    }
}
