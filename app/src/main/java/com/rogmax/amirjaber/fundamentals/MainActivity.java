package com.rogmax.amirjaber.fundamentals;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.rogmax.amirjaber.fundamentals.fragments.FragmentSlide;
import com.rogmax.amirjaber.fundamentals.fragments.SingleChoiceClass;
import com.rogmax.amirjaber.fundamentals.providers.NamesProvider;
import com.rogmax.amirjaber.fundamentals.services.GPSTracker;
import com.rogmax.amirjaber.fundamentals.services.MyService;

import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements FragmentSlide.OnFragmentInteractionListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private static final String TAG = "MainActivity";

    // widget
    private TextView textViewHelloAndroid, progressCounter, toastText;
    private EditText editTextName, editTextAge, editTextBundle;
    private FrameLayout fragmentContainer;
    private RelativeLayout toastLayout;
    private ProgressBar progressBar;
    private FloatingActionButton fab;


    // vars
    private final int RECEIVE_SMS_PERMISSION = 10;
    private final int SEND_SMS_PERMISSION = 12;
    private final int CALL_PHONE_PERMISSION = 13;
    private final int LOCATION_REQUEST_CODE = 11;
    public static final int W_EXT_STG_RCD_AUD_PERMISSION_CODE = 14;

    private int day, month, year, hour, minute;
    private int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;
    private int progressStatus = 0;
    private AudioManager audioManager;
    int i = 0;
    String myNom, tempNom;
    String message;
    EditText sms, nom;
    private Handler handler = new Handler();
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    Random random;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";


    // activity created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: created");

        askPermission(Manifest.permission.SEND_SMS, SEND_SMS_PERMISSION);
        askPermission(Manifest.permission.RECEIVE_SMS, RECEIVE_SMS_PERMISSION);
        askPermission(Manifest.permission.CALL_PHONE, CALL_PHONE_PERMISSION);
        askPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_REQUEST_CODE);
        askPermission(WRITE_EXTERNAL_STORAGE, W_EXT_STG_RCD_AUD_PERMISSION_CODE);
        askPermission(RECORD_AUDIO, W_EXT_STG_RCD_AUD_PERMISSION_CODE);

        initViews();
        init();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RECEIVE_SMS_PERMISSION) {
            Log.i(TAG, "onRequestPermissionsResult: yes");
        }

        switch (requestCode) {
            case W_EXT_STG_RCD_AUD_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Storage permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
                }
            case CALL_PHONE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Call permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Call permission denied", Toast.LENGTH_SHORT).show();
                }
            case SEND_SMS_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Sms permission granted (Send)", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Sms permission denied (Send)", Toast.LENGTH_SHORT).show();
                }
            case RECEIVE_SMS_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Sms permission granted (Receive)", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Sms permission denied (Receive)", Toast.LENGTH_SHORT).show();
                }
                break;
            case LOCATION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    // activity about to be visible
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: started");
    }

    // activity is visible
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: resumed");
    }

    // another activity is taking focus
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: paused");
    }

    // activity no longer visible
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: stopped");
    }

    // before activity is destroyed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: destroyed");
    }

    private void initViews() {
        textViewHelloAndroid = findViewById(R.id.tv_hello_android);
        fragmentContainer = findViewById(R.id.fragment_container);
        editTextName = findViewById(R.id.et_name);
        editTextAge = findViewById(R.id.et_age);
        editTextBundle = findViewById(R.id.et_data);
        progressBar = findViewById(R.id.progressBar);
        progressCounter = findViewById(R.id.progressCounter);
        toastLayout = (RelativeLayout) this.getLayoutInflater().inflate(R.layout.toast_custom, (RelativeLayout) findViewById(R.id.relLayoutToast));
        toastText = toastLayout.findViewById(R.id.tvToast);
        fab = findViewById(R.id.fab);


    }

    private void init() {
        textViewHelloAndroid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewHelloAndroid.setTextColor(getRandomColor());
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMail();
            }
        });
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    }


    public void startService(View view) {
        startService(new Intent(getBaseContext(), MyService.class));
    }

    // method to stop the service
    public void stopService(View view) {
        stopService(new Intent(getBaseContext(), MyService.class));
    }

    public void addName(View view) {
        ContentValues values = new ContentValues();

        values.put(NamesProvider.NAME, ((EditText) findViewById(R.id.et_name)).getText().toString());
        values.put(NamesProvider.AGE, ((EditText) findViewById(R.id.et_age)).getText().toString());
        Uri uri = getContentResolver().insert(NamesProvider.CONTENT_URI, values);

        assert uri != null;
        showShortToast(uri.toString());
    }

    public void retrieveUsers(View view) {
        // Retrieve users records
        String URL = "content://com.rogmax.amirjaber.fundamentals.providers.NamesProvider";

        Uri students = Uri.parse(URL);
        Cursor c = managedQuery(students, null, null, null, "name");

        if (c.moveToFirst()) {
            do {
                Toast.makeText(this,
                        c.getString(c.getColumnIndex(NamesProvider._ID)) +
                                ", " + c.getString(c.getColumnIndex(NamesProvider.NAME)) +
                                ", " + c.getString(c.getColumnIndex(NamesProvider.AGE)),
                        Toast.LENGTH_SHORT).show();
            } while (c.moveToNext());
        }
    }

    public void goToFragment(View view) {
        Intent intent = new Intent(MainActivity.this, FragmentActivity.class);
        startActivity(intent);

    }

    public void openActivity(View view) {
        String data = editTextBundle.getText().toString();
        Intent intent = new Intent(getApplicationContext(), BundleActivity.class);
        intent.putExtra("data", data);
        startActivity(intent);
    }

    public void slideFragment(View view) {
        String text = editTextName.getText().toString();
        FragmentSlide fragmentSlide = FragmentSlide.newInstance(text);
        FragmentManager fragmentManager = getSupportFragmentManager();
        @SuppressLint("CommitTransaction") FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.add(R.id.fragment_container, fragmentSlide, "FRAGMENT_SLIDE").commit();
    }

    public void multiFragments(View view) {
        Intent intent = new Intent(MainActivity.this, MultiFragActivity.class);
        startActivity(intent);
    }

    public void alertDialog(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams") View customView = inflater.inflate(R.layout.dialog_custom, null);
        builder.setView(customView);
        builder.setCancelable(true);

        Button okayButton = customView.findViewById(R.id.okay_button);

        final AlertDialog dialog = builder.create();

        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void dateTimeDialog(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams") View customView = inflater.inflate(R.layout.dialog_date_time, null);
        builder.setView(customView);
        builder.setCancelable(true);

        final Button dateButton = customView.findViewById(R.id.dialog_date);
        Button timeButton = customView.findViewById(R.id.dialog_time);

        final AlertDialog dialog = builder.create();

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, MainActivity.this,
                        year, month, day);
                datePickerDialog.show();
            }
        });


        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, MainActivity.this,
                        hour, minute, android.text.format.DateFormat.is24HourFormat(MainActivity.this));
                timePickerDialog.show();
            }
        });

        dialog.show();
    }

    public void progressBar(View view) {
        progressStatus = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 1;
                    handler.post(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            progressBar.setProgress(progressStatus);
                            progressCounter.setText(progressStatus + "/" + progressBar.getMax());
                        }
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    public void listDialog(View view) {
        SingleChoiceClass singleChoiceClass = new SingleChoiceClass();
        singleChoiceClass.show(getSupportFragmentManager(), "list_dialog");
    }

    public void tabFragment(View view) {
        Intent intent = new Intent(getApplicationContext(), TabActivity.class);
        startActivity(intent);
    }

    public void normalToast(View view) {
        showShortToast("This is a normal toast");
    }

    @SuppressLint("SetTextI18n")
    public void customToast(View view) {
        toastText.setText("TOAST");
        toastText.setTextSize(25);

        final Toast t = new Toast(this);
        t.setGravity(Gravity.CENTER | Gravity.BOTTOM, 0, 0);
        t.setDuration(Toast.LENGTH_SHORT);
        t.setView(toastLayout);
        t.show();
    }

    public void normalSnackBar(View view) {
        showSnackBar(view);
    }

    public void customSnackBar(View view) {
        Snackbar.make(view, add(), Snackbar.LENGTH_SHORT).show();
    }

    public void dragDropActivity(View view) {
        addNotification();

        Intent intent = new Intent(MainActivity.this, DragDropActivity.class);
        startActivity(intent);
    }

    public void getLocation(View view) {

        GPSTracker gps = new GPSTracker(getApplicationContext());
        Location l = gps.getLocation();
        if (l != null) {
            double lat = l.getLatitude();
            double lon = l.getLongitude();
            Toast.makeText(this, "Latitude: " + lat + "\nLongitude: " + lon, Toast.LENGTH_SHORT).show();
        }
    }

    public void openMail() {
        Intent intent = new Intent(MainActivity.this, EmailActivity.class);
        startActivity(intent);
    }

    public void sendMessage(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams") View customView = inflater.inflate(R.layout.dialog_send_message, null);
        builder.setView(customView);
        builder.setCancelable(true);

        sms = customView.findViewById(R.id.sms);
        nom = customView.findViewById(R.id.nom);
        Button sendButton = customView.findViewById(R.id.btnSendSMS);

        final AlertDialog dialog = builder.create();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = sms.getText().toString();
                tempNom = nom.getText().toString();
                if (tempNom.matches("")) {
                    myNom = "+380632945376";
                } else {
                    myNom = tempNom;
                }

                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(myNom, null, message, null, null);
                    showShortToast("Message sent!");
                    dialog.dismiss();
                } catch (Exception e) {
                    showShortToast("SMS failed, please try again later!");
                    e.printStackTrace();
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    public void callPhone(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams") View customView = inflater.inflate(R.layout.dialog_call_phone, null);
        builder.setView(customView);
        builder.setCancelable(true);

        nom = customView.findViewById(R.id.nom);
        final Button callButton = customView.findViewById(R.id.btnCallPhone);

        final AlertDialog dialog = builder.create();

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempNom = nom.getText().toString();

                if (tempNom.matches("")) {
                    myNom = "+380632945376";
                } else {
                    myNom = tempNom;
                }

                try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + myNom));
                    showShortToast("Calling!");
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(callIntent);
                    dialog.dismiss();
                } catch (Exception e) {
                    showShortToast("Failed to call, please try again later!");
                    e.printStackTrace();
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    public void goToAnimations(View view) {
        Intent intent = new Intent(MainActivity.this, AnimationActivity.class);
        startActivity(intent);
    }

    public void recordAudio(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams") View customView = inflater.inflate(R.layout.dialog_record_audio, null);
        builder.setView(customView);
        builder.setCancelable(true);

        final Button recordButton = customView.findViewById(R.id.btn_record);
        final Button stopButton = customView.findViewById(R.id.btn_stop);
        final Button playButton = customView.findViewById(R.id.btn_play);
        final Button stopPlayButton = customView.findViewById(R.id.btn_stop_playing);

        final AlertDialog dialog = builder.create();

        stopButton.setEnabled(false);
        playButton.setEnabled(false);
        stopPlayButton.setEnabled(false);

        random = new Random();

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    AudioSavePathInDevice = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                            CreateRandomFileName(5) + "AudioRecording.3gp";

                    MediaRecorderReady();

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    recordButton.setEnabled(false);
                    stopButton.setEnabled(true);

                    showShortToast("Recording started");
                } else {
                    askPermission(WRITE_EXTERNAL_STORAGE, W_EXT_STG_RCD_AUD_PERMISSION_CODE);
                    askPermission(RECORD_AUDIO, W_EXT_STG_RCD_AUD_PERMISSION_CODE);
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaRecorder.stop();
                stopButton.setEnabled(false);
                playButton.setEnabled(true);
                recordButton.setEnabled(true);
                stopPlayButton.setEnabled(false);

                showShortToast("Recording completed");
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws IllegalArgumentException, SecurityException, IllegalStateException {
                stopButton.setEnabled(false);
                recordButton.setEnabled(false);
                stopPlayButton.setEnabled(true);

                mediaPlayer = new MediaPlayer();

                try {
                    mediaPlayer.setDataSource(AudioSavePathInDevice);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();
                showShortToast("Recording playing");
            }
        });

        stopPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopButton.setEnabled(false);
                recordButton.setEnabled(true);
                stopPlayButton.setEnabled(false);
                playButton.setEnabled(true);

                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    MediaRecorderReady();
                }
            }
        });

        dialog.show();
    }

    public void switchMode(View view) {
        i++;

        if (i == 1) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            showShortToast("Now in Vibrate Mode");
        }
        if (i == 2) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            showShortToast("Now in Normal Mode");
        }
        if (i == 3) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            showShortToast("Now in Silent Mode");
        }
        if (i == 4) {
            i = 1;
        }
    }

    public void displayMode(View view) {
        int mod = audioManager.getRingerMode();
        if (mod == AudioManager.RINGER_MODE_VIBRATE) {
            Snackbar.make(view, addString("Vibrate Mode"), Snackbar.LENGTH_SHORT);
        } else if (mod == AudioManager.RINGER_MODE_NORMAL) {
            Snackbar.make(view, addString("Ringing Mode"), Snackbar.LENGTH_SHORT);
        } else {
            Snackbar.make(view, addString("Silent Mode"), Snackbar.LENGTH_SHORT);
        }
    }

    public void MediaRecorderReady() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    public String CreateRandomFileName(int string) {
        StringBuilder stringBuilder = new StringBuilder(string);
        int i = 0;
        while (i < string) {
            stringBuilder.append(RandomAudioFileName
                    .charAt(random.nextInt(RandomAudioFileName.length())));

            i++;
        }
        return stringBuilder.toString();
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    private SpannableStringBuilder add() {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append("   ");
        ImageSpan imageSpan = new ImageSpan(MainActivity.this, R.drawable.ic_action_name);
        spannableStringBuilder.setSpan(imageSpan, spannableStringBuilder.length() - 1, spannableStringBuilder.length(), 0);
        spannableStringBuilder.append("   ");
        spannableStringBuilder.append("Custom SnackBar");

        return spannableStringBuilder;
    }

    private SpannableStringBuilder addString(String message) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append("   ");
        spannableStringBuilder.append(message);

        return spannableStringBuilder;
    }

    private int getRandomColor() {
        Random random = new Random();
        return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    private void askPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            // go ahead and ask for permission
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        } else {
            Toast.makeText(this, "Permission already granted.", Toast.LENGTH_SHORT).show();
        }
    }


    private void addNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "M-CH-ID")
                .setSmallIcon(R.drawable.ic_notf)
                .setContentTitle("This is a notification")
                .setContentText("This is the details");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add the notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.notify(0, builder.build());

    }

    public void showShortToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showSnackBar(View view) {
        Snackbar.make(view, "Normal SnackBar", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onFragmentInteraction(String sendBackText) {
        editTextAge.setText(sendBackText);
        onBackPressed();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        yearFinal = year;
        monthFinal = month;
        dayFinal = dayOfMonth;

        showShortToast(yearFinal + "/" + monthFinal + "/" + dayFinal);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        hourFinal = hourOfDay;
        minuteFinal = minute;

        showShortToast(hourFinal + "/" + minuteFinal);

    }


}
