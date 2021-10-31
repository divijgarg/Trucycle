package com.example.recycleapplicationv3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;

import java.util.jar.Attributes;

public class RecyclingCenterDialogFragment extends DialogFragment {
    private String[] Namelist=null;
    private String[] latlist=null;
    private String[] lonlist=null;
    private double userLat;
    private double userLon;
    public RecyclingCenterDialogFragment(String[] namelist, String[] latlist, String[] lonlist, double userLat, double userLon){
        Namelist=namelist;
        this.latlist = latlist;
        this.lonlist = lonlist;
        this.userLat = userLat;
        this.userLon = userLon;

    }
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int selectedindex=0;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose a Recycling Center")
                .setItems(Namelist, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println(Namelist[which]);
                        System.out.println(latlist[which]);
                        System.out.println(lonlist[which]);
                        Fragment fragment = new MapsFragment();
                        Bundle args = new Bundle();
                        args.putString("latitude", latlist[which]);
                        args.putString("longitude", lonlist[which]);
                        args.putDouble("userlat", userLat);
                        args.putDouble("userlon", userLon);


                        fragment.setArguments(args);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.body_container, fragment).commit();

                    }
                });
        return builder.create();
    }
}
