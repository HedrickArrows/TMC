package com.example.michal.tmc;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.text.DateFormat;
import android.os.Environment;
import java.util.Date;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import android.database.Cursor;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import java.io.ByteArrayOutputStream;
import java.util.Dictionary;

public class AddTree extends AppCompatActivity {

    Location location;
    String nazwa_zbioru;
    String lon = "";
    String lat = "";
    String IMAGE = "";
    Dictionary<String, String> dict;
    private LocationManager mLocationManager;
    SQLiteDatabase db;
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {

        }
        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    String[] columnNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tree);
        Toolbar toolbar = findViewById(R.id.toolbar);
        Bundle bundle = getIntent().getExtras();
        nazwa_zbioru = bundle.getString("message");
        toolbar.setTitle(nazwa_zbioru + ": Dodaj drzewo");
        setSupportActionBar(toolbar);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                    7 );
        }
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED ) {
            final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();
            }
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                // mLocationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, mLocationListener, null);
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, mLocationListener);
            }
        }
        db = openOrCreateDatabase("TREES",MODE_PRIVATE,null);

        Cursor dbCursor = db.query(nazwa_zbioru, null, null, null, null, null, null);
        columnNames = dbCursor.getColumnNames();

        if(columnNames.length > 4){
            LinearLayout layout = (LinearLayout) findViewById(R.id.textBoxContainer);
            for(int i = 4; i< columnNames.length; i++) {
                EditText e = new EditText(this);
                e.setId(View.generateViewId());
                e.setPadding(20, 20, 20, 20);
                e.setMinimumWidth(500);
                e.setHint(columnNames[i]);
                layout.addView(e);
            }
        }

    }

    public void setLocation(View view)
    {
        TextView latitude = findViewById(R.id.latitudeField);
        TextView longitude = findViewById(R.id.longitudeField);

        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED ) {
            location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null) {
                latitude.setText(String.valueOf(location.getLatitude()));
                longitude.setText(String.valueOf(location.getLongitude()));
                lon = String.valueOf(location.getLongitude());
                lat = String.valueOf(location.getLatitude());
            }
        }


    }



    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 0, outputStream);
        return outputStream.toByteArray();
    }

    static final int REQUEST_TAKE_PHOTO = 1;


    private Uri imgUri;
    private String photoDir;

    public void addPhoto(View view)
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, REQUEST_TAKE_PHOTO);
        }

        if ( ContextCompat.checkSelfPermission( this, Manifest.permission.CAMERA )
                == PackageManager.PERMISSION_GRANTED ) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

               // if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
               // {
               //     ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_TAKE_PHOTO);
               //     if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
               //         return;
               //}

                //File imagesFolder = new File(Environment.getExternalStorageDirectory(), "TMC_Images");
                //imagesFolder.mkdirs();

                //File photoFile = new File(imagesFolder, "JPEG_image"+
                //        new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg");
                //photoDir = photoFile.getAbsolutePath();

                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                //FileProvider.getUriForFile(this,"com.example.android.fileprovider", photoFile);
                //imgUri = FileProvider.getUriForFile(this,
                //        BuildConfig.APPLICATION_ID + ".provider",
                //        photoFile);
                        // Uri.fromFile(photoFile);
                //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);


                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

            }
        }
    }

    private Bitmap bmp;
    private Bitmap fullImg;
    final int thumbSize = 256;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            try {
                //fullImg = BitmapFactory.decodeStream(getContentResolver().openInputStream(imgUri));
                //zakomentowana próba podejścia lepiej do zdjęcia, nie polecam
                Bundle extras = data.getExtras();
                bmp = (Bitmap) extras.get("data");

                ImageView img = (ImageView) findViewById(R.id.thumb);
                //bmp = ThumbnailUtils.extractThumbnail(fullImg,
                //        thumbSize, thumbSize);
                img.setImageBitmap(bmp);
            } catch (Exception e){
                Log.e("photo display", e.getMessage(),e);
            }
        }
    }


    public void addToDb(View view)
    {
        byte[] imgBitmap = getBitmapAsByteArray(bmp);

        String cols = "", vals = "";

        if(columnNames.length > 4){
            LinearLayout layout = (LinearLayout) findViewById(R.id.textBoxContainer);
            for(int i = 4; i< columnNames.length; i++) {

                cols += ", " + columnNames[i];

                EditText text = (EditText) layout.getChildAt(i - 4);
                vals += "', '" + text.getText();
            }
        }


        db.execSQL("INSERT INTO " + nazwa_zbioru +" (LON, LAT, IMAGE" + cols +
                ") values ('"+ lon +"','"+lat +"', '"+ imgBitmap + vals +"')");
        finish();

    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.NO_WRAP);
        return temp;
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.NO_WRAP);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }



}
