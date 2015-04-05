package io.ohalloran.crypto;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.ohalloran.crypto.coding.Cryption;
import io.ohalloran.crypto.data.Message;
import io.ohalloran.crypto.data.ParseFactory;
import io.ohalloran.crypto.data.Person;

public class MessagesActivity extends ActionBarActivity implements View.OnFocusChangeListener, View.OnClickListener, ParseFactory.OnParseUpdateListener {
    public static String PERSON_ID = "person_id";

    ListView listView;

    EditText messageInput;
    ImageButton imageSource;
    Button sendButton;

    ListAdapter<Message> adapter;
    Person recep;
    Integer images[] = new Integer[]{R.drawable.babyanimal, R.drawable.bunnypuppy, R.drawable.kitten,
            R.drawable.mr_krabs, R.drawable.puppies, R.drawable.sandy,
            R.drawable.spongebob, R.drawable.squidward, R.drawable.toofar};


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
        ArrayList<Message> filtered = new ArrayList<>();
        List<Message> check = ParseFactory.getMessages();
        if (recep == null) {
            filtered = (ArrayList) check;
        } else {
            for (Message i : check) {
                if (recep == ParseFactory.getLocalUser()) {
                    filtered.add(i);
                }
            }
        }
        Collections.sort(filtered); //TODO filter
        adapter = new ListAdapter<Message>(filtered) {
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
                pic.setImageBitmap(extractBitmap(data));
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

    private Bitmap extractBitmap(Message m) {
        return BitmapFactory.decodeByteArray(m.getImageData(), 0,
                m.getImageData().length, new BitmapFactory.Options());
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
                getImage();
                break;
            case R.id.send_button:
                //send the message
                String msg = messageInput.getText().toString();
                Bitmap img = ((BitmapDrawable) imageSource.getDrawable()).getBitmap();
                //TODO send for encoding
                ParseFactory.sendMessage(img, msg);
                messageInput.setText(null);
                break;
        }
    }

    @Override
    public void onComplete() {
        adapter.updateData(ParseFactory.getMessages());
    }

    private void getImage() {
        final ListAdapter<Integer> adapter = new ListAdapter<Integer>(images) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = convertView != null ? convertView :
                        getLayoutInflater().inflate(R.layout.message_reveal, parent, false);
                ImageView img = (ImageView) view.findViewById(R.id.source_image);
                img.setImageResource(images[position]);
                return view;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                imageSource.setImageResource(images[i]);
            }
        });
        builder.show();
    }
}
