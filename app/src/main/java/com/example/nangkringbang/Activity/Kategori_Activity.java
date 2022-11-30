package com.example.nangkringbang.Activity;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.nangkringbang.R;

public class Kategori_Activity extends AppCompatActivity {
    int version = Build.VERSION.SDK_INT;
    FrameLayout viewLayout;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_menu_detail);
//        this.viewLayout = (FrameLayout) findViewById(R.id.viewlayout2);
//        loadFragment(new Kategori_Fragment());
        statusBar(this.version);
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

//    private void loadFragment(Fragment fragment) {
//        Intent i = getIntent();
//        Bundle args = new Bundle();
//        args.putString("KEY_ID_KATEGORI", i.getStringExtra("KEY_ID_KATEGORI"));
//        args.putString("KEY_NAMA_KATEGORI", i.getStringExtra("KEY_NAMA_KATEGORI"));
//        AppCompatActivity activity = (AppCompatActivity) this.viewLayout.getContext();
//        FragmentManager fm = activity.getSupportFragmentManager();
//        if (fm.getBackStackEntryCount() > 0) {
//            fm.popBackStack();
//            fragment.setArguments(args);
//            activity.getSupportFragmentManager().beginTransaction().replace(R.id.viewlayout2, fragment).commit();
//            return;
//        }
//        fragment.setArguments(args);
//        activity.getSupportFragmentManager().beginTransaction().replace(R.id.viewlayout2, fragment).commit();
//    }
}
