package com.example.recycleapplicationv3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HelpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HelpFragment extends Fragment {

    
    private LinearLayout barcode, news, tutorials;

    private Button scanBarcode;

    public static HelpFragment newInstance(String param1, String param2) {
        HelpFragment fragment = new HelpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);

        // Inflate the layout for this fragment

        barcode=(LinearLayout) view.findViewById(R.id.barcodescan);
        tutorials=(LinearLayout) view.findViewById(R.id.tutorials);
        news =(LinearLayout) view.findViewById(R.id.news);
        //TODO: LISTVIEW ONVALUEEVENTLISTENER FIREBASE HERE

        //TODO: DETERMINATE PROGRESSBAR WORK HERE

        barcode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Fragment Barcode = new BarcodeFragmentv2();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.body_container, Barcode ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();

            }
        });
        news.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Fragment News = new NewsFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.body_container, News ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();

            }
        });

        tutorials.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Fragment Videos = new InformationFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.body_container, Videos ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();

            }
        });
        return inflater.inflate(R.layout.fragment_help, container, false);
    }
}