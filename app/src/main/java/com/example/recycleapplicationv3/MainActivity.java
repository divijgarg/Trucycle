package com.example.recycleapplicationv3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;


public class
MainActivity extends AppCompatActivity {

//    BottomNavigationView navigationView;
    private DrawerLayout d1;
    private ActionBarDrawerToggle abdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        d1=(DrawerLayout)findViewById(R.id.drawer);
        setTitle("Recycle!");
        abdt=new ActionBarDrawerToggle(this,d1,null, R.string.Open,R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);
        d1.addDrawerListener(abdt);
        abdt.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().show();//show action bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//hide notification bar
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Fragment fragment = new HomeFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.body_container, fragment).commit();

//        getSupportActionBar().hide();

//        navigationView = findViewById(R.id.bottom_navigation);
       /* getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new HomeFragment()).commit();
//        navigationView.setSelectedItemId(R.id.nav_home);
//        navigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.nav_home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.nav_like:
                        fragment = new NewsFragment();
                        break;
                    case R.id.nav_bar:
                        fragment = new BarcodeFragmentv2();
                        break;
                    case R.id.nav_info:
                        fragment = new InformationFragment();
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.body_container, fragment).commit();
                return true;
            }
        });*/
        NavigationView navigationView=(NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("WrongConstant")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment = null;
                    switch (item.getItemId()){
                        case R.id.nav_home:
                            fragment = new HomeFragment();
                            break;
                        case R.id.nav_like:
                            fragment = new NewsFragment();
                            break;
                        case R.id.nav_bar:
                            fragment = new BarcodeFragmentv2();
                            break;
                        case R.id.nav_info:
                            fragment = new InformationFragment();
                            break;
                }
                d1.closeDrawer(GravityCompat.START);
                getSupportFragmentManager().beginTransaction().replace(R.id.body_container, fragment).commit();
                return true;

            }

        });
    }
    @SuppressLint("WrongConstant")
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        return abdt.onOptionsItemSelected(item)||super.onOptionsItemSelected(item);

    }
}