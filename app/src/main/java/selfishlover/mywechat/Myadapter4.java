package selfishlover.mywechat;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by selfishlover on 2017/6/23.
 */

public class Myadapter4 extends BaseAdapter {
    Context context;
    String myname;
    List<MessageRecord> list;
    ChatRecordHelper helper;
    Myadapter4(Context con, String my, ChatRecordHelper recordHelper) {
        context = con;
        myname = my;
        helper = recordHelper;
        list = new ArrayList<>();
        updateSelf();
    }
    public void updateSelf() {
        list.clear();
        Cursor cursor = helper.query();
        while (cursor.moveToNext()) {
            int index1 = cursor.getColumnIndex("speaker");
            int index2 = cursor.getColumnIndex("message");
            String speaker = cursor.getString(index1);
            String message = cursor.getString(index2);
            list.add(new MessageRecord(speaker, message));
        }
        cursor.close();
        notifyDataSetChanged();
    }
    public void addRecord(String speaker, String message) {
        list.add(new MessageRecord(speaker, message));
        helper.insert(speaker, message);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        if (list == null) return 0;
        return list.size();
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public Object getItem(int i) {
        if (list == null) return null;
        return list.get(i);
    }
    @Override
    public View getView(int i, View view, ViewGroup viewgroup) {
        MessageRecord record = list.get(i);
        if (view == null) {
            if (record.speaker.equals(myname)) {
                view = LayoutInflater.from(context).inflate(R.layout.my_message_item, viewgroup, false);
            } else {
                view = LayoutInflater.from(context).inflate(R.layout.friend_message_item, viewgroup, false);
            }
        }
        Button icon = (Button)view.findViewById(R.id.icon);
        TextView speakerview = (TextView)view.findViewById(R.id.speaker);
        TextView messageview = (TextView)view.findViewById(R.id.message);
        icon.setText(record.speaker.substring(0, 1));
        speakerview.setText(record.speaker);
        messageview.setText(record.message);
        return view;
    }
}
