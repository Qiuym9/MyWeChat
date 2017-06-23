package selfishlover.mywechat;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Chat extends AppCompatActivity {
    MyNetWorkManager manager;
    String friendname;
    EditText input;
    AsyncTask<Void, String, Void> listentask;
    Myadapter4 adapter4;
    ChatingListHelper chatingListHelper;
    FriendListHelper friendListHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        manager = MyNetWorkManager.getInstance();
        chatingListHelper = new ChatingListHelper(this, "ChatingList", null, 1);
        friendListHelper = new FriendListHelper(this, "FriendList", null, 1);
        initizeUI();
        listenToServer();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            stopListening();
            finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, keyEvent);
        }
    }
    void initizeUI() {
        TextView namebar = (TextView)findViewById(R.id.namebar);
        Bundle bundle = getIntent().getExtras();
        friendname = bundle.getString("name");
        namebar.setText(friendname);
        input = (EditText)findViewById(R.id.message);
        Button sendbutton = (Button)findViewById(R.id.send);
        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userinput = input.getText().toString();
                if (userinput.equals("")) {
                    Toast.makeText(Chat.this, "发送的消息不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                input.setText("");
                adapter4.addRecord(manager.myname, userinput);
                if (chatingListHelper.insert(friendname, manager.myname, userinput) < 0) {
                    chatingListHelper.update(friendname, manager.myname, userinput);
                }
                if (friendname.equals("群聊")) {
                    manager.groupChat(userinput);
                } else {
                    manager.personalChat(friendname, userinput);
                }
            }
        });
        ListView listView = (ListView)findViewById(R.id.messagelist);
        ChatRecordHelper helper = new ChatRecordHelper(this, friendname, null, 1);
        adapter4 = new Myadapter4(this, manager.myname, helper);
        listView.setAdapter(adapter4);
    }
    void listenToServer() {
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
                if (tag.equals("2")) {
                    friendListHelper.insert(content);
                    return;
                }
                if (tag.equals("3")) {
                    friendListHelper.delete(content);
                    return;
                }
                if (tag.equals("4")) {
                    int index = content.indexOf(" ");
                    String sender = content.substring(0, index);
                    String words = content.substring(index+1);
                    if (sender.equals(friendname)) {
                        adapter4.addRecord(sender, words);
                    } else {
                        ChatRecordHelper helper = new ChatRecordHelper(Chat.this, sender, null, 1);
                        helper.insert(sender, words);
                    }
                    if (chatingListHelper.insert(sender, sender, words) < 0) {
                        chatingListHelper.update(sender, sender, words);
                    }
                    return;
                }
                if (tag.equals("5")) {
                    int index = content.indexOf(" ");
                    String sender = content.substring(0, index);
                    String words = content.substring(index+1);
                    if (friendname.equals("群聊")) {
                        adapter4.addRecord(sender, words);
                    } else {
                        ChatRecordHelper helper = new ChatRecordHelper(Chat.this, "群聊", null, 1);
                        helper.insert(sender, words);
                    }
                    if (chatingListHelper.insert("群聊", sender, words) < 0) {
                        chatingListHelper.update("群聊", sender, words);
                    }
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
