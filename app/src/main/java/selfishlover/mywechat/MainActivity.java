package selfishlover.mywechat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by selfishlover on 2016/10/29.
 */

public class MainActivity extends AppCompatActivity {
    ListView chatlist, friendlist, resultlist;
    LinearLayout third;
    Myadapter1 adapter1;
    Myadapter2 adapter2;
    Myadapter3 adapter3;
    MyNetWorkManager manager;
    AsyncTask<Void, String, Void> listentask;
    String chosen1, chosen2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chatlist = (ListView)findViewById(R.id.chatlist);
        friendlist = (ListView)findViewById(R.id.friendlist);
        resultlist = (ListView)findViewById(R.id.resultlist);
        third = (LinearLayout)findViewById(R.id.third);
        manager = MyNetWorkManager.getInstance();
        setAdapterAndListener();
        setListener();
        listenToServer();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        adapter1.updateSelf();
        adapter2.updateSelf();
        listenToServer();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            stopListening();
            manager.closeConnection();
            finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, keyEvent);
        }
    }

    private void setAdapterAndListener() {
        ChatingListHelper chatingListHelper = new ChatingListHelper(this, "ChatingList", null, 1);
        adapter1 = new Myadapter1(MainActivity.this, chatingListHelper);
        chatlist.setAdapter(adapter1);
        chatlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                stopListening();
                String name = adapter1.list.get(i).name;
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                Intent intent = new Intent(MainActivity.this, Chat.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }
        });
        chatlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                chosen1 = adapter1.list.get(i).name;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("删除记录");
                builder.setMessage("确定删除该项聊天？");
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        adapter1.deleteRecord(chosen1);
                    }
                });
                builder.create().show();
                return true;
            }
        });

        FriendListHelper helper = new FriendListHelper(this, "FriendList", null, 1);
        helper.insert("群聊");
        adapter2 = new Myadapter2(MainActivity.this, helper);
        friendlist.setAdapter(adapter2);
        friendlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                stopListening();
                TextView temp = (TextView)view.findViewById(R.id.username);
                String name = temp.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                Intent intent = new Intent(MainActivity.this, Chat.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }
        });
        friendlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView temp = (TextView)view.findViewById(R.id.username);
                chosen2 = temp.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("删除好友");
                builder.setMessage("确定删除该好友？");
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        adapter2.removeFriend(chosen2);
                        manager.removeFriend(chosen2);
                    }
                });
                builder.create().show();
                return true;
            }
        });

        List<String> list = new ArrayList<>();
        adapter3 = new Myadapter3(list, MainActivity.this);
        resultlist.setAdapter(adapter3);
        resultlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView friendtext = (TextView)view.findViewById(R.id.username);
                String frinedname = friendtext.getText().toString();
                manager.addFriend(frinedname);
                adapter3.list.remove(i);
                adapter3.notifyDataSetChanged();
                adapter2.addFriend(frinedname);
            }
        });
    }

    private void setListener() {
        Button showchat = (Button)findViewById(R.id.showchat);
        showchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatlist.setVisibility(View.VISIBLE);
                friendlist.setVisibility(View.INVISIBLE);
                third.setVisibility(View.INVISIBLE);
            }
        });
        Button showfriends = (Button)findViewById(R.id.showfirends);
        showfriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatlist.setVisibility(View.INVISIBLE);
                friendlist.setVisibility(View.VISIBLE);
                third.setVisibility(View.INVISIBLE);
            }
        });
        Button showthird = (Button)findViewById(R.id.showthird);
        showthird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatlist.setVisibility(View.INVISIBLE);
                friendlist.setVisibility(View.INVISIBLE);
                third.setVisibility(View.VISIBLE);
            }
        });
        Button query = (Button)findViewById(R.id.query);
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager.queryAll();
            }
        });
    }

    private void listenToServer() {
        listentask = new AsyncTask<Void, String, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                while (true) {
                    String message = manager.getMessage();
                    if (message != null && !message.equals("")) {
                        publishProgress(message);
                    }
                }
            }
            @Override
            protected void onProgressUpdate(String... values) {
                String message = values[0];
                String tag = message.substring(0, 1);
                String content = message.substring(2);
                if (tag.equals("1")) {
                    adapter3.list.clear();
                    String[] temp = content.split(" ");
                    for (String username : temp) {
                        if (!username.equals(manager.myname)) {
                            adapter3.list.add(username);
                        }
                    }
                    adapter3.notifyDataSetChanged();
                    return;
                }
                if (tag.equals("2")) {
                    adapter2.addFriend(content);
                    return;
                }
                if (tag.equals("3")) {
                    adapter2.removeFriend(content);
                    return;
                }
                if (tag.equals("4")) {
                    int index = content.indexOf(" ");
                    String sender = content.substring(0, index);
                    String words = content.substring(index+1);
                    ChatRecordHelper helper = new ChatRecordHelper(MainActivity.this, sender, null, 1);
                    helper.insert(sender, words);
                    adapter1.addorupdateRecord(sender, sender, words);
                    return;
                }
                if (tag.equals("5")) {
                    int index = content.indexOf(" ");
                    String sender = content.substring(0, index);
                    String words = content.substring(index+1);
                    ChatRecordHelper helper = new ChatRecordHelper(MainActivity.this, "群聊", null, 1);
                    helper.insert(sender, words);
                    adapter1.addorupdateRecord("群聊", sender, words);
                }
            }
        };
        listentask.execute();
    }
    void stopListening() {
        if (listentask.isCancelled()) return;
        listentask.cancel(true);
    }
}
