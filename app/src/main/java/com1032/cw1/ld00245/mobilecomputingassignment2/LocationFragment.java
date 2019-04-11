package com1032.cw1.ld00245.mobilecomputingassignment2;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.location.LocationListener;
import android.location.LocationManager;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;


/**
 * Created by PunLife on 30/04/2016.
 */
public class LocationFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private SupportMapFragment mSupportMapFragment;

    public static LocationFragment newInstance() {
        LocationFragment fragment = new LocationFragment();
        return fragment;
    }

    //Server Locations
    private static final String BR = "Sao Paulo, Brazil";
    private static final String EUW = "Amsterdam, Netherlands";
    private static final String EUNE = "Amsterdam, Netherlands";
    private static final String LAN = "Miami, FL, United States";
    private static final String LAS = "Santiago, Chile";
    private static final String NA = "Chicago, Illinois, United States";
    private static final String OCE = "Sidney, Australia";
    private static final String RU = "Moscow, Russia";
    private static final String TR = "Istanbul, Turkey";
    private static final String JP = "Tokyo, Japan";
    private String[] servers = new String[]{BR, EUW, EUNE, LAN, LAS, NA, OCE, RU, TR, JP};
    private String[] serverNames = new String[]{"BR", "EUW", "EUNE", "LAN", "LAS", "NA", "OCE", "RU", "TR", "JP"};
    private GoogleApiClient mGoogleApiClient;
    private Geocoder geocoder;
    private Location lastLocation;
    private LatLng selectedMarkLatLang;
    private CameraPosition cameraPosition;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private DecimalFormat df = new DecimalFormat("0.00");
    private Polyline polyline;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.location_fragment, container, false);
        lastLocation = new Location("Summoner Location");
        geocoder = new Geocoder(getContext(), Locale.getDefault());
        selectedMarkLatLang = new LatLng(0, 0);
        final Button calculate = (Button) rootView.findViewById(R.id.calculateButton);
        Button fetch = (Button) rootView.findViewById(R.id.fetchButton);
        final TextView address1 = (TextView) rootView.findViewById(R.id.address1);
        final TextView address2 = (TextView) rootView.findViewById(R.id.address2);
        final TextView address3 = (TextView) rootView.findViewById(R.id.address3);
        final TextView server = (TextView) rootView.findViewById(R.id.selectedServer);
        final TextView serverCoordLat = (TextView) rootView.findViewById(R.id.serverCoordLat);
        final TextView serverCoordLong = (TextView) rootView.findViewById(R.id.serverCoordLong);
        final TextView distance = (TextView) rootView.findViewById(R.id.distance);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        try {
            lastLocation = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        if (lastLocation != null) {
            try {
                address1.setText(geocoder.getFromLocation(lastLocation.getLatitude(), lastLocation.getLongitude(), 1).get(0).getAddressLine(0));
                address2.setText(geocoder.getFromLocation(lastLocation.getLatitude(), lastLocation.getLongitude(), 1).get(0).getAddressLine(1));
                address3.setText(geocoder.getFromLocation(lastLocation.getLatitude(), lastLocation.getLongitude(), 1).get(0).getAddressLine(2));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean check = displayGPSStatus();
                if (check) {
                    locationListener = new CustomLocationListener();

                    try {
                        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, Looper.myLooper());
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(getActivity(), "Please turn your GPS on.", Toast.LENGTH_SHORT);
                }
            }
        });

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapwhere);
        if (mSupportMapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mSupportMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.mapwhere, mSupportMapFragment).commit();
        }

        if (mSupportMapFragment != null) {
            mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {

                @Override
                public void onMapReady(final GoogleMap googleMap) {
                    if (googleMap != null) {
                        calculate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (selectedMarkLatLang != null && lastLocation != null && !server.getText().equals("Selected Server:")) {
                                    if (polyline != null) {
                                        polyline.remove();
                                    }
                                    Location summonerLocation = new Location("A");
                                    summonerLocation.setLatitude(lastLocation.getLatitude());
                                    summonerLocation.setLongitude(lastLocation.getLongitude());
                                    Location serverLocation = new Location("B");
                                    serverLocation.setLatitude(selectedMarkLatLang.latitude);
                                    serverLocation.setLongitude(selectedMarkLatLang.longitude);
                                    double dist = (summonerLocation.distanceTo(serverLocation) / 1000f);
                                    distance.setText("Distance to server: " + df.format(dist).toString() + "km");
                                    PolylineOptions polylineOptions = new PolylineOptions().add(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()))
                                            .add(new LatLng(serverLocation.getLatitude(), serverLocation.getLongitude())).width(6f).color(Color.RED);
                                    polyline = googleMap.addPolyline(polylineOptions);
                                } else {
                                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            /**
                             * Changes the UI based on the marker locations
                             * @param marker
                             * @return
                             */
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                if (!marker.getTitle().equals("Current Location")) {
                                    server.setText(marker.getSnippet());
                                    serverCoordLat.setText("Latitude: " + marker.getPosition().latitude + "");
                                    serverCoordLong.setText("Longitude: " + marker.getPosition().longitude + "");
                                    selectedMarkLatLang = marker.getPosition();
                                    distance.setText("Distance to server:");

                                } else {
                                    server.setText("Selected Server:");
                                    serverCoordLat.setText("Latitude");
                                    serverCoordLong.setText("Longitude");
                                    distance.setText("Distance to server:");

                                }
                                cameraPosition = new CameraPosition.Builder().target(marker.getPosition()).zoom(7.0f).build();
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                                googleMap.moveCamera(cameraUpdate);
                                return false;
                            }
                        });
                        googleMap.getUiSettings().setAllGesturesEnabled(true);
                        googleMap.addMarker(new MarkerOptions()
                                .title("Current Location")
                                .position(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()))
                        );
                        /**
                         * generates all the server markers
                         */
                        for (int i = 0; i < servers.length; i++) {
                            try {
                                List<Address> addresses = geocoder.getFromLocationName(servers[i], 1);
                                googleMap.addMarker(new MarkerOptions()
                                        .title("League of Legends server")
                                        .position(new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude()))
                                        .snippet(serverNames[i] + " Server")
                                );
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        if (lastLocation != null) {
                            cameraPosition = new CameraPosition.Builder().target(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude())).zoom(5.0f).build();
                        } else {
                            cameraPosition = new CameraPosition.Builder().target(new LatLng(51.2362, 0.5704)).zoom(5.0f).build();

                        }
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                        googleMap.moveCamera(cameraUpdate);

                    }

                }
            });
        }
        return rootView;

    }

    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private Boolean displayGPSStatus() {
        ContentResolver contentResolver = getActivity().getContentResolver();
        boolean gpsStatus = Settings.Secure.isLocationProviderEnabled(contentResolver, LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;

        } else {
            return false;
        }

    }

    private class CustomLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null || lastLocation != null) {
                lastLocation.setLatitude(location.getLatitude());
                lastLocation.setLongitude(location.getLongitude());
            } else {
                Toast.makeText(getActivity(), "Error,location not received.", Toast.LENGTH_SHORT).show();
                Log.i("Log:", "LOCATION NOT CHANGED/LOCATION NULL");
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
    }
}
