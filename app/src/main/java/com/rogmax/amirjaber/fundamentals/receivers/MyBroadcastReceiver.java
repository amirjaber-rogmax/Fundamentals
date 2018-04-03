package com.rogmax.amirjaber.fundamentals.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


/**
 * Created by Amir Jaber on 3/1/2018.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Broadcast receiver triggered", Toast.LENGTH_SHORT).show();

    }


}
