package com.example.recycleapplicationv3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MapsFragment extends Fragment {
    private GoogleMap map = null;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            final String[] formattedObj = new String[1];
            formattedObj[0] = "jkswsws";
            ArrayList<String> latitude = getArguments().getStringArrayList("Latitudes");
            ArrayList<String> longitude = getArguments().getStringArrayList("Longitudes");
            ArrayList<String> name = getArguments().getStringArrayList("Names");
            double userLat = getArguments().getDouble("userlat");
            double userLong = getArguments().getDouble("userlon");
            System.out.println("userlat: " + userLat);
            System.out.println("userlon: " + userLong);
            for (int i = 0; i < latitude.size(); i++) {
                LatLng recyclingCenter = new LatLng(Double.parseDouble(latitude.get(i)), Double.parseDouble(longitude.get(i)));
                String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude.get(i) + "," + longitude.get(i) + "&key=AIzaSyA47LH3T001h48Hfj5c0fin9SJWWOAt7ZU";
                AsyncHttpClient earth911req = new AsyncHttpClient();
                RequestParams earthParams = new RequestParams();

                int finalI = i;
                earth911req.get(url, earthParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                        // Handle resulting parsed JSON response here
//                        Snackbar.make(view, "MF IT WORKED", Snackbar.LENGTH_LONG)
//                                .setAction("Action", null).show();

                        JSONObject res = response;
                        System.out.println(response);
                        JSONArray resultlist=null;
                        try {
                            resultlist=response.getJSONArray("results");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            formattedObj[0] = resultlist.getJSONObject(0).getString("formatted_address");
                            map.addMarker(new MarkerOptions()
                                    .position(recyclingCenter)
                                    .title(name.get(finalI))
                                    .snippet(formattedObj[0])
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                            System.out.println("addy: " + formattedObj[0]);
                            LatLng userLoc = new LatLng(userLat, userLong);
                            map.addMarker(new MarkerOptions().position(userLoc).title("Your Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(userLoc, 10));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


//                dialog.show(getActivity().getSupportFragmentManager(), "stuff");
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    }
                });
                String snippname = "ejidjled";

            }
            LatLng userLoc = new LatLng(userLat, userLong);
            map.addMarker(new MarkerOptions().position(userLoc).title("Your Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(userLoc, 10));



//            LatLng sydney = new LatLng(-34, 151);
//            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}