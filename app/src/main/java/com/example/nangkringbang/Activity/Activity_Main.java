package com.example.nangkringbang.Activity;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.nangkringbang.Fragment.Fragment_Home;
import com.example.nangkringbang.Fragment.Fragment_Explore;
import com.example.nangkringbang.Fragment.Fragment_Cart;
import com.example.nangkringbang.Fragment.Fragment_Orders;
import com.example.nangkringbang.R;
import com.example.nangkringbang.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Activity_Main extends AppCompatActivity {

    private ActivityMainBinding main;
    private BottomNavigationView bottomNavigationView;
    private int version = Build.VERSION.SDK_INT;
    private FrameLayout viewLayout;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onStart() {
        super.onStart();
        mUser               = mAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main                = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(main.getRoot());
        viewLayout          = main.viewlayout1;
        bottomNavigationView= main.bottomNavigation;
        mAuth               = FirebaseAuth.getInstance();
        statusBar(version);

        loadFragment(new Fragment_Home());
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(MenuItem item) {
                viewLayout.removeAllViews();
                switch (item.getItemId()) {
                    case R.id.action_home:
                        Activity_Main.this.loadFragment(new Fragment_Home());
                        return true;
                    case R.id.action_explore:
                        Activity_Main.this.loadFragment(new Fragment_Explore());
                        return true;
                    case R.id.action_orders:
                        Activity_Main.this.loadFragment(new Fragment_Orders());
                        return true;
                    case R.id.action_cart:
                        Activity_Main.this.loadFragment(new Fragment_Cart());
                        return true;
                    default:
                        Activity_Main.this.loadFragment(new Fragment_Home());
                        return true;
                }
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = this.getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
            this.getSupportFragmentManager().beginTransaction().replace(R.id.viewlayout1, fragment).commit();
        }
        this.getSupportFragmentManager().beginTransaction().replace(R.id.viewlayout1, fragment).commit();
    }

    private void statusBar(int build) {
        if (build >= 23) {
            getWindow().getDecorView().setSystemUiVisibility(8192);
            return;
        }
        Window window = getWindow();
        window.clearFlags(67108864);
        window.addFlags(Integer.MIN_VALUE);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
    }
}
