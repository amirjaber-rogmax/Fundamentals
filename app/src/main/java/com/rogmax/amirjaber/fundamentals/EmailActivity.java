package com.rogmax.amirjaber.fundamentals;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EmailActivity extends AppCompatActivity {

    //widgets
    private EditText emailTo;
    private EditText mSubject;
    private EditText mMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        emailTo = findViewById(R.id.et_to);
        mSubject = findViewById(R.id.et_subject);
        mMessage = findViewById(R.id.et_message);


    }

    public void sendEmail(View view) {
        String recipientList = emailTo.getText().toString();
        String[] recipients = recipientList.split(",");
        String subject = mSubject.getText().toString();
        String message = mMessage.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an Email Client ( ͡° ͜ʖ ͡°)"));

    }

}
