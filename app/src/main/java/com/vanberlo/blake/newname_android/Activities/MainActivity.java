package com.vanberlo.blake.newname_android.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.vanberlo.blake.newname_android.Fragments.AboutFragment;
import com.vanberlo.blake.newname_android.Fragments.FavouritesFragment;
import com.vanberlo.blake.newname_android.Fragments.HomeFragment;
import com.vanberlo.blake.newname_android.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnFragmentInteractionListener, AboutFragment.OnFragmentInteractionListener, FavouritesFragment.OnFragmentInteractionListener {


    private Fragment curFragment;

    /**
     * Set up the UI for the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        curFragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_layout_main, curFragment).commit();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Set up links for the side drawer buttons to update the fragment
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            curFragment = new HomeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_layout_main, curFragment).commit();
        } else if (id == R.id.nav_favourites) {
            curFragment = new FavouritesFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_layout_main, curFragment).commit();
        } else if (id == R.id.nav_about) {
            curFragment = new AboutFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_layout_main, curFragment).commit();
        }
        else if (id == R.id.nav_share) {
            onShareBtnClicked();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onFragmentInteraction(Uri uri){

    }

    /**
     * Starts a send intent with a link to NameGen on the Google Play store as the content
     */
    public void onShareBtnClicked(){
        Intent myIntent = new Intent(Intent.ACTION_SEND);
        myIntent.setType("text/plain");
        String shareBody = "https://play.google.com/store/apps/details?id=com.vanberlo.blake.newname_android"; // Link to NameGen on the Play Store
        String shareSub = "Check out NameGen on the Google Play store!";
        myIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
        myIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
        startActivity(Intent.createChooser(myIntent,"Share using..."));
    }
}
