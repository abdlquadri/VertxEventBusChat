package abdlquadri.ng.vertxeventbus;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import mjson.Json;
import ng.abdlquadri.eventbus.EventBus;
import ng.abdlquadri.eventbus.handlers.ConnectHandler;
import ng.abdlquadri.eventbus.handlers.Handler;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText text;
    private RecyclerView chatList;
    ArrayList chatDB = new ArrayList<Chat>();
    private ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        text = (EditText) findViewById(R.id.text);
        chatList = (RecyclerView) findViewById(R.id.chat_list);

        chatList.setLayoutManager(new LinearLayoutManager(this));

         adapter = new ChatAdapter(chatDB);
        chatList.setAdapter(adapter);

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.send("chat.to.server", Json.object().set("message", text.getText().toString().trim()).toString());
                text.setText("");
            }
        });


    }

    @Override
    protected void onResume() {
        new ConnectAsyncTask().execute();
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public class ConnectAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            final String[] result = {"hf"};
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            EventBus.connect("your-server-ip", 7000, new ConnectHandler() {
                @Override
                public void connected(boolean isConnected) {
                    if (isConnected) {
                        result[0] = getTitle() + " : Connected";
                        EventBus.registerHandler("chat.to.client", new Handler() {
                            @Override
                            public void handle(String message) {
                                System.out.println(message);

                                String s = Json.read(message).at("body").at("chat").at("message").asString();
                                String t = Json.read(message).at("body").at("timestamp").asString();
                                chatDB.add(new Chat(s,t));
                                Snackbar.make(toolbar, s+t, Snackbar.LENGTH_LONG)
                                        .setAction("Close", null).show();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.notifyDataSetChanged();

                                    }
                                });

                            }
                        });

                        EventBus.send("chat.to.server", Json.object().set("message", "maasage").toString());


                    } else {
                        result[0] = getTitle() + " : Not Connected";

                    }
                }
            });
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return result[0];
        }

        @Override
        protected void onPostExecute(String s) {

            setTitle(s);
        }
    }

}
