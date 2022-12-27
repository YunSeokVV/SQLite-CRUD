package com.example.comparewithmvvmrecyclerviewcrud.SQLite;

import android.provider.BaseColumns;
// 공식문서 보고 SQLite 공부중. 공부로그 12월22일 참조.
public final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FeedReaderContract() {}

    private static FeedReaderContract instance;

    public static FeedReaderContract getInstance(){
        if (instance == null){
            synchronized (FeedReaderContract.class){
                instance = new FeedReaderContract();
            }
        }
        return instance;
    }


    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_TITLE = "name";
        public static final String COLUMN_NAME_AGE = "age";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_TITLE + " TEXT," +
                    FeedEntry.COLUMN_NAME_AGE + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;
}