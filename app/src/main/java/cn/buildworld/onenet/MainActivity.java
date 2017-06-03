package cn.buildworld.onenet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import cn.buildworld.onenet.fragment.FirstFragmet;
import cn.buildworld.onenet.fragment.HumFragment;
import cn.buildworld.onenet.fragment.SecondFragmet;
import cn.buildworld.onenet.fragment.TempFragment;
import cn.buildworld.onenet.fragment.TotalFragment;
import cn.buildworld.onenet.service.GetDataService;
import cn.buildworld.onenet.util.Preferences;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "主界面";
    private HumFragment humFragment;
    private TempFragment tempFragment;
    private DrawerLayout drawer;
    private TotalFragment totalFragment;
    private FirstFragmet firstFragmet;
    private SecondFragmet secondFragmet;


    private Intent getdataIntent;
//    private BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String actionName = intent.getAction();
//            //判断接收到的广播是否是想要的
//            if (actionName.equals("datachange")){
//                Log.i(TAG, "onReceive: "+"接收到了广播");
//
//            }
//        }
//    };

    //记录时间
    private Preferences preferences;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        String savedApiKey = Preferences.getInstance(this).getString(Preferences.API_KEY,null);
        String saveDeviceNum = Preferences.getInstance(this).getString(Preferences.Device_Num,null);
        if (savedApiKey.equals(null) && saveDeviceNum.equals(null)){
            Intent intent = new Intent(this,EditApikeyActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            startActivity(intent);
        }
        else {

            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, FirstFragmet.newInstance()).commit();


            //开启获取数据的服务
//        getdataIntent = new Intent();
//        getdataIntent.setClass(MainActivity.this, GetDataService.class);
//        startService(getdataIntent);

            //接收广播
//        registerBoradcastReceiver();

            //startService(new Intent(MainActivity.this,GetDataService.class));
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    //注册广播
//    public void registerBoradcastReceiver(){
//        IntentFilter myIntentFilter = new IntentFilter();
//        myIntentFilter.addAction("datachange");
//        registerReceiver(receiver, myIntentFilter);
//    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//            Toast.makeText(this, "我是相机", Toast.LENGTH_SHORT).show();
//            humFragment = HumFragment.newInstance();
//            getSupportFragmentManager().beginTransaction().add(R.id.fragment,humFragment).commit();
//
//
//        } else if (id == R.id.nav_gallery) {
//            Toast.makeText(this, "我是相册", Toast.LENGTH_SHORT).show();
//            tempFragment = TempFragment.newInstance();
//            getSupportFragmentManager().beginTransaction().add(R.id.fragment,tempFragment).commit();
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }




    NavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            drawer.closeDrawer(GravityCompat.START);
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            switch (item.getItemId()) {
                case R.id.temp:
//                    humFragment = HumFragment.newInstance();
//                    ft.replace(R.id.fragment, humFragment).commit();

                    firstFragmet = FirstFragmet.newInstance();
                    ft.replace(R.id.fragment,firstFragmet).commit();
                    getSupportActionBar().setTitle("湿度检测");
                    break;

                case R.id.hum:
//                    tempFragment = TempFragment.newInstance();
//                    ft.replace(R.id.fragment, tempFragment).commit();

                    secondFragmet = SecondFragmet.newInstance();
                    ft.replace(R.id.fragment,secondFragmet).commit();
                    getSupportActionBar().setTitle("温度检测");
                    break;

                case R.id.data_total:
                    totalFragment = TotalFragment.newInstance();
                    ft.replace(R.id.fragment,totalFragment).commit();
                    getSupportActionBar().setTitle("数据集合");
                    break;
                case R.id.setting:
                    startActivity(new Intent(MainActivity.this,EditApikeyActivity.class));
                    break;
            }
            return true;
        }
    };
}
