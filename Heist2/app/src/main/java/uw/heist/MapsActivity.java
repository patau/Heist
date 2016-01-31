/*THIS IS THE RIGHT ONE*/
package uw.heist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.HandlerThread;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.os.Vibrator;
import android.widget.TextView;
import android.os.CountDownTimer;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int timeMillis;
    private String userName;
    private String role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int minRemain = 10;
        double secRemain = 59;
        this.timeMillis = 0;
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        this.userName = intent.getStringExtra("USERNAME");
        this.role = intent.getStringExtra("ROLE");
        System.out.println("now in the map. My username is " + this.userName);

        setContentView(R.layout.activity_maps);
//        TextView timer = (TextView) findViewById(R.id.timer);
//        timer.setText(Integer.toString(minRemain) + secRe);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 5000;

        Intent alarmIntent = new Intent(MapsActivity.this, LocReciever.class);

        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 2014, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    }


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

//        mMap.setMyLocationEnabled(true);
        // Add coords for UW Red Square; moves camera there, zooms
        // No marker but shows radius
        LatLng sydney = new LatLng(47.656056, -122.309503);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(17.0f));
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        // Instantiates a new CircleOptions object and defines the center and radius
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(47.656056, -122.309503))
                .fillColor(0x30ff0000)
                .strokeColor(0x30ff0000)
                .radius(350);

// Add radius circle to map
        Circle circle = mMap.addCircle(circleOptions);

        StartupActivity.userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.child("users").getChildren()) {
                    if (data.hasChild("guard")) { //the user is a guard.
                        String s = data.child("guard").toString();
                        Scanner scanner = new Scanner(s);
                        String latString = "";
                        String lngString = "";
                        while (scanner.hasNext()) {
                            String d = scanner.next();
                            if (d.startsWith("{0")) {
                                d = d.substring(3);
                                d = d.substring(0, d.length() - 1);
                                latString = d;
                                lngString = scanner.next();
                                lngString = lngString.substring(2, lngString.length() - 1);
                            }
                        }
                        double lat =Double.parseDouble(latString);
                        double lng = Double.parseDouble(lngString);
                        //draw a marker at lat,lng
                        System.out.println(data.child("guard").toString());
                        LatLng person = new LatLng(lat, lng);
                        mMap.addMarker(new MarkerOptions().position(person).title("guard"));
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }



    public void comeHere(View view) {
//        Vibrator v = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
//        // Vibrate for 500 milliseconds
//        v.vibrate(500);
    }
}
//    public void countDown(View view) {
//        Timer timer = new Timer();
//        my
//    }

//    CountDownTimer timer = new CountDownTimer(30000, 1000) {
//
//        public void onTick(long millisUntilFinished) {
//            timer.setText("seconds remaining: " + millisUntilFinished / 1000);
//            //here you can have your logic to set text to edittext
//        }
//
//        public void onFinish() {
//            timer.setText("done!");
//        }
//
//    }.start();
//}
