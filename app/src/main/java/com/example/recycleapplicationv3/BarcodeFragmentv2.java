package com.example.recycleapplicationv3;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Looper;
import android.provider.Settings;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.params.HttpParams;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BarcodeFragmentv2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BarcodeFragmentv2 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    //This class provides methods to play DTMF tones
    private ToneGenerator toneGen1;
    private TextView barcodeText;
    private String barcodeData;
    private boolean scanned = false;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    private double latitude=0;
    private double longitude=0;

    public BarcodeFragmentv2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BarcodeFragmentv2.
     */
    // TODO: Rename and change types and number of parameters
    public static BarcodeFragmentv2 newInstance(String param1, String param2) {
        BarcodeFragmentv2 fragment = new BarcodeFragmentv2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        // method to get the location
        getLastLocation();
    }
    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            latitude= location.getLatitude();
                            longitude=location.getLongitude();
                            System.out.println("lat: "+location.getLatitude()+", long: "+location.getLongitude());
                        }
                    }
                });
            } else {
                Toast.makeText(getContext(), "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude= mLastLocation.getLatitude();
            longitude=mLastLocation.getLongitude();
            System.out.println("lat: "+mLastLocation.getLatitude()+", long:"+mLastLocation.getLongitude());
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_barcode_fragmentv2, container, false);
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC,     100);
        surfaceView = rootView.findViewById(R.id.surface_view);
        barcodeText = rootView.findViewById(R.id.barcode_text);
        initialiseDetectorsAndSources();
        // Inflate the layout for this fragment
        return rootView;
    }
    private void initialiseDetectorsAndSources() {

        //Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(getActivity())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(getActivity(), barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                // Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {


                    barcodeText.post(new Runnable() {

                        @Override
                        public void run() {
                            if (!scanned) {
                                if (barcodes.valueAt(0).email != null) {
                                    barcodeText.removeCallbacks(null);
                                    barcodeData = barcodes.valueAt(0).email.address;
                                    barcodeText.setText(barcodeData);
                                    scanned = true;
                                    toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                                    String url1 = "https://api.upcitemdb.com/prod/trial/lookup?upc=" + barcodeData;//api URL MUST HAVE HTTPS:// THAT IS NOT OPTIONAL

                                    new AsyncHttpClient().get(url1, new AsyncHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                                            String str = new String(responseBody);//this will show all the raw data which can be substringed for individual data
                                            System.out.println(str);


                                        }

                                        @Override
                                        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                                            error.printStackTrace();//wil print error message
                                        }


                                    });

                                } else {
                                    if (!scanned) {
                                        getLastLocation();
                                        barcodeData = barcodes.valueAt(0).displayValue;
                                        barcodeText.setText(barcodeData);
                                        scanned = true;
                                        String url1 = "https://api.upcitemdb.com/prod/trial/lookup?upc=" + barcodeData;//api URL MUST HAVE HTTPS:// THAT IS NOT OPTIONAL

                                        new AsyncHttpClient().get(url1, new AsyncHttpResponseHandler() {
                                            @Override
                                            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                                                String str = new String(responseBody);//this will show all the raw data which can be substringed for individual data
//                                                System.out.println(str);
                                                try {
                                                    JSONObject jObject = new JSONObject(str);
                                                    JSONArray listOfItems=jObject.getJSONArray("items");
                                                    System.out.println(listOfItems);
                                                    JSONObject jsonObject2=new JSONObject((listOfItems.get(0).toString()));
                                                    String category=(jsonObject2.getString("category"));
                                                    String newCat="";
                                                    System.out.println("category = " + category);

                                                    for (int i = category.length() - 1; i >= 0; i--) {
                                                        if (category.charAt(i)=='>'){
                                                            category=category.substring(i+1);
                                                            break;
                                                        }
                                                    }
//                                                    category= category.substring(category.indexOf('>')+2);
                                                    for (int i = 0; i < category.length(); i++) {
                                                        if(category.charAt(i)!='>'){
                                                            newCat=newCat+category.charAt(i);
                                                        }
                                                        else{
                                                            i++;
                                                        }
                                                    }
                                                    System.out.println(newCat);
                                                    String newCat2="";
                                                    for (int i = 0; i < newCat.length(); i++) {
                                                        if(newCat.charAt(i)!=' '){
                                                            newCat2=newCat2+newCat.charAt(i);
                                                        }
                                                        else{
                                                            newCat2=newCat2+",";
                                                        }
                                                    }
                                                    System.out.println(newCat2);
                                                    System.out.println(123456);


                                                    //TODO: FIREBASE WORK HERE



                                                    findId(newCat2);
                                                }
                                                catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }

                                            @Override
                                            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                                                error.printStackTrace();//wil print error message
                                            }

                                        });
                                    }
//                                try {
////                                    Response response = client.newCall(request).execute();
//
//
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
                                    toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);

                                }
                            }

                        }
                    });

                }
            }
        });
    }


    public void findId(String name) {

        String url = "https://api.earth911.com/earth911.searchMaterials";
        AsyncHttpClient earth911req = new AsyncHttpClient();
        RequestParams earthParams = new RequestParams();
        earthParams.put("api_key", "9f8e452085d40bad");
        earthParams.put("query", name);
        earth911req.get(url, earthParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                // Handle resulting parsed JSON response here
//                        Snackbar.make(view, "MF IT WORKED", Snackbar.LENGTH_LONG)
//                                .setAction("Action", null).show();

                JSONObject res = response;
                JSONArray resarray = null;
                try {
                    resarray=res.getJSONArray("result");
                    System.out.println(resarray.length());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                int[] matlist=new int[resarray.length()];
                for(int i=0;i<matlist.length;i++){
                    JSONObject Obj= null;
                    try {
                        Obj = resarray.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        matlist[i]= Integer.parseInt(Obj.getString("material_id"));
                        System.out.println(matlist[i]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if(matlist.length>1) {
                    final int[] totalScans = {0};
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(SignInActivity.personName);
                    userRef.child("scans").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                totalScans[0] = (int) ((long) snapshot.getValue());
                                userRef.child("scans").setValue(totalScans[0] + 1);
                            }
                            catch (Exception e){
                                userRef.child("scans").setValue(1);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                System.out.println(response);
                findRecyclingCenter(matlist);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }
        });
//        return null;
    }
    public void findRecyclingCenter(int[] mat_IDs) {
        String url = "https://api.earth911.com/earth911.searchLocations";
        AsyncHttpClient earth911req = new AsyncHttpClient();
        RequestParams earthParams = new RequestParams();

        earthParams.put("api_key", "9f8e452085d40bad");
        if(mat_IDs.length>1) {
            earthParams.put("material_id", mat_IDs[0]);
        }
        earthParams.put("latitude", latitude);
        earthParams.put("longitude", longitude);


        earth911req.get(url, earthParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                // Handle resulting parsed JSON response here
//                        Snackbar.make(view, "MF IT WORKED", Snackbar.LENGTH_LONG)
//                                .setAction("Action", null).show();

                JSONObject res = response;
                System.out.println(response);
                JSONArray rclist=null;
                try {
                    rclist=response.getJSONArray("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String[] namelist=new String[rclist.length()];
                String[] latlist=new String[rclist.length()];
                String[] lonlist=new String[rclist.length()];
                for(int i=0;i<namelist.length;i++){
                    try {
                        namelist[i]= rclist.getJSONObject(i).getString("description");
                        latlist[i]= rclist.getJSONObject(i).getString("latitude");
                        lonlist[i]= rclist.getJSONObject(i).getString("longitude");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                ArrayList<String> essenLats = new ArrayList<>();
                ArrayList<String> essenLongs = new ArrayList<>();
                ArrayList<String> essenNames = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    essenLats.add(latlist[i]);
                    essenLongs.add(lonlist[i]);
                    essenNames.add(namelist[i]);
                }
                Bundle args = new Bundle();

                args.putStringArrayList("Longitudes", essenLongs);
                args.putStringArrayList("Latitudes", essenLats);
                args.putStringArrayList("Names", essenNames);

                RecyclingCenterDialogFragment dialog=new RecyclingCenterDialogFragment(namelist, latlist, lonlist, latitude, longitude);
                Fragment fragment = new MapsFragment();
                fragment.setArguments(args);
                args.putDouble("userlat", latitude);
                args.putDouble("userlon", longitude);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.body_container, fragment).commit();

//                dialog.show(getActivity().getSupportFragmentManager(), "stuff");
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }
        });
//        return null;
    }
}