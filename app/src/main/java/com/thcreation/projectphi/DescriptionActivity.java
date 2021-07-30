package com.thcreation.projectphi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.thcreation.projectphi.Intent.GoogleMap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DescriptionActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;

    TextView tvname,tvaddress,tvprice,tvdesciption;
    ImageView img;
    RatingBar ratingBar;

    String destination;
    String pid = "0";
    String name = "0";
    String disc = "0";
    String price = "0";
    String rate = "0";

    Dialog dialog;

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        dialog = new Dialog(this);

        drawerLayout = findViewById(R.id.drawer_layout);
        tvname = findViewById(R.id.tvName);
        tvaddress = findViewById(R.id.tvAddress);
        tvprice = findViewById(R.id.tvPrice);
        tvdesciption = findViewById(R.id.tvDiscription);
        img = findViewById(R.id.img);
        ratingBar = findViewById(R.id.desRating);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

       /// destination = "Kandy";              //get destination from db

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        pid=bundle.getString("pid");
        name=bundle.getString("name");
        disc=bundle.getString("desc");
        price=bundle.getString("price");
        rate=bundle.getString("rate");

        int[] array = new int[]{R.drawable.h1,R.drawable.h2, R.drawable.h3, R.drawable.h4, R.drawable.h5, R.drawable.h6};

        tvname.setText(name);
        tvdesciption.setText(disc);
        tvprice.setText(price);
        img.setImageResource(array[Integer.parseInt(pid)-1]);
        ratingBar.setRating(Float.parseFloat(rate));

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLocation();
    }

    private void loadLocation() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://beezzserver.com/slthadi/projectPHI/address/index.php?pid="+pid+"";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        setLocation(response);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error = "+error.getMessage());
//                Toast.makeText(MainActivity.this,"Error="+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(jsonArrayRequest);
    }

    public void setLocation(JSONArray response){
        List<String> list = new ArrayList<>();
        for(int i=0; i<response.length(); i++){

            try {
                JSONObject obj = response.getJSONObject(i);
                list.add(obj.getString("address"));
                destination=obj.getString("address");
                tvaddress.setText(destination);


            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }


    public void ClickMenu(View view) {
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) { drawerLayout.openDrawer(GravityCompat.START); }

    public void ClickLogo(View view){ closeDrawer(drawerLayout); }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View view){ redirectActivity(this,HomeActivity.class); }
    public void warningDialog(View view){
        dialog.setContentView(R.layout.dialog_warning);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
    public void dismiss(View view){
        dialog.dismiss();
    }

    public static void redirectActivity(Activity activity, Class aClass) {
        Intent intent = new Intent(activity,aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }


    public void trackLocation(View view) {
        findMyLocation();

    }

    private void gotoGooleMap(String source) {

        String sSource = source;
        String sDestination = destination;

        if(sSource.equals("") && sDestination.equals("")){
            Toast.makeText(DescriptionActivity.this, "some location are missing", Toast.LENGTH_SHORT).show();
        }else{
            DisplayTrack(sSource,sDestination);
        }
    }


    private void findMyLocation() {
        if (ActivityCompat.checkSelfPermission(DescriptionActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(DescriptionActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(DescriptionActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100 && grantResults.length > 0 && (grantResults[0] + grantResults[1] ==
                PackageManager.PERMISSION_GRANTED)){
            getCurrentLocation();
        }else{
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {

        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){

            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {

                    Location location = task.getResult();
                    if (location != null) {

                        try {
                            Geocoder geocoder = new Geocoder(DescriptionActivity.this, Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            String mylocation=addresses.get(0).getAddressLine(0);
                            gotoGooleMap(mylocation);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                        LocationRequest locationRequest = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).
                                setInterval(10000).
                                setFastestInterval(1000).
                                setNumUpdates(1);

                        LocationCallback locationCallback = new LocationCallback(){
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                Location location1 = locationResult.getLastLocation();

                                try {
                                    Geocoder geocoder = new Geocoder(DescriptionActivity.this, Locale.getDefault());
                                    List<Address> addresses = geocoder.getFromLocation(location1.getLatitude(), location1.getLongitude(), 1);
                                    String mylocation=addresses.get(0).getAddressLine(0);
                                    gotoGooleMap(mylocation);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        };
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
                    }
                }
            });
        }else{
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).
                    setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    private void DisplayTrack(String sSource, String sDestination) {

        try {
            Uri uri = Uri.parse("https://www.google.co.in/maps/dir/"+sSource+"/"+sDestination);
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.setPackage("com.google.android.apps.maps");

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);


        }catch (ActivityNotFoundException e){
            Uri uri = Uri.parse("http://play.google.com/store/app/details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        }
    }

    public void goFeedback(View view) {
        Intent in = new Intent(this,FeedbackActivity.class);
        in.putExtra("pid",pid);
        in.putExtra("rate",rate);
        startActivity(in);
    }
}