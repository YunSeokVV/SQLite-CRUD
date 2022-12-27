package com.example.comparewithmvvmrecyclerviewcrud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

                //데이터 추가 케이스
                if(intent_case.equals("추가")){
                    // Create a new map of values, where column names are the keys
                    ContentValues values = new ContentValues();
                    //values 의 키값은 테이블명, values의 value 값은 컬럼값이 된다.
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, name.getText().toString());
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_AGE, age.getText().toString());

                    // Insert the new row, returning the primary key value of the new row
                    long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
                    Log.v(TAG,"newRowId "+newRowId);
                }

                //데이터 수정 케이스
                else{
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    // New value for one column
                    String changed_name = name.getText().toString();
                    String changed_age = age.getText().toString();
                    ContentValues values = new ContentValues();
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, changed_name);
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_AGE, changed_age);

                    // Which row to update, based on the title
                    String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
                    String[] selectionArgs = { getIntent().getStringExtra("name") };

                    int count = db.update(
                            FeedReaderDbHelper.FeedEntry.TABLE_NAME,
                            values,
                            selection,
                            selectionArgs);
                    Log.v(TAG,"count "+count);
                }

                setResult(RESULT_OK,intent);
                finish();
            }
        });


    }
}
