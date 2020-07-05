package com.campus.management;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class Splash1 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Employee obj;
    CardView adminPanel, fee, expense, admissions;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash1);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Campus Management");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.header_name_id);
        adminPanel=findViewById(R.id.splash_admin_id);
        fee=findViewById(R.id.splash_fee_id);
        expense=findViewById(R.id.splash_expense_id);
        admissions=findViewById(R.id.splash_reg_id);

        sharedPreferences=getSharedPreferences("myPref",MODE_PRIVATE);
        editor=sharedPreferences.edit();

        Gson gson = new Gson();
        String json = sharedPreferences.getString("user", "");
        obj = gson.fromJson(json, Employee.class);
        navUsername.setText(obj.getEmpName());

        adminPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(obj.getEmpEmail().equals("nor@gmail.com")) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }
        });

        fee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(obj.getAccessFee().equals("given")) {
                    startActivity(new Intent(getApplicationContext(), StudentSelection.class));
                }else{
                    Toast.makeText(getApplicationContext(),"You can't Access Fee Section", Toast.LENGTH_SHORT).show();
                }
            }
        });

        expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(obj.getAccessExpenses().equals("given")) {
                    startActivity(new Intent(getApplicationContext(), ExpenseManager.class));
                }else{
                    Toast.makeText(getApplicationContext(),"You can't Access Expense Section", Toast.LENGTH_SHORT).show();
                }
            }
        });

        admissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(obj.getAcessRegisteration().equals("given")) {
                    startActivity(new Intent(getApplicationContext(), StudentRegisteration.class));
                }else{
                    Toast.makeText(getApplicationContext(),"You can't Access Admissions Section", Toast.LENGTH_SHORT).show();
                }
            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_admin) {
            if(obj.getEmpEmail().equals("nor@gmail.com")) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
            // Handle the camera action
        } else if (id == R.id.nav_expense) {
            if(obj.getAccessExpenses().equals("given")) {
                startActivity(new Intent(getApplicationContext(), ExpenseManager.class));
            }else{
                Toast.makeText(getApplicationContext(),"You can't Access Expense Section", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_fee) {
            if(obj.getAccessFee().equals("given")) {
                startActivity(new Intent(getApplicationContext(), StudentSelection.class));
            }else{
                Toast.makeText(getApplicationContext(),"You can't Access Fee Section", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_logout) {
            editor.putString("userState", "nothing");
            editor.commit();
            startActivity(new Intent(getApplicationContext(), LogIn.class));
            finish();

        } else if (id == R.id.nav_reg) {
            if(obj.getAcessRegisteration().equals("given")) {
                startActivity(new Intent(getApplicationContext(), StudentRegisteration.class));
            }else{
                Toast.makeText(getApplicationContext(),"You can't Access Admissions Section", Toast.LENGTH_SHORT).show();
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
