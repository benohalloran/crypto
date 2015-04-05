package io.ohalloran.crypto;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import io.ohalloran.crypto.coding.Cryption;
import io.ohalloran.crypto.data.Message;
import io.ohalloran.crypto.data.Person;
import io.ohalloran.crypto.parse.ParseFactory;
import io.ohalloran.crypto.utils.ListAdapter;

public class MessagesActivity extends ActionBarActivity implements View.OnFocusChangeListener, View.OnClickListener, ParseFactory.OnParseUpdateListener {
    public static String PERSON_ID = "person_id";

    ListView listView;
    TextView emptyText;
    EditText messageInput;
    ImageButton imageSource;
    Button sendButton;

    ListAdapter<Message> adapter;
    Person recep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        listView = (ListView) findViewById(android.R.id.list);
        messageInput = (EditText) findViewById(R.id.message_input);
        imageSource = (ImageButton) findViewById(R.id.image_mask);
        sendButton = (Button) findViewById(R.id.send_button);

        messageInput.setOnFocusChangeListener(this);
        imageSource.setOnClickListener(this);
        sendButton.setOnClickListener(this);

        try {
            recep = ParseFactory.getByID(getIntent().getExtras().getString(PERSON_ID));
            setTitle(recep.userName());
        } catch (Exception e) {
            Log.wtf("Message Activity", "Error loading clicked", e);
        }
        ParseFactory.refresh(this);
        Collections.sort(ParseFactory.getMessages()); //TODO filter
        adapter = new ListAdapter<Message>(ParseFactory.getMessages()) {
            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                View root = view;
                if (root == null)
                    root = getLayoutInflater().inflate(R.layout.single_message, viewGroup, false);
                ImageView pic = (ImageView) root.findViewById(R.id.message_image);
                TextView textView = (TextView) root.findViewById(R.id.message_text);
                Message data = getItem(i);
                int gravity = ParseFactory.getPersonWhoPosted(data).equals(ParseFactory.getLocalUser())
                        ? Gravity.LEFT : Gravity.RIGHT;
                ((LinearLayout.LayoutParams) pic.getLayoutParams()).gravity = gravity;


                textView.setText(data.getUpdatedAt().toString());
                textView.setGravity(gravity);

//                pic.forceLayout();
//                textView.forceLayout();
                return root;
            }

            @Override
            public void updateData(List<Message> messages) {
                Collections.sort(messages);
                super.updateData(messages);
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Message data = (Message) listView.getAdapter().getItem(i);
                View popup = getLayoutInflater().inflate(R.layout.message_reveal, null, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(MessagesActivity.this)
                        .setView(popup).setCancelable(true);
                Bitmap img = BitmapFactory.decodeByteArray(data.getImageData(), 0,
                        data.getImageData().length, new BitmapFactory.Options());
               ((TextView) popup.findViewById(R.id.decrypted_msg)).setText(Cryption.mobiDecode(img));
                ((ImageView) popup.findViewById(R.id.source_image))
                        .setImageBitmap(img);
                builder.show();

            }
        });
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

    @Override
    public void onComplete() {
        adapter.updateData(ParseFactory.getMessages());
    }
}
