package io.ohalloran.crypto;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import io.ohalloran.crypto.utils.ListAdapter;

public class MessagesActivity extends ActionBarActivity {
    ListView listView;
    TextView emptyText;

    ListAdapter<Message> adapter = new ListAdapter<Message>() {

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View root;
            if (view == null)
                root = getLayoutInflater().inflate(R.layout.single_message, viewGroup, false);
            else
                root = view;
            ImageView pic = (ImageView) root.findViewById(R.id.message_image);
            TextView textView = (TextView) root.findViewById(R.id.message_text);

            //  Message data = getItem(i);


            /*textView.setGravity(side);
            picHost.setForegroundGravity(side
*/
            return root;
        }

        @Override
        public int getCount() {
            return 10;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        listView = (ListView) findViewById(android.R.id.list);
        emptyText = (TextView) findViewById(android.R.id.text1);

        listView.setAdapter(adapter);
        handleVisabilities();

    }

    private void handleVisabilities() {

    }

}
