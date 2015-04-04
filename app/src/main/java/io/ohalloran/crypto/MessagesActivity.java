package io.ohalloran.crypto;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import io.ohalloran.crypto.utils.ListAdapter;

public class MessagesActivity extends ActionBarActivity implements View.OnFocusChangeListener, View.OnClickListener {
    ListView listView;
    TextView emptyText;
    EditText messageInput;
    ImageButton imageSource;
    Button sendButton;

    ListAdapter<String> adapter = new ListAdapter<String>(new String[]{"one", "two", "three"}) {
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View root;
            if (view == null)
                root = getLayoutInflater().inflate(R.layout.single_message, viewGroup, false);
            else
                root = view;
            ImageView pic = (ImageView) root.findViewById(R.id.message_image);
            TextView textView = (TextView) root.findViewById(R.id.message_text);
            int gravity = i % 2 == 0 ? Gravity.LEFT : Gravity.RIGHT;
            ((LinearLayout.LayoutParams) pic.getLayoutParams()).gravity = gravity;
            textView.setGravity(gravity);
            return root;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(adapter);

        messageInput = (EditText) findViewById(R.id.message_input);
        imageSource = (ImageButton) findViewById(R.id.image_mask);
        sendButton = (Button) findViewById(R.id.send_button);

        messageInput.setOnFocusChangeListener(this);
        imageSource.setOnClickListener(this);
        sendButton.setOnClickListener(this);
    }


    @Override
    public void onFocusChange(View view, boolean b) {
        if (view.getId() == R.id.message_input) {
            messageInput.setLines(b ? 3 : 1);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_mask:
                //get the image
                break;
            case R.id.send_button:
                //send the message
                String msg = messageInput.getText().toString();
                Bitmap img = ((BitmapDrawable) imageSource.getDrawable()).getBitmap();
                //TODO send for encoding
                break;
        }
    }
}
