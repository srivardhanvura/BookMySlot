package com.itt.bookmyslot.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.itt.bookmyslot.Fragment.AboutFragment;
import com.itt.bookmyslot.Fragment.BookingFragment;
import com.itt.bookmyslot.Fragment.CovidFragment;
import com.itt.bookmyslot.Fragment.DonateFragment;
import com.itt.bookmyslot.Fragment.HomeFragment;
import com.itt.bookmyslot.Fragment.PrevBookingFragment;
import com.itt.bookmyslot.Fragment.PrevDonationFragment;
import com.itt.bookmyslot.R;

import info.androidhive.fontawesome.FontDrawable;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawer = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_open, R.string.navigation_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        int[] icons = {R.string.fa_home_solid, R.string.fa_football_ball_solid, R.string.fa_donate_solid,
                R.string.fa_history_solid,R.string.fa_money_check_solid, R.string.fa_heart_solid, R.string.fa_info_solid};

        renderMenuIcons(navigationView.getMenu(), icons, true, false);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.home_menu);
        }
    }

    private void renderMenuIcons(Menu menu, int[] icons, boolean isSolid, boolean isBrand) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            if (!menuItem.hasSubMenu()) {
                FontDrawable drawable = new FontDrawable(this, icons[i], isSolid, isBrand);
                drawable.setTextColor(ContextCompat.getColor(this, R.color.black));
                drawable.setTextSize(20);
                menu.getItem(i).setIcon(drawable);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else {
            if (getSupportFragmentManager().findFragmentById(R.id.frame_layout) instanceof HomeFragment) {
//                super.onBackPressed();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
                startActivity(intent);
                finish();
                System.exit(0);
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
                getSupportActionBar().setTitle("BookMySlot");
                navigationView.setCheckedItem(R.id.home_menu);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.about_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new AboutFragment()).commit();
                getSupportActionBar().setTitle("About");
                break;

            case R.id.previous_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new PrevBookingFragment()).commit();
                getSupportActionBar().setTitle("Previous Bookings");
                break;

            case R.id.booking_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new BookingFragment()).commit();
                getSupportActionBar().setTitle("Booking");
                break;

            case R.id.donate_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new DonateFragment()).commit();
                getSupportActionBar().setTitle("Donate");
                break;

            case R.id.prev_donation_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new PrevDonationFragment()).commit();
                getSupportActionBar().setTitle("Previous Donations");
                break;

            case R.id.covid_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new CovidFragment()).commit();
                getSupportActionBar().setTitle("Covid Data");
                break;

            case R.id.home_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
                getSupportActionBar().setTitle("BookMySlot");
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}