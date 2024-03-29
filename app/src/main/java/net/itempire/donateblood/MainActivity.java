package net.itempire.donateblood;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.mikepenz.iconics.context.IconicsContextWrapper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    android.app.FragmentManager fragmentManager;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        sessionManager = new SessionManager(getApplicationContext());

        fragmentManager = getFragmentManager();

        if(sessionManager.isLoggedIn()){
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer,new ProfileFragment()).commit();
        }
        else {
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new DonationRequestsFragment()).commit();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(sessionManager.isLoggedIn()){
            getMenuInflater().inflate(R.menu.logout, menu);
        }
        else{
            getMenuInflater().inflate(R.menu.login, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_login) {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);

            return true;
        }
        if (id == R.id.action_logout){
            sessionManager.logoutUser();
            MainActivity.this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            sessionManager.checkLogin();
            fragmentManager.popBackStack();
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer,new ProfileFragment()).addToBackStack(null).commit();
        } else if (id == R.id.nav_request_donation) {
            sessionManager.checkLogin();
            fragmentManager.popBackStack();
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer,new BloodGroupFragment()).addToBackStack(null).commit();
        } else if (id == R.id.nav_my_requests) {
            sessionManager.checkLogin();
            fragmentManager.popBackStack();
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer,new MyRequestsFragment()).addToBackStack(null).commit();
        } else if (id == R.id.nav_view_donation_requests) {
            fragmentManager.popBackStack();
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer,new DonationRequestsFragment()).addToBackStack(null).commit();
        } else if (id == R.id.nav_featured_donors) {
            fragmentManager.popBackStack();
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer,new FeaturedDonorsFragment()).addToBackStack(null).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }
}
