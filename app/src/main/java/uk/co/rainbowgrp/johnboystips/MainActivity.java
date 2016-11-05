package uk.co.rainbowgrp.johnboystips;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import uk.co.rainbowgrp.johnboystips.fragments.AboutFragment;
import uk.co.rainbowgrp.johnboystips.fragments.AnteFragment;
import uk.co.rainbowgrp.johnboystips.fragments.HistoryFragment;
import uk.co.rainbowgrp.johnboystips.fragments.HomeFragment;
import uk.co.rainbowgrp.johnboystips.fragments.RaceFragment;
import uk.co.rainbowgrp.johnboystips.fragments.StatsFragment;
import uk.co.rainbowgrp.johnboystips.fragments.TipsFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    private TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getWindow().setBackgroundDrawableResource(R.color.white);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        email = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textView);

        Intent i = getIntent();

        email.setText(i.getStringExtra("email"));

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;

        if (id == R.id.nav_home) {

            fragment = new HomeFragment();

        } else if (id == R.id.nav_tips) {

            fragment = new TipsFragment();

        } else if (id == R.id.nav_history) {

            fragment = new HistoryFragment();

        } else if (id == R.id.nav_ante) {

            fragment = new AnteFragment();

        } else if (id == R.id.nav_race) {

            fragment = new RaceFragment();

        } else if (id == R.id.nav_stats) {

            fragment = new StatsFragment();

        } else if (id == R.id.nav_about) {

            fragment = new AboutFragment();

        } else if (id == R.id.nav_share) {

            finish();

        } else if (id == R.id.nav_exit) {

            finish();

        }

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRefresh() {



    }
}
