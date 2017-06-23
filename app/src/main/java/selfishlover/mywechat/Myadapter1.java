package selfishlover.mywechat;

import android.database.Cursor;
import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by selfishlover on 2016/10/29.
 */

public class Myadapter1 extends BaseAdapter {
    ChatingListHelper helper;
    public List<ChatingRecord> list;
    private Context context;
    Myadapter1(Context pcontext, ChatingListHelper phelper) {
        list = new ArrayList<>();
        context = pcontext;
        helper = phelper;
        updateSelf();
    }
    public void updateSelf() {
        list.clear();
        Cursor cursor = helper.query();
        while (cursor.moveToNext()) {
            int index1 = cursor.getColumnIndex("name");
            int index2 = cursor.getColumnIndex("speaker");
            int index3 = cursor.getColumnIndex("message");
            String name = cursor.getString(index1);
            String speaker = cursor.getString(index2);
            String message = cursor.getString(index3);
            list.add(new ChatingRecord(name, speaker, message));
        }
        cursor.close();
        notifyDataSetChanged();
    }
    public void addorupdateRecord(String name, String speaker, String message) {
        boolean flag = false;
        ChatingRecord temp = null;
        for (ChatingRecord record : list) {
            if (record.name.equals(name)) {
                flag = true;
                temp = record;
                break;
            }
        }
        if (flag) {
            temp.speaker = speaker;
            temp.message = message;
            helper.update(name, speaker, message);
        } else {
            list.add(new ChatingRecord(name, speaker, message));
            helper.insert(name, speaker, message);
        }
        notifyDataSetChanged();
    }
    public void deleteRecord(String name) {
        ChatingRecord temp = null;
        for (ChatingRecord record : list) {
            if (record.name.equals(name)) {
                temp = record;
                break;
            }
        }
        list.remove(temp);
        helper.delete(name);
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
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.item1, viewgroup, false);
        ChatingRecord record = list.get(i);
        Button icon = (Button)view.findViewById(R.id.icon);
        TextView speaker = (TextView)view.findViewById(R.id.speaker);
        TextView message = (TextView)view.findViewById(R.id.message);
        icon.setText(record.name.substring(0, 1));
        speaker.setText(record.speaker);
        message.setText(record.message);
        return view;
    }
}
