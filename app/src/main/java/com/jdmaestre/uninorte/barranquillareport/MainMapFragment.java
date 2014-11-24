package com.jdmaestre.uninorte.barranquillareport;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseUser;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * Created by Jose on 22/11/2014.
 */
public class MainMapFragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;
    private static String JsonURL = "http://starcrash.hostinazo.com/finalweb/maps.php";
    protected String TAG = MainActivity.class.getSimpleName();
    protected static Context mContext;
    protected JSONObject mData;
    protected MarkerOptions myPosition;

    // Acquire a reference to the system Location Manager

    LocationListener locationListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_home, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        Button mReportarButton = (Button)getView().findViewById(R.id.reportar_Button);

        mReportarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence[] tipoIncidencia = new CharSequence[]{"Muy grave","Grave","Leve","Cancelar"};
                LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                String locationProvider = LocationManager.GPS_PROVIDER;
                final Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

                new AlertDialog.Builder(getActivity())
                        .setTitle("Incidente")
                .setItems(tipoIncidencia, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item

                        if (which == 0){
                            googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()))
                                    .title("Nuevo reporte").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)) );
                        }else if (which == 1){
                            googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()))
                                    .title("Nuevo reporte").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)) );
                        }else{
                            googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()))
                                    .title("Nuevo reporte").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)) );
                        }

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
            }
        });

        if (isNetworkAvailable()) {
            GetDataTask getDataTask = new GetDataTask();
            getDataTask.execute();
        }



    }



    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        // Define a listener that responds to location updates
         locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                LatLng a = new LatLng(location.getLatitude(),location.getLongitude());

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(a)    // Sets the center of the map to Mountain View
                        .zoom(14)                   // Sets the zoom
                        //.bearing(90)                // Sets the orientation of the camera to east
                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),2000,null);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {

            }

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        String locationProvider = LocationManager.NETWORK_PROVIDER;


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5 , locationListener);



    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(locationListener);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }





    public class GetDataTask extends AsyncTask<Object, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Object... params) {
            int responseCode = -1;
            JSONObject jsonResponse = null;
            try {
                URL blogFeedUsr = new URL("http://starcrash.hostinazo.com/finalweb/maps.php");
                HttpURLConnection connection = (HttpURLConnection) blogFeedUsr
                        .openConnection();
                connection.connect();

                responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {

                    try {

                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getActivity(), "Cargando...", Toast.LENGTH_SHORT).show();
                            }
                        });

                        jsonResponse = new JSONObject(
                                readUrl("http://starcrash.hostinazo.com/finalweb/maps.php"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {

                }

            } catch (MalformedURLException e) {

            } catch (IOException e) {

            } catch (Exception e) {

            }
            return jsonResponse;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if (result==null){
                Toast.makeText(getActivity(), "Hubo un problema al realizar la transacción. Por favor inténtalo de nuevo.", Toast.LENGTH_SHORT).show();
            }else{
                mData = result;
                handleBlogResponse();
            }
        }

    }

    public void handleBlogResponse() {


        if (mData == null){

        } else {
            try {
                JSONArray jsonPosts = mData.getJSONArray("incidentes");

                for (int i = 0;i<jsonPosts.length() ;i++){

                    JSONObject post = jsonPosts.getJSONObject(i);
                    Double latitud = post.getDouble("latitud");
                    Double longitud = post.getDouble("longitud");
                    String nivel = post.getString("nivel");

                    if (nivel.equals("Leve")){
                        googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latitud, longitud))
                                .title(nivel).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)) );
                    }else if (nivel.equals("Grave")){
                        googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latitud, longitud))
                                .title(nivel).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)) );
                    }else{
                        googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latitud, longitud))
                                .title(nivel).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)) );
                    }

                }

            } catch (JSONException e) {
                Log.e(TAG, "Exception caught!", e);
            }
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(MainActivity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isNetworkAvaible = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isNetworkAvaible = true;

        } else {
            Toast.makeText(getActivity(), "No hay red disponible ", Toast.LENGTH_LONG)
                    .show();
        }
        return isNetworkAvaible;
    }




    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }



}




