package com.rokoapp.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.rokoapp.R;
import com.rokoapp.utils.SaveCache;

import static com.rokoapp.utils.ParamUtils.ID_AUTH;

public class Home extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private RelativeLayout headerUser;
    private TextView textViewUserName, welcomeText;
    private ImageView userImage;

    private Boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViews();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        ID_AUTH = SaveCache.getString(Home.this,"Authorization");

        int width = getResources().getDisplayMetrics().widthPixels / 2;
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) mNavigationView.getLayoutParams();
        params.width = width;
        mNavigationView.setLayoutParams(params);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                if (menuItem.getItemId() == R.id.my_trips) {

                }
                /*if (menuItem.getItemId() == R.id.pass_list) {
//                    startActivity(new Intent(Home.this, Notifications.class));
                    startActivity(new Intent(Home.this, PassesList.class));
                    Home.this.finish();
                }*/
                /*if (menuItem.getItemId() == R.id.my_pass) {
//                    startActivity(new Intent(Home.this, Notifications.class));
                    startActivity(new Intent(Home.this, Notifications.class));
                    Home.this.finish();
                }*/
                if (menuItem.getItemId() == R.id.drawer_share) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out Roko App at: https://play.google.com/store/apps/details?id="+getPackageName());
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
                /*if (menuItem.getItemId() == R.id.drawer_exit) {

                }
                if (menuItem.getItemId() == R.id.drawer_costumer_service) {
                    startActivity(new Intent(MainActivity.this, CostumerService.class));
                    MainActivity.this.finish();
                }
                if (menuItem.getItemId() == R.id.drawer_faq) {
                    startActivity(new Intent(MainActivity.this, Faq.class));
                    MainActivity.this.finish();
                }*/
                return false;
            }
        });
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


        setupDrawerHeader();

    }

    private void setupDrawerHeader() {
        String userName = SaveCache.getString(Home.this, "userName");

        welcomeText.setText("Welcome");
        textViewUserName.setText(userName);

        if (!userName.equals("") && !userName.isEmpty()) {
            String[] old = userName.split(" ");
            StringBuilder add = new StringBuilder();
            for (String s : old)
                add.append(s.charAt(0));

            String text = add.toString();
            text = text.length() < 2 ? text : text.substring(0, 2);

            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getRandomColor();

            TextDrawable drawable = TextDrawable.builder().buildRound(text, color); // radius in px
            userImage.setImageDrawable(drawable);
        }

        headerUser.setOnClickListener( view -> {
            Intent i = new Intent(Home.this, EditProfile.class);
            startActivity(i);
        });
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.main_drawer);

        View header = mNavigationView.inflateHeaderView(R.layout.drawer_header);
        headerUser = header.findViewById(R.id.headerUser);
        textViewUserName = header.findViewById(R.id.userName);
        welcomeText = header.findViewById(R.id.welcomeText);
        userImage = header.findViewById(R.id.userImage);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (exit) {
            finish();
            System.exit(0);
        } else {
            Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(() -> exit = false, 3 * 1000);

        }
    }
}
