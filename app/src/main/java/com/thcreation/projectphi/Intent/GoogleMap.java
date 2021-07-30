package com.thcreation.projectphi.Intent;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.thcreation.projectphi.DescriptionActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GoogleMap extends AppCompatActivity {

    FusedLocationProviderClient fusedLocationProviderClient;

    public Context acContext;

    public String finalLoc;

    private void gotoGooleMap(String source,String destination) {


        String sSource = source;
        String sDestination = destination;

        if(sSource.equals("") && sDestination.equals("")){
            Toast.makeText(acContext, "some location are missing", Toast.LENGTH_SHORT).show();
        }else{
            DisplayTrack(sSource,sDestination);
        }
    }


    public void findMyLocation(String destination, Context context) {

        acContext =context;
        finalLoc = destination;

        if (ActivityCompat.checkSelfPermission(acContext,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(acContext,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions((Activity) acContext,
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
            Toast.makeText(acContext, "Permission denied", Toast.LENGTH_SHORT).show();
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
                            Geocoder geocoder = new Geocoder(acContext, Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            String mylocation=addresses.get(0).getAddressLine(0);
                            gotoGooleMap(mylocation,finalLoc);

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
                                    Geocoder geocoder = new Geocoder(acContext, Locale.getDefault());
                                    List<Address> addresses = geocoder.getFromLocation(location1.getLatitude(), location1.getLongitude(), 1);
                                    String mylocation=addresses.get(0).getAddressLine(0);
                                    gotoGooleMap(mylocation,finalLoc);

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
}
