package com.example.comparewithmvvmrecyclerviewcrud.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.comparewithmvvmrecyclerviewcrud.R;
import com.example.comparewithmvvmrecyclerviewcrud.SQLite.FeedReaderContract;
import com.example.comparewithmvvmrecyclerviewcrud.SQLite.FeedReaderDbHelper;

public class Main2Activity extends AppCompatActivity {

    Button btn;
    String intent_case;
    EditText name;
    EditText age;
    String TAG = "Main2Activity";

    FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(Main2Activity.this);
    // getWritableDatabase : Create and/or open a database that will be used for reading and writing.
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        btn = findViewById(R.id.btn);
        name = findViewById(R.id.input_name);
        age = findViewById(R.id.input_age);


        intent_case = getIntent().getStringExtra("status");
        Log.v(TAG,"intent_case "+intent_case);
        btn.setText(intent_case);

        if(intent_case.equals("수정")){
            name.setText(getIntent().getStringExtra("name"));
            age.setText(getIntent().getStringExtra("age"));
        }


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("age",age.getText().toString());
                intent.putExtra("name",name.getText().toString());
                intent.putExtra("choosen_user",getIntent().getStringExtra("name"));

                setResult(RESULT_OK,intent);
                finish();
            }
        });


    }
}
