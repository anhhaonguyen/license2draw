package control.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by doba on 2/22/17.
 */

public class LocationController implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = "LocationController";
    private static final int PERMISSION_LOCATION = 100;

    public interface LocationControllerListener {
        public void onFail();

        public void onLocationChanged(Location location);
    }

    private LocationControllerListener locationControllerListener;

    private Context context;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;

    public LocationController(Context context) {
        this.context = context;
        initGoogleAPIClient(context);
    }

    public void setLocationListener(LocationControllerListener locationListener) {
        this.locationControllerListener = locationListener;
    }

    private void initGoogleAPIClient(Context context) {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        createLocationRequest();
    }


    /**
     * Create location request object
     */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private boolean checkPermission(String str) {
        int result = ContextCompat.checkSelfPermission(context, str);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void loadLocationService() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            locationControllerListener.onLocationChanged(mLastLocation);
            Log.e(TAG, String.format("%f - %f",mLastLocation.getLatitude(),mLastLocation.getLongitude()));
        }
        else{
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);

        }
    }

    private void callPermissionUI() {
        if (!checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)){
            ActivityCompat.requestPermissions((Activity)context, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
        } else{
            loadLocationService();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_LOCATION:
                loadLocationService();
            default:
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.v(TAG, "onConnected ");
        callPermissionUI();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        locationControllerListener.onFail();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        locationControllerListener.onLocationChanged(location);
    }

    public void connect(){
        mGoogleApiClient.connect();
    }

    public void disconnect(){
        mGoogleApiClient.disconnect();
    }
}
