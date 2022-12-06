package com.example.nangkringbang.Activity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.nangkringbang.Adapter.Adapter_Image_Slide;
import com.example.nangkringbang.Model.Model_Menu;
import com.example.nangkringbang.R;
import com.example.nangkringbang.databinding.ActivityMenuDetailBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Activity_Menu_Detail extends AppCompatActivity {

    private ActivityMenuDetailBinding main;
    private TextView namaMenu, hrgMenu, descMenu;
    private ViewPager imgMenu;
    private TabLayout imgIndicator;
    private ImageButton btn_fav, btn_cart;

    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private String menuId;
    private static final String PROFILE = "users";
    private static final String FAVORITE = "favorit";
    private static final String MENU = "menu";
    private static final String CART = "keranjang";
    private List<String> mList = new ArrayList<>();

    private int version = Build.VERSION.SDK_INT;

    private String formatRupiah(int number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = ActivityMenuDetailBinding.inflate(getLayoutInflater());
        setContentView(main.getRoot());
        namaMenu = main.namaMenu;
        hrgMenu = main.hrgMenu;
        descMenu = main.descMenu2;
        btn_fav = main.btnFav;
        btn_cart = main.addCart;
        imgMenu = main.imgMenu;
        imgIndicator = main.menuIndicator;

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        mAuth       = FirebaseAuth.getInstance();
        mUser       = mAuth.getCurrentUser();


        if (getIntent() != null) {
            if (mUser != null){
                menuId = getIntent().getExtras().getString("KEY_MENU_ID");

                firebaseFirestore.collection(MENU).document(menuId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(Activity_Menu_Detail.this, "Error loading menu", Toast.LENGTH_SHORT).show();
                            Log.d("TAG_PROFILE", error.toString());
                        } else if (value != null) {
                            Model_Menu menu = value.toObject(Model_Menu.class);

                            namaMenu.setText(menu.getMenu_nama());
                            descMenu.setText(menu.getMenu_desk());
                            hrgMenu.setText(formatRupiah(menu.getMenu_harga()));
                            mList = menu.getMenu_img();

                            //Setup ViewPager
                            Adapter_Image_Slide adapter_image_viewPager = new Adapter_Image_Slide(Activity_Menu_Detail.this, mList, "foto makanan");
                            imgMenu.setAdapter(adapter_image_viewPager);
                            imgMenu.setOffscreenPageLimit(2);

                            //setup tab layout with viewpager
                            imgIndicator.setupWithViewPager(imgMenu);

                            //Check favorite
                            firebaseFirestore.collection(PROFILE)
                                    .document(mUser.getUid())
                                    .collection(FAVORITE)
                                    .document(menuId)
                                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value,
                                                            @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                                            if (error != null) {
                                                Toast.makeText(Activity_Menu_Detail.this, "Error loading data", Toast.LENGTH_SHORT).show();
                                                Log.d("TAG_FAV", error.toString());
                                            } else if (value.exists()) {
                                                btn_fav.setImageResource(R.drawable.circle_2);
                                                btn_fav.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        firebaseFirestore.collection(PROFILE)
                                                                .document(mUser.getUid())
                                                                .collection(FAVORITE)
                                                                .document(menuId)
                                                                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    btn_fav.setImageResource(R.drawable.circle_1);
                                                                    btn_fav.invalidate();
                                                                    Snackbar.make(main.getRoot(), "Berhasil hapus favorit", Snackbar.LENGTH_LONG).show();
                                                                } else if (!task.isSuccessful()) {
                                                                    Snackbar.make(main.getRoot(), "Gagal hapus favorit", Snackbar.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            } else {
                                                btn_fav.setImageResource(R.drawable.circle_1);
                                                btn_fav.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        Map<String, Object> item = new HashMap<>();
                                                        item.put("fav_nama", menu.getMenu_nama());
                                                        item.put("fav_desk", String.valueOf(menu.getMenu_harga()));
                                                        item.put("fav_img", menu.getMenu_img());
                                                        item.put("fav_type", "menu");

                                                        firebaseFirestore.collection(PROFILE)
                                                                .document(mUser.getUid())
                                                                .collection(FAVORITE)
                                                                .document(menuId)
                                                                .set(item).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    btn_fav.setImageResource(R.drawable.circle_2);
                                                                    btn_fav.invalidate();
                                                                    Snackbar.make(main.getRoot(), "Berhasil tambah favorit", Snackbar.LENGTH_LONG).show();
                                                                } else if (!task.isSuccessful()) {
                                                                    Snackbar.make(main.getRoot(), "Gagal tambah favorit", Snackbar.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });

                            //addCart
                            if (menu.getMenu_stok() != 0){
                                btn_cart.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        AlertDialog.Builder alert = new AlertDialog.Builder(Activity_Menu_Detail.this);

                                        alert.setTitle("Tambah keranjang");
                                        alert.setMessage("Masukan quantity");

                                        // Set an EditText view to get user input
                                        final EditText input = new EditText(Activity_Menu_Detail.this);
                                        input.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);
                                        alert.setView(input);

                                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                String qty = input.getText().toString();
                                                if (qty != null){
                                                    if(Integer.parseInt(qty) > menu.getMenu_stok()){
                                                        Snackbar.make(main.getRoot(), "Quantity melebihi stok", Snackbar.LENGTH_SHORT).show();
                                                    }else if (Integer.parseInt(qty) <= 0){
                                                        Snackbar.make(main.getRoot(), "Angka tidak valid", Snackbar.LENGTH_SHORT).show();
                                                    }else {
                                                        Map<String, Object> item = new HashMap<>();
                                                        item.put("cart_nama", menu.getMenu_nama());
                                                        item.put("cart_harga", menu.getMenu_harga());
                                                        item.put("cart_qty", Integer.parseInt(qty));
                                                        item.put("cart_total", (Integer.parseInt(qty)) * menu.getMenu_harga());
                                                        item.put("cart_img", menu.getMenu_img());

                                                        firebaseFirestore.collection(PROFILE).document(mUser.getUid()).collection(CART)
                                                                .document(menuId)
                                                                .set(item)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Snackbar.make(main.getRoot(), "Berhasil tambah keranjang", Snackbar.LENGTH_LONG).show();
                                                                            firebaseFirestore.collection(MENU).document(menuId).update("menu_stok", menu.getMenu_stok() - Integer.parseInt(qty));
                                                                        } else if (!task.isSuccessful()) {
                                                                            Snackbar.make(main.getRoot(), "Gagal tambah keranjang", Snackbar.LENGTH_LONG).show();
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                }
                                            }
                                        });

                                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                            }
                                        });

                                        alert.show();
                                    }
                                });
                            }else{
                                btn_cart.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Snackbar.make(main.getRoot(), "Maaf stok kosong", Snackbar.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }
                });
            }
        }

        statusBar(this.version);
        }

        @Override
        protected void onPause () {
            super.onPause();
        }

        @Override
        protected void onResume () {
            super.onResume();
        }

        @Override
        protected void onStop () {
            super.onStop();
        }

        private void statusBar ( int build){
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
