package io.ohalloran.crypto;

import android.content.Intent;
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

import io.ohalloran.crypto.data.ParseFactory;
import io.ohalloran.crypto.data.Person;

//single conversation
public class ConversationsActivity extends ActionBarActivity implements ParseFactory.OnParseUpdateListener {
    ListView listView;
    TextView emptyText;

    ListAdapter<Person> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);
        listView = (ListView) findViewById(android.R.id.list);
        emptyText = (TextView) findViewById(android.R.id.text1);

        //  firstTimeConfig();

        adapter = new ListAdapter<Person>(ParseFactory.getPeople()) {
            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                View root = view != null ? view :
                        getLayoutInflater().inflate(R.layout.conversation_header, viewGroup, false);

                QuickContactBadge contactBadge = (QuickContactBadge) root.findViewById(R.id.avatar);
                TextView name = (TextView) root.findViewById(R.id.header_name);

                Person data = getItem(i);
                name.setText(data.userName());

                contactBadge.setImageResource(getImgResource(data));
                return root;
            }

            int getImgResource(Person who) {
                switch (who.userName()) {
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

        ParseFactory.refresh(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Person data = adapter.getItem(i);
                Intent intent = new Intent(ConversationsActivity.this, MessagesActivity.class);
                intent.putExtra(MessagesActivity.PERSON_ID, data.getID());
                startActivity(intent);
            }
        });
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
            case R.id.refresh:
                ParseFactory.refresh(this);
        }
        return false;
    }

    @Override
    public void onComplete() {
        adapter.updateData(ParseFactory.getPeople());
    }
}
