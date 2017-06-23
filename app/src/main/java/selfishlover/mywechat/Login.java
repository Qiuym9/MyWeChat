package selfishlover.mywechat;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    MyNetWorkManager manager;
    AsyncTask<Void, Void, Boolean> task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        manager = MyNetWorkManager.getInstance();
        setListener();
    }

    private void setListener() {
        Button login = (Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText server = (EditText)findViewById(R.id.serverip);
                String inputip = server.getText().toString();
                if (inputip.equals("")) {
                    Toast.makeText(Login.this, "服务器IP不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                EditText text = (EditText)findViewById(R.id.username);
                String name = text.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(Login.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                manager.serverip = inputip;
                manager.myname = name;

                task = new AsyncTask<Void, Void, Boolean>() {
                    @Override
                    protected Boolean doInBackground(Void... voids) {
                        boolean result1 = manager.setupConnection();
                        if (!result1) return false;
                        boolean result2 = manager.bindMyname();
                        return result2;
                    }
                    @Override
                    protected void onPostExecute(Boolean result) {
                        if (!result) {
                            Toast.makeText(Login.this, "登录失败", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            Login.this.startActivity(intent);
                        }
                    }
                };
                task.execute();
            }
        });
    }
}
