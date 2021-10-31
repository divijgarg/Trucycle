package com.example.recycleapplicationv3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;


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
        setTitle("Dashboard");
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

        createNotificationChannel();


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


    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "RecycleNotificationChannel";
            String description = "Channel for recycle reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyRecycle", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);


        }
    }
    public void setNotif(int dayindex,int hour, int minute) {

        //you can get notificationManager like this:
        //notificationManage r= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationManagerCompat.from(getApplicationContext()).cancelAll();


        Intent intent = new Intent(MainActivity.this, NotificationBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

        AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
        //long triggertime=when alarm is set
        //get date, time from fb here.

        String[] daynames={"SUNDAY","MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY"};
        LocalDateTime dateTime = LocalDateTime.now();
        LocalDateTime nextoccurence;

        switch(dayindex) {
            case 0:
                nextoccurence = dateTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
                break;
            case 1:
                nextoccurence = dateTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY));
                break;
            case 2:
                nextoccurence = dateTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.WEDNESDAY));
                break;
            case 3:
                nextoccurence = dateTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.THURSDAY));
                break;
            case 4:
                nextoccurence = dateTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
                break;
            case 5:
                nextoccurence = dateTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
                break;
            case 6:
                nextoccurence = dateTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + dayindex);
        }
        ZonedDateTime zdt = nextoccurence.atZone(ZoneId.of("America/Chicago"));
        Calendar calendar = Calendar.getInstance();
        int hour24hrs = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        long millis = zdt.toInstant().toEpochMilli()+3600000*(hour-hour24hrs)+60000*(minute-minutes);
        double extra=Math.floorMod(millis,60000);
        millis= (long) (millis-extra);

        System.out.println("NOTIF set to "+millis);

        alarmManager.set(AlarmManager.RTC_WAKEUP,millis,
                pendingIntent);

    }
}