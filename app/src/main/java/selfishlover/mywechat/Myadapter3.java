package selfishlover.mywechat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by selfishlover on 2016/10/29.
 */

public class Myadapter3 extends BaseAdapter {
    public List<String> list;
    private Context context;
    Myadapter3(List<String> plist, Context pcontext) {
        list = plist;
        context = pcontext;
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
        String record = list.get(i);
        Button usericon = (Button)view.findViewById(R.id.usericon);
        usericon.setText(record.substring(0, 1));
        TextView username = (TextView)view.findViewById(R.id.username);
        username.setText(record);
        return view;
    }
}
