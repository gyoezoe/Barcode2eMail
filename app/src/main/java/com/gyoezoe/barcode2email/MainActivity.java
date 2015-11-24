package com.gyoezoe.barcode2email;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "Preferences";

    private EditText eMail;
    private EditText subject;
    private TextView editBody;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        eMail = (EditText) findViewById(R.id.editEmail);
        editBody = (TextView) findViewById(R.id.editBody);
        subject = (EditText) findViewById(R.id.editSubject);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME,0);
        eMail.setText(prefs.getString("eMail", ""));
        subject.setText(prefs.getString("subject", ""));

        final Button buttonScan = (Button) findViewById(R.id.buttonScan);
        buttonScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                startActivityForResult(intent, 0);
            }
        });


        final Button buttonSend = (Button) findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setPackage("com.google.android.gm");
                i.setType("text/html");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{eMail.getText().toString()});
                i.putExtra(Intent.EXTRA_SUBJECT, "Barcode2eMail - " + subject.getText());
                i.putExtra(Intent.EXTRA_TEXT, editBody.getText());
                try {
                    startActivity(i);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStop(){
        super.onStop();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("eMail", eMail.getText().toString());
        editor.putString("subject", subject.getText().toString());
        editor.apply();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                editBody.append(contents + "\n");
            } else if (resultCode == RESULT_CANCELED) {
            }
            editBody.requestFocus();
        }
    }

}
