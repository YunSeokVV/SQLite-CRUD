package com.example.comparewithmvvmrecyclerviewcrud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amitshekhar.DebugDB;
import com.example.comparewithmvvmrecyclerviewcrud.SQLite.FeedReaderContract;
import com.example.comparewithmvvmrecyclerviewcrud.SQLite.FeedReaderDbHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button button;
    String name ="";
    String age ="";
    String TAG = "MainActivity";

    RecyclerView recyclerView;
    Adapter adapter;
    int current_position;
    ArrayList<People> item = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        adapter = new Adapter(MainActivity.this);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(this);
        // getWritableDatabase : Create and/or open a database that will be used for reading and writing.
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Define a projection that specifies which columns from the database you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_AGE
        };

        // Filter results WHERE "title" = 'My Title' 쿼리할려고 선언했던 변수들이다. 지금은 데이터를 다 갖고오기 때문에 안쓰인다.
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArgs = { "27" };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = FeedReaderContract.FeedEntry.COLUMN_NAME_AGE + " DESC";

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
                //projection,             // The array of columns to return (pass null to get all) 테이블안의 모든 데이터를 갖고오기 위해서 원래 있던 코드를 수정.
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );


        while(cursor.moveToNext()) {
            item.add(new People(cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE)),cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_AGE))));
        }
        cursor.close();

        adapter.InitItem(item);
        adapter.notifyItemInserted(adapter.items.size());

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

                // Define 'where' part of query.
                String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
                // Specify arguments in placeholder order.
                String[] selectionArgs = { adapter.items.get(position).getName() };
                // Issue SQL statement.
                int deletedRows = db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs);

                adapter.items.remove(position);
                adapter.notifyItemRemoved(position);

                Log.v(TAG,"deletedRows "+deletedRows);
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
                People people = new People(name, age);
                adapter.addItem(people);
                adapter.notifyItemInserted(adapter.items.size());

                Log.v(TAG, "adapter.items.size() " + adapter.items.size());
            }

            //아이템 수정하는 경우
            else {
                Log.v(TAG, "아이템 수정");
                adapter.items.get(current_position).setName(name);
                adapter.items.get(current_position).setAge(age);
                adapter.notifyItemChanged(current_position);
            }
        }

        //이전화면에서 뒤로가기 한 경우
        else{
            Log.v(TAG, "null 임");
        }


    }
}
