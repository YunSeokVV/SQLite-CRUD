package com.example.comparewithmvvmrecyclerviewcrud.ViewModel;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.comparewithmvvmrecyclerviewcrud.Model.People;
import com.example.comparewithmvvmrecyclerviewcrud.SQLite.FeedReaderContract;
import com.example.comparewithmvvmrecyclerviewcrud.SQLite.FeedReaderDbHelper;

import java.util.ArrayList;
import java.util.List;

//AndroidViewModel 클래스를 Viewmodel 대신 사용하면 ViewModel 에서 Context를 사용할 수 있다. 하지만 가급적 이런 방식은 지양해야 한다고 한다.
public class MainActivityViewModel extends AndroidViewModel {
    public static String TAG = "MainActivityViewModel";
    Context context = getApplication().getApplicationContext();

    // SQLite에서 쿼리후 모든 결과 데이터를 담는 리스트.
    ArrayList<People> item = new ArrayList<>();

    // 리사이클러뷰 안의 데이터를 관찰한다.
    private MutableLiveData<ArrayList<People>> mutable_item = new MutableLiveData<>();

    FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(context);
    // getWritableDatabase : Create and/or open a database that will be used for reading and writing.
    SQLiteDatabase db = dbHelper.getWritableDatabase();

    // Filter results WHERE "title" = 'My Title' 쿼리할려고 선언했던 변수들이다. 지금은 데이터를 다 갖고오기 때문에 안쓰인다.
    String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ?";
    String[] selectionArgs = { "27" };

    // How you want the results sorted in the resulting Cursor
    String sortOrder = FeedReaderContract.FeedEntry.COLUMN_NAME_AGE + " DESC";


    // 테이블안의 모든 데이터를 갖고오는 함수
    public ArrayList<People> SearchAllData(){
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
        mutable_item.setValue(item);
        return item;
    }

    public void DeleteValue(int position, People people){
        // SQLite 에서 데이터 삭제
        // Define 'where' part of query.
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { people.getName() };
        // Issue SQL statement.
        int deletedRows = db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
        Log.v(TAG,"deletedRows "+deletedRows);

        //MutableLiveData에서 데이터 삭제
        mutable_item.postValue(item);
        ArrayList<People> currentPeoples = mutable_item.getValue();
        currentPeoples.remove(position);
        mutable_item.postValue(currentPeoples);
    }

    public void AddValue(People people){
        // SQLite 에서 데이터 추가
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        //values 의 키값은 테이블명, values의 value 값은 컬럼값이 된다.
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, people.getName());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_AGE, people.getAge());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
        Log.v(TAG,"newRowId "+newRowId);

        //MutableLiveData 에 데이터 추가
        mutable_item.postValue(item);
        ArrayList<People> addedPerson = mutable_item.getValue();
        addedPerson.add(people);
        mutable_item.postValue(addedPerson);
    }

    public void UpdateValue(People chagned_people, int position, String choosen_user){

        Log.v(TAG,"people "+chagned_people.getAge()+" "+chagned_people.getName());
        Log.v(TAG,"position "+position);
        Log.v(TAG,"choosen_user "+choosen_user);

        // SQLite 에서 데이터 수정
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // New value for one column
        String changed_name = chagned_people.getName();
        String changed_age = chagned_people.getAge();
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, changed_name);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_AGE, changed_age);

        // Which row to update, based on the title
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
        String[] selectionArgs = { choosen_user };

        int count = db.update(
                FeedReaderDbHelper.FeedEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        Log.v(TAG,"count "+count);

        //MutableLiveData에서 데이터 수정
        mutable_item.postValue(item);
        ArrayList<People> currentPeoples = mutable_item.getValue();
        currentPeoples.set(position, chagned_people);
        mutable_item.postValue(currentPeoples);
    }

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<ArrayList<People>> getMutable_item() {
        return mutable_item;
    }


}
