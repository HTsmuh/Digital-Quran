package com.horizontech.biz.digitalquran.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.horizontech.biz.digitalquran.Adapter.PagerAdapter;
import com.horizontech.biz.digitalquran.Menu.AboutUsActivity;
import com.horizontech.biz.digitalquran.Menu.CreditsActivity;
import com.horizontech.biz.digitalquran.R;
import com.horizontech.biz.digitalquran.Menu.SettingActivity;
import com.winsontan520.wversionmanager.library.WVersionManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    boolean haveConnectedWifi = false;
    boolean haveConnectedMobile = false;
    String latestVersion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String network= String.valueOf(haveNetworkConnection());
        if (network.equals("true")){
            //getCurrentVersion();
            String currentVersion = getCurrentVersion();
            Log.d("LOG", "Current version = " + currentVersion);
            try {
                latestVersion = new GetLatestVersion().execute().get();
                Log.d("LOG", "Latest version = " + latestVersion);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            //Toast.makeText(this, ""+network+"  "+latestVersion+"  "+currentVersion, Toast.LENGTH_SHORT).show();
            //If the versions are not the same
            if(!currentVersion.equals(latestVersion)){
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("An Update is Available");
                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Click button action
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.horizontech.biz.digitalquran")));
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Cancel button action
                    }
                });

                builder.setCancelable(false);
                builder.show();
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("PARA"));
        tabLayout.addTab(tabLayout.newTab().setText("SURAH"));
        tabLayout.addTab(tabLayout.newTab().setText("BOOKMARK"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private boolean haveNetworkConnection() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }private String getCurrentVersion(){
        PackageManager pm = this.getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo =  pm.getPackageInfo(this.getPackageName(),0);

        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        String currentVersion = pInfo.versionName;

        return currentVersion;
    }
    private class GetLatestVersion extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                //It retrieves the latest version by scraping the content of current version from play store at runtime
                String urlOfAppFromPlayStore = "https://play.google.com/store/apps/details?id=com.horizontech.biz.digitalquran";
                Document doc = Jsoup.connect(urlOfAppFromPlayStore).get();
                latestVersion = doc.getElementsByAttributeValue("itemprop","softwareVersion").first().text();

            }catch (Exception e){
                e.printStackTrace();
            }
            return latestVersion;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about_us, menu);
        getMenuInflater().inflate(R.menu.menu_credits, menu);
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        getMenuInflater().inflate(R.menu.menu_rate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_credit) {
            Intent intent = new Intent(this, CreditsActivity.class);
            startActivity(intent);
        }else  if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        }else  if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutUsActivity.class);
            startActivity(intent);
        }else  if (id == R.id.action_rate) {
            WVersionManager versionManager = new WVersionManager(this);
            versionManager.setTitle("Please rate us"); // optional
            versionManager.setMessage("We need your help to rate this app!"); // optional
            versionManager.setAskForRatePositiveLabel("OK"); // optional
            versionManager.setAskForRateNegativeLabel("Not now"); // optional
            versionManager.askForRate();
        }

        return super.onOptionsItemSelected(item);
    }
}