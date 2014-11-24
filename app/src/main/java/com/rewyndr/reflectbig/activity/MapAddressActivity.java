package com.rewyndr.reflectbig.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rewyndr.reflectbig.R;
import com.rewyndr.reflectbig.model.AddressLocation;

import java.io.IOException;
import java.util.List;

public class MapAddressActivity extends FragmentActivity implements LocationListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private int fence;
    String CURRENT_CLASS = this.getClass().getName();
    private AddressLocation loc = null;
    private Location current = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_address);
        setUpMapIfNeeded();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                addMarker(point);
                loc = new AddressLocation(point.latitude, point.longitude, fence);
                getAddressFromLocation(loc);
                EditText add = (EditText) findViewById(R.id.editText);
                add.setText(loc.getAddress());
            }
        });
    }

    private void cleanMap() {
        mMap.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        getMyLocation();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
        }
    }

    /**
     * This method helps in picking up the location of the user via GPS or Network.
     */
    private void getMyLocation() {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        Log.d(CURRENT_CLASS, "" + service.getProviders(true));
        boolean isGPSEnabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = isConnectedToInternet(this);
        Log.d(CURRENT_CLASS, "Internet Accessible : " + isNetworkEnabled);
        Log.d(CURRENT_CLASS, "GPS Accessible : " + isGPSEnabled);
        if(isNetworkEnabled) {
            Log.d(CURRENT_CLASS, "Fetching location from best possible option");
            LatLng myLocation = null;
            if (current != null) {
                myLocation = new LatLng(current.getLatitude(),
                        current.getLongitude());
                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(myLocation, 5);
                mMap.animateCamera(yourLocation);
            } else {
                service.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);
            }
        } else {
            Log.d(CURRENT_CLASS, "Fetching location from GPS");
            service.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 100, this);
        }
    }

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    /**
     * This method is used from getting the location information in form of human readable address.
     * @param current
     * @return
     */
    private void getAddressFromLocation(AddressLocation current) {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(getApplicationContext());
        Log.d(CURRENT_CLASS, "Geocoder : " + geocoder);
        try {
            addresses = geocoder.getFromLocation(current.getLatitude(), current.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String address = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getAddressLine(1);
        String country = addresses.get(0).getAddressLine(2);
        String fullAddress = address + "," + city + "," + country;
        loc.setAddress(fullAddress);
        loc.setShortAddress(address);
    }

    /**
     * This method is used to add a marker to the current location which can help in identifying the address of the user's position.
     */
    private void addMarker(LatLng point) {
        cleanMap();
        if (null != mMap) {
            mMap.addMarker(new MarkerOptions()
                    .position(point).draggable(true));
            fence = Integer.valueOf(((EditText) findViewById(R.id.editText2)).getText().toString());
            if (fence == 0) {
                fence = 1000;
            }
            mMap.addCircle(new CircleOptions().center(point)
                    .radius(fence)
                    .strokeColor(Color.GREEN)
                    .fillColor(Color.WHITE));
        }
    }

    /**
     * An utility method which helps to focus on the current location of the user in the Map which is rendered.
     * @param currentLocation
     */
    private void moveToTheAddress(AddressLocation currentLocation) {
        CameraUpdate center =
                CameraUpdateFactory.newLatLng(new LatLng(currentLocation.getLatitude(),
                        currentLocation.getLongitude()));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
    }

    public void locateMe(View view) {
        String strAddress = ((EditText) findViewById(R.id.editText)).getText().toString();
        Geocoder coder = new Geocoder(getApplicationContext());
        List<Address> address;
        try {
            address = coder.getFromLocationName(strAddress,1);
            if (address == null) {
                Toast.makeText(this, "Wrong Address! Try again", Toast.LENGTH_SHORT).show();
            } else {
                Address location = address.get(0);
                double lat = (double) (location.getLatitude());
                double longi = (double) (location.getLongitude());
                LatLng point = new LatLng(lat, longi);
                addMarker(point);
                loc = new AddressLocation(point.latitude, point.longitude, strAddress, fence);
                loc.setShortAddress(strAddress.split(",")[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map_address, menu);
        menu.findItem(R.id.use).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.use) {
            if(loc == null) {
                Toast.makeText(getApplicationContext(), "Lookup location in the map", Toast.LENGTH_SHORT).show();
            } else {
                Intent i = new Intent();
                loc.setAddress(((EditText) findViewById(R.id.editText)).getText().toString());
                i.putExtra("address", loc);
                setResult(Activity.RESULT_OK, i);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
