package com.example.recycleapplicationv3;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {


    private TextView welcome, weather;

    private int x = 0; //dont ask
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private ProgressBar scansProgBar;
    private TextView scansTextView;
    private Button notificationbutton;

    private ImageView bronzeMedal, goldMedal, silverMedal;

    //    private LinearLayout barcode, news, tutorials;
//
    private Button thingstodoBtn;

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        welcome = (TextView) view.findViewById(R.id.welcome);
        welcome.setText("Welcome, " + SignInActivity.personFirstName);
////        lvItems = (ListView) view.findViewById(R.id.lvItems);
//        items = new ArrayList<String>();
////        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.activity_list_item,items);
////        lvItems.setAdapter(itemsAdapter);
//        items.add("First Item");
//        items.add("Second Item");

        thingstodoBtn = view.findViewById(R.id.thingstodoBtn);
        thingstodoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment ThingsToDo = new HelpFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.body_container, ThingsToDo); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();

            }
        });


        notificationbutton=view.findViewById(R.id.setNotification);
        notificationbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] daysofweek={"MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY","SUNDAY"};

                new AlertDialog.Builder(getContext())
                        .setTitle("Choose Date")

                        .setItems(daysofweek, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                showTimePicker(which);
                            }
                        })
                        .setIcon(R.drawable.logo)
                        .show();

            }
        });



        bronzeMedal = (ImageView) view.findViewById(R.id.bronzeMedal);
        goldMedal = (ImageView) view.findViewById(R.id.goldMedal);
        silverMedal = (ImageView) view.findViewById(R.id.silverMedal);

        scansProgBar = view.findViewById(R.id.scansProgBar);
        scansTextView = view.findViewById(R.id.scansTextView);
        final int[] totalScans = {0};
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(SignInActivity.personName);

        userRef.child("scans").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    int modDivider = 1;
                    int subtractor = 1;
//                    int multiplier = 4;
                    totalScans[0] = (int) ((long) snapshot.getValue());

                    if (totalScans[0]>=25 && totalScans[0]<75) {
                        modDivider = 50;
                        subtractor = 25;
                        bronzeMedal.setVisibility(View.VISIBLE);
                    } else if (totalScans[0]>=75 && totalScans[0]<175) {
                        modDivider = 100;
                        subtractor = 75;
                        bronzeMedal.setVisibility(View.VISIBLE);
                        silverMedal.setVisibility(View.VISIBLE);
                    } else if (totalScans[0]>=175){
                        modDivider = 100;
                        subtractor = 175;
                        bronzeMedal.setVisibility(View.VISIBLE);
                        silverMedal.setVisibility(View.VISIBLE);
                        goldMedal.setVisibility(View.VISIBLE);

                    } else {
                        subtractor = 0;
                        modDivider = 25;
                    }
                    totalScans[0] = totalScans[0]-subtractor;

                    scansProgBar.setProgress((totalScans[0] % modDivider) * (100/modDivider));
                    scansTextView.setText(Integer.toString(totalScans[0]));
//                userRef.child("scans").setValue(totalScans[0]+1);
                }
                catch (Exception e){
                    scansTextView.setText(Integer.toString(0));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



//        scanBarcode= (Button) view.findViewById(R.id.scanbarcode);

    requestPermissions();

    return view;

    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, 44);


//        initialiseDetectorsAndSources();
    }

    private void showTimePicker(int dayindex){
        DialogFragment newFragment = new TimePickerFragment(dayindex);
        newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");

    }
}