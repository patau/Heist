package uw.heist;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.firebase.client.*;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StartupActivity extends AppCompatActivity {
    public static final int gameID = 12345;
    private Map<String, Boolean> persons;
    public static Firebase userRef;
    public String name = "";
    public boolean isTheif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        userRef = new Firebase("https://sizzling-heat-2181.firebaseio.com/");
        if (userRef.child("users") == null) {
            List<Person> users = new ArrayList<>();
            userRef.setValue(users);
        }
        setContentView(R.layout.activity_startup);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_startup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void makeGuard(View view) {
        EditText box = (EditText) findViewById(R.id.name);
        String name = box.getText().toString();
        this.name = name;
        this.isTheif = false;
       // Map<String, Map<String, Boolean>> users = new HashMap<String, Map<String, Boolean>>();
       // users.put(this.name, PersonMap);
       // userBase.setValue(users);
    }

    public void makeThief(View view) {
        EditText box = (EditText) findViewById(R.id.name);
        String name = box.getText().toString();
        this.name = name;
        this.isTheif = true;

    }

    public void startGame(View view) {

        GPSTracker gps = new GPSTracker(this);
        if(gps.canGetLocation()){
            double lat = gps.getLatitude();
            double lng = gps.getLongitude();

            List<Double> points = new ArrayList<>();
            Map<String, List<Double>> person = new HashMap<>();
            String s;
            if (this.isTheif) {
                s = "theif";
            } else {
                s = "guard";
            }
            points.add(lat);
            points.add(lng);
            person.put(s, points);
            Firebase user = userRef.child("users").push();
            String userName = user.getKey();
            System.out.println("user = " + userName);
            user.setValue(person);

            Log.d("GPS", lat + "" + lng);
            Intent intent = new Intent(this.getApplicationContext(), MapsActivity.class);
            intent.putExtra("USERNAME", userName);
            intent.putExtra("ROLE", s);
            startActivity(intent);
        } else {
            throw new IllegalStateException("location services are not enabled");
        }
    }
}
