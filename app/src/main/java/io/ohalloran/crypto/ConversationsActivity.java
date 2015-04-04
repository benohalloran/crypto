package io.ohalloran.crypto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

//single conversation
public class ConversationsActivity extends ActionBarActivity {
    ListView listView;
    TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);
        listView = (ListView) findViewById(android.R.id.list);
        emptyText = (TextView) findViewById(android.R.id.text1);
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
