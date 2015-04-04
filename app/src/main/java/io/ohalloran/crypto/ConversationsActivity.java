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

import java.util.Date;

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


        listView.setAdapter(adapter = new ListAdapter<Person>(Person.newPerson("Spongebob"),
                Person.newPerson("Sandy"), Person.newPerson("Mr. Krabs")) {
            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                View root = view != null ? view :
                        getLayoutInflater().inflate(R.layout.conversation_header, viewGroup, false);

                QuickContactBadge contactBadge = (QuickContactBadge) root.findViewById(R.id.avatar);
                TextView name = (TextView) root.findViewById(R.id.header_name);
                TextView preview = (TextView) root.findViewById(R.id.header_preview);

                Person data = getItem(i);
                name.setText(data.name);
                return root;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Person data = adapter.getItem(i);
                Intent intent = new Intent(ConversationsActivity.this, MessagesActivity.class);
                intent.putExtra(MessagesActivity.PERSON_ID, data.personID);
                startActivity(intent);
            }
        });

        for (Person p : Person.people())
            for (int i = 0; i < 10; i++)
                p.newMessage(i++ + " " + p.name,
                        Person.ME, new Date(System.currentTimeMillis() - 10000 * i), i % 2 == 0);
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
                startActivity(new Intent(this, MessagesActivity.class));
                return true;
        }
        return false;
    }
}
