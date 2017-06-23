package selfishlover.mywechat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by selfishlover on 2017/6/22.
 */

public class ChatRecordHelper extends SQLiteOpenHelper {
    String tablename;
    ChatRecordHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        tablename = name;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create = "create table if not exists " + tablename + " (_id integer primary key autoincrement, speaker text, message text)";
        sqLiteDatabase.execSQL(create);
    }

    public long insert(String speaker, String message) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("speaker", speaker);
        values.put("message", message);
        long flag = database.insert(tablename, null, values);
        database.close();
        return flag;
    }

    public Cursor query() {
        SQLiteDatabase database = getReadableDatabase();
        String rawquery = "select * from " + tablename;
        return database.rawQuery(rawquery, null);
    }
}
