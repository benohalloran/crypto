package io.ohalloran.crypto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.ohalloran.crypto.data.Message;
import io.ohalloran.crypto.data.Person;
import io.ohalloran.crypto.utils.ListAdapter;

//single conversation
public class ConversationsActivity extends ActionBarActivity {
    ListView listView;
    TextView emptyText;

    ListAdapter<Person> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);
        listView = (ListView) findViewById(android.R.id.list);
        emptyText = (TextView) findViewById(android.R.id.text1);

        firstTimeConfig();

        adapter = new ListAdapter<Person>(Person.listAll(Person.class)) {
            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                View root = view != null ? view :
                        getLayoutInflater().inflate(R.layout.conversation_header, viewGroup, false);

                QuickContactBadge contactBadge = (QuickContactBadge) root.findViewById(R.id.avatar);
                TextView name = (TextView) root.findViewById(R.id.header_name);

                Person data = getItem(i);
                name.setText(data.name);

                contactBadge.setImageResource(getImgResource(data));
                return root;
            }

            int getImgResource(Person who) {
                switch (who.name) {
                    case "Spongebob":
                        return R.drawable.spongebob;
                    case "Mr. Krabs":
                        return R.drawable.mr_krabs;
                    case "Sandy":
                        return R.drawable.sandy;
                    case "Squidward":
                        return R.drawable.squidward;
                }
                return -1;
            }
        };
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Person data = adapter.getItem(i);
                Intent intent = new Intent(ConversationsActivity.this, MessagesActivity.class);
                intent.putExtra(MessagesActivity.PERSON_ID, (long) data.getId());
                startActivity(intent);
            }
        });
    }

    private void firstTimeConfig() {
        boolean mboolean = false;

        SharedPreferences settings = getSharedPreferences("PREFS_NAME", 0);
        mboolean = settings.getBoolean("FIRST_RUN", false);
        if (!mboolean) {
            // do the thing for the first time
            settings = getSharedPreferences("PREFS_NAME", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("FIRST_RUN", true);
            editor.commit();

            List<Person> list = new ArrayList<>();
            for (String s : new String[]{"Spongebob", "Mr. Krabs", "Sandy", "Squidward"}) {
                Person p = new Person(s);
                list.add(p);
                p.save();
            }
            for (int i = 0; i < 100; i++) {
                Message mes = new Message(i + "", list.get(i % list.size()).name,
                        list.get((int) (Math.random() * list.size())).name);
                mes.save();
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_conversation_acitivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_thread:
                //TODO new message???
                return true;
        }
        return false;
    }
}
