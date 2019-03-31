package com.example.user.application1;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    static double latitude, longitude;
    float zoomLevel = 16.0f;
    private CircleOptions circleOptions;

    //Home parameters made public
//    public double FTlatitude = 40.4001;
//    public double FTlongitude = 49.8529;



    private final String CHANNEL_ID = "personal_notifications";
    private final int NOTIFICATION_ID = 001;
    private final float RADIUS = 6371;
    private final float my_radius = (float) 0.4;
    private float distance;
    LocationManager locationManager;
    Location location;
    Marker marker;
    public static String currentAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Getting parameters from SETTINGS Activity

        SharedPreferences settings = getSharedPreferences("PREFS", 0);
        Globals.FTlatitude = settings.getFloat("homeLatValue", (float)0.0);
        Globals.FTlongitude = settings.getFloat("homeLongValue", (float)0.0);
        Globals.phoneNumber = settings.getString("phoneNumber", "+994706356325");




        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        final Criteria criteria = new Criteria();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }


                location = locationManager.getLastKnownLocation(locationManager
                        .getBestProvider(criteria, false));

                latitude = location.getLatitude();
                longitude = location.getLongitude();




        //latitude = 40.3595;
        //longitude = 49.8266;

        //Instantiates a new CircleOptions object +  center/radius
        circleOptions = new CircleOptions()
                .center( new LatLng(Globals.FTlatitude, Globals.FTlongitude) ) //HERE
                .radius( 400 )
                .fillColor(0x40ff0000)
                .strokeColor(Color.TRANSPARENT)
                .strokeWidth(2);


        //Navigation (make execution of this part of the code conditional)

        Button homeBttn = (Button) findViewById(R.id.homeBttn);
        homeBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String latMy = String.valueOf(latitude);
                String lngMy = String.valueOf(longitude);

                String myLatHome = String.valueOf(Globals.FTlatitude);
                String myLongHome = String.valueOf(Globals.FTlongitude);
                String destination = myLatHome + ", " + myLongHome;

                //Toast.makeText(this, "Navigation", Toast.LENGTH_SHORT).show();
                String url = "http://maps.google.com/maps?saddr=" + latMy + ","
                        + lngMy + "&daddr=" + destination; //here

                Intent navigation = new Intent(Intent.ACTION_VIEW);
                navigation.setData(Uri.parse(url));

                startActivity(navigation);

            }
        });

        Button homeScreenBttn = (Button)findViewById(R.id.homeScreenBttn);
        homeScreenBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), MainActivity.class);
                //how to pass information to the second screen/activity
                startActivity(startIntent);
            }
        });


        float dlat = (float) (latitude - Globals.FTlatitude);
        float dlon = (float) (longitude - Globals.FTlongitude);
        float a = (float) (Math.pow((Math.sin(dlat / 2)), 2) + Math.cos(latitude) * Math.cos(Globals.FTlatitude) * Math.pow((Math.sin(dlon / 2)), 2));
        float c = (float) (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
        float d = RADIUS * c;

        distance = d;

        final Handler ha=new Handler();
        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
                //call function
                location = locationManager.getLastKnownLocation(locationManager
                        .getBestProvider(criteria, false));

                latitude = location.getLatitude();
                longitude = location.getLongitude();

                float dlat = (float) (latitude - Globals.FTlatitude);
                float dlon = (float) (longitude - Globals.FTlongitude);
                float a = (float) (Math.pow((Math.sin(dlat / 2)), 2) + Math.cos(latitude) * Math.cos(Globals.FTlatitude) * Math.pow((Math.sin(dlon / 2)), 2));
                float c = (float) (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
                float d = RADIUS * c;

                distance = d;

                proximityAlert();

                ha.postDelayed(this, 10000 );
            }
        }, 10000);

        //proximityAlert();

        Button contact = (Button)findViewById(R.id.contactBttn);

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendWhatsAppMessage();

            }
        });


    }

    private void proximityAlert(){
        if (distance > my_radius) {

            displayNotification();
            Toast.makeText(getApplicationContext(), "You're exiting your area", Toast.LENGTH_LONG).show();

        }
    }

    private void displayNotification() {
        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_sms_notification);
        builder.setContentTitle("Simple Notification");
        builder.setContentText("This is a simple notification..");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());

    }

    private void createNotificationChannel() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            CharSequence name = "Personal Notifications";
            String description = "Include all the personal notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);

            notificationChannel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }



    public static void getAddress(Context context, double LATITUDE, double LONGITUDE) {

        //Set Address
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);

            if (addresses != null && addresses.size() > 0) {



                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                //String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                currentAddress = address + " " + city + " " + " " + state + " " + country + " " + knownName;


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }


    public void sendWhatsAppMessage() {
        PackageManager packageManager = this.getPackageManager();
        Intent i = new Intent(Intent.ACTION_VIEW);
        String phone = Globals.phoneNumber;
        getAddress(this, 40.4001, 49.8529);
        String message = currentAddress + "\n" + "Sent from Mnemosyne";;

        try {
            String url = "https://api.whatsapp.com/send?phone="+ phone +"&text=" + URLEncoder.encode(message, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                this.startActivity(i);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    /**public void sendWhatsAppMessage() {

        PackageManager pm = getPackageManager();




        try {

            Uri uri = Uri.parse("+994706356325");

            Intent i = new Intent(Intent.ACTION_SENDTO, uri);
            i.setType("text/plain");


            getAddress(this, latitude, longitude);
            String text = currentAddress + "\n" + "Sent from Mnemosyne";

            PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            i.setPackage("com.whatsapp");

            i.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(i, "Share using"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, currentAddress, Toast.LENGTH_SHORT)
                    .show();
        }

    }*/


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);


        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        MarkerOptions yourMarkerOptions;
        Marker yourMarker;


        yourMarkerOptions = new MarkerOptions();
        yourMarkerOptions.title("My Location");
        yourMarkerOptions.snippet("");
        yourMarkerOptions.position(new LatLng(latitude, longitude));
        //Set your marker icon using this method.
        //yourMarkerOptions.icon();


        //float zoomLevel = 16.0f; //This goes up to 21

        marker = mMap.addMarker(yourMarkerOptions);

        Circle circle = mMap.addCircle(circleOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), zoomLevel));

        Button zoomOut = (Button) findViewById(R.id.zoomOut);
        zoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                zoomLevel--;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), zoomLevel));

            }
        });

      final Handler handler1 = new Handler();
       Runnable runnable = new Runnable() {
            @Override
            public void run() {
                onLocationChanged(location);
                handler1.postDelayed(this, 1000);
            }
        };

//Start
        handler1.postDelayed(runnable, 1000);



    }



    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0, this);

        //Place current location marker
        LatLng latLng = new LatLng(latitude, longitude);
        marker.remove();


       // if(marker!=null){
           // marker.setPosition(latLng);
       // }else{
            marker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .title("I am here"));
       // }


        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
    }



    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

