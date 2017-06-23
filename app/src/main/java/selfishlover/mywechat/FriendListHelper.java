package selfishlover.mywechat;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by selfishlover on 2017/6/21.
 */

public class FriendListHelper extends SQLiteOpenHelper {
    String tablename;
    FriendListHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        tablename = name;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create = "create table if not exists " + tablename + " (name text primary key)";
        sqLiteDatabase.execSQL(create);
    }

    public long insert(String name) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        long flag = database.insert(tablename, null, values);
        database.close();
        return flag;
    }

    public void delete(String name) {
        SQLiteDatabase database = getWritableDatabase();
        String[] args = {name};
        database.delete(tablename, "name = ?", args);
        database.close();
    }

    public Cursor query() {
        SQLiteDatabase database = getReadableDatabase();
        String rawquery = "select * from " + tablename;
        return database.rawQuery(rawquery, null);
    }
}
