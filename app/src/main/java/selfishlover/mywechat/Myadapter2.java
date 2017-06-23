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

public class Myadapter2 extends BaseAdapter {
    public FriendListHelper helper;
    public List<String> list;
    private Context context;
    Myadapter2(Context pcontext, FriendListHelper phelper) {
        helper = phelper;
        list = new ArrayList<>();
        context = pcontext;
        updateSelf();
    }
    public void updateSelf() {
        list.clear();
        Cursor cursor = helper.query();
        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex("name");
            String name = cursor.getString(index);
            list.add(name);
        }
        cursor.close();
        notifyDataSetChanged();
    }
    public void addFriend(String friendname) {
        for (String name : list) {
            if (name.equals(friendname)) return;
        }
        list.add(friendname);
        helper.insert(friendname);
        notifyDataSetChanged();
    }
    public void removeFriend(String friendname) {
        list.remove(friendname);
        helper.delete(friendname);
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
            view = LayoutInflater.from(context).inflate(R.layout.item2, viewgroup, false);
        String username = list.get(i);
        Button icon = (Button)view.findViewById(R.id.usericon);
        TextView name = (TextView)view.findViewById(R.id.username);
        String firstletter = ""+username.charAt(0);
        icon.setText(firstletter);
        name.setText(username);
        return view;
    }
}
