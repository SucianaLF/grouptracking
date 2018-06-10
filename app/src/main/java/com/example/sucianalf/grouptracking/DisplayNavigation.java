package com.example.sucianalf.grouptracking;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.sucianalf.grouptracking.Dijkstra.Dijkstra;
import com.example.sucianalf.grouptracking.Dijkstra.Edge;
import com.example.sucianalf.grouptracking.Dijkstra.Graph;
import com.example.sucianalf.grouptracking.Dijkstra.Vertex;
import com.example.sucianalf.grouptracking.Model.DijkstraObject;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

public class DisplayNavigation extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "MapActivity";
    private static final String API_KEY = "AIzaSyA6A-6_iU9_oybRIttTVu9DMVG9lzvFs2E";
    private static final String BASE_JSONURL = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    Marker mrk;

    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    public FusedLocationProviderClient mFusedLocationProviderClient;
    private EditText mSearchText;
    double carilat, carilong, inilat, inilong;
    private DatabaseReference mDatabase;
    int count, count1;

    public MarkerOptions options = new MarkerOptions();
    public ArrayList<LatLng> latlngs = new ArrayList<>();

    public ArrayList<DijkstraObject> getLatlngs = new ArrayList<>();


    //public ArrayList<DijkstraObject> dijkstra = new ArrayList<>();
    public String searchString, storedString, placeIdOri, placeIdDest;
    public String OriAPI, DestAPI;
    public String arrDirection;
    private DijkstraObject get;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_navigation);
        Log.d(TAG, "onCreate:");
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        count = 0;
        count1 = 0;
        mSearchText = findViewById(R.id.input_search);
        getLocationPermission();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMaxZoomPreference(16);

        if (mLocationPermissionsGranted) {
            getDeviceLocationOnMapReady();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            init();

            if (getLatlngs.size() > 1 && getLatlngs != null) {
                Dijkstra obj = new Dijkstra();
                Graph g = new Graph(getLatlngs.size()+1);

                for (DijkstraObject get : getLatlngs) {
                    options.position(new LatLng(get.getstartLat(), get.getStartLng()));
                    options.title("V" + get.getIndex());
                    options.snippet(""+(int)get.getDistance());
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                    mMap.addMarker(options);

                    g.addEdge(get.getIndex(), get.getEndIndex(), (int)get.getDistance());
                    g.addEdge(get.getEndIndex(), get.getIndex(), (int)get.getDistance());
                }

                obj.calculate(g.getVertex(0));

                // Print the minimum Distance.
                for(Vertex v:g.getVertices()) {
                    Log.d(TAG, "Vertex - " + v + " , Dist - " + v.minDistance + " , Path - ");
                    for (Vertex pathvert : v.path) {
                        System.out.print(pathvert + " ");
                    }
                    Log.d(TAG, ""+v);
                }
            }


        }
    }

    private void getLocationPermission(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
        Log.d(TAG, "Location Permission: Auth Location Permission");
    }

    private void initMap(){
        Log.d(TAG, "initMap: initializing map");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(DisplayNavigation.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d("TAG", "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d("TAG", "Location Permission Granted");
                    mLocationPermissionsGranted = true;
                    initMap();
                }
            }
        }
    }

    private void init(){
        Log.d(TAG, "init: initializing");

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER)
                {
                    geoLocate();
                    makeJsonObjectRequestPlaceId(OriAPI, DestAPI);
                    makeJsonObjectRequestNode(placeIdOri, placeIdDest);

                    if (count == 0) {
                        if (placeIdDest == null && placeIdOri == null) {
                            count++;
                            Toast.makeText(DisplayNavigation.this, "Internal Server Error. Please Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    if (placeIdDest != null || placeIdOri != null)
                    {
                        count++;
                    }

                    if (count == 3)
                    {
                        if (latlngs == null || latlngs.size() < 1 ){
                            count++;
                            Toast.makeText(DisplayNavigation.this, "Internal Server Error. Please Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                return false;
            }
        });
    }

    private void geoLocate(){
        Log.d(TAG, "geoLocate: geolocating");

        searchString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(DisplayNavigation.this);

        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
        }

        if(list.size() > 0) {
            Address address = list.get(0);
            if(mrk != null)
            {
                mrk.remove();
            }
            Log.i(TAG, "geoLocate: found  location: " + address.toString());

            carilat = address.getLatitude();
            carilong = address.getLongitude();
            LatLng cari = new LatLng(carilat, carilong);//dari search
            LatLng ini = new LatLng(inilat, inilong);//lokasi device

            DestAPI = BASE_JSONURL + "origin=" + carilat + "," + carilong +
                    "&destination=" + carilat + "," + carilong +
                    "&mode=driving&alternatives=true&key=" + API_KEY;
            Log.i(TAG, "geoLocate: found destloc placeid: "+DestAPI);

            //initMap();

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            mrk =  mMap.addMarker(new MarkerOptions().position(cari).title(mSearchText.getText().toString()));
            builder.include(cari);
            builder.include(ini);
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (height * 0.20);

            onMapReady(mMap);

            LatLngBounds bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
            mMap.animateCamera(cu);
        }
    }

    private void getDeviceLocationOnMapReady(){
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationPermissionsGranted){
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Location currentLocation = (Location) task.getResult();
                            inilat = currentLocation.getLatitude();
                            inilong = currentLocation.getLongitude();

                            OriAPI = BASE_JSONURL + "origin=" + inilat + "," + inilong +
                                    "&destination=" + inilat + "," +inilong +
                                    "&mode=driving&alternatives=true&key="+API_KEY;
                            Log.i(TAG, "Device Location: "+OriAPI);
                            if (count1 == 0)
                            {
                                count1++;
                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                        DEFAULT_ZOOM);
                            }
                        }else{
                            Toast.makeText(DisplayNavigation.this, "unable to get current location", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void makeJsonObjectRequestPlaceId(String JsonOrigin, String JsonDestination){
        String JsonOriginURL = JsonOrigin;
        JsonObjectRequest jsonObjReqOrigin = new JsonObjectRequest(Request.Method.GET, JsonOriginURL,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    placeIdOri = response.getJSONArray("geocoded_waypoints")
                            .getJSONObject(0)
                            .getString("place_id")
                            .toString();
                    Log.i("Origin Place_Id", placeIdOri);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReqOrigin);

        String JsonDestinationURL = JsonDestination;
        JsonObjectRequest jsonObjReqDestination = new JsonObjectRequest(Request.Method.GET, JsonDestinationURL,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    placeIdDest = response.getJSONArray("geocoded_waypoints")
                            .getJSONObject(0)
                            .getString("place_id")
                            .toString();
                    Log.d("Destination Place_Id", placeIdDest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReqDestination);
    }

    private void makeJsonObjectRequestNode(String oriNode, String destNode){
        String urlJsonObj = BASE_JSONURL+"origin=place_id:" + oriNode +
                "&destination=place_id:" + destNode + "&mode=driving&units=metric&avoid=tolls&alternatives=true&key="+API_KEY;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, urlJsonObj,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                arrDirection = response.toString();
                try {
                    getLatlngs.clear();
                    JSONArray routes = response.getJSONArray("routes");

                    int k = 0;
                    int l = 1;
                    int temp = 0;
                    int temp2 = 0;
                    for (int j=0; j < routes.length(); j++){
                        JSONArray steps = routes.getJSONObject(j)
                                .getJSONArray("legs")
                                .getJSONObject(0)
                                .getJSONArray("steps");
                        k = temp;
                        l = temp2;
                        for(int i=0; i < steps.length(); i++){
                            double startLat = steps.getJSONObject(i).getJSONObject("start_location").getDouble("lat");
                            double startLng = steps.getJSONObject(i).getJSONObject("start_location").getDouble("lng");
                            double endLat = steps.getJSONObject(i).getJSONObject("end_location").getDouble("lat");
                            double endLng = steps.getJSONObject(i).getJSONObject("end_location").getDouble("lng");
                            double distance = steps.getJSONObject(i).getJSONObject("distance").getDouble("value");
//                            latlngs.add(new LatLng(startLat, startLng));
                            if (l != steps.length())
                            {
                                getLatlngs.add(new DijkstraObject(k, startLat, startLng, endLat, endLng, l, distance));
                            }
                            else
                            {
                                getLatlngs.add(new DijkstraObject(k, startLat, startLng, endLat, endLng, k, 0));
                            }
                            k++;
                            l++;
                        }
                        temp = k;
                        temp2=l;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //Toast.makeText(getApplicationContext(), "kosong", Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
}
