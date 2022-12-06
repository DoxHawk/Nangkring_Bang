package com.example.nangkringbang.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nangkringbang.Adapter.Adapter_Image_Slide;
import com.example.nangkringbang.Model.Model_Tempat;
import com.example.nangkringbang.R;
import com.example.nangkringbang.databinding.ActivityTempatDetailBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_Tempat_Detail extends AppCompatActivity {

    private ActivityTempatDetailBinding main;
    private TextView namaTempat, timeTempat, locTempat, txtTemKat, txtTemEmail, txtTemTelp;
    private ViewPager imgTempat;
    private TabLayout imgIndicator;
    private ImageButton btn_fav;

    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private String tempatId;
    private static final String PROFILE = "users";
    private static final String FAVORITE = "favorit";
    private static final String PLACES = "tempat";
    private List<String> tList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = ActivityTempatDetailBinding.inflate(getLayoutInflater());
        setContentView(main.getRoot());

        namaTempat = main.namaTempat;
        txtTemKat = main.txtTemKat;
        txtTemEmail = main.txtTemEmail;
        txtTemTelp = main.txtTemTelp;
        locTempat = main.txtTempat;
        timeTempat = main.txtTime;
        btn_fav = main.btnFav;
        imgTempat = main.imgTempat;
        imgIndicator = main.tempatIndicator;

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        getTempat();
    }

    private void getTempat() {
        if (getIntent() != null) {
            if (mUser != null) {
                tempatId = getIntent().getExtras().getString("KEY_TEMPAT_ID");
                firebaseFirestore.collection(PLACES).document(tempatId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value,
                                        @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(Activity_Tempat_Detail.this, "Error loading tempat", Toast.LENGTH_SHORT).show();
                            Log.d("TAG_TEMPAT", error.toString());
                        } else if (value != null) {
                            Model_Tempat model = value.toObject(Model_Tempat.class);
//                            SimpleDateFormat sfd = new SimpleDateFormat("H:mm");
//                            String buka = sfd.format(model.getTempat_buka());
//                            String tutup = sfd.format(model.getTempat_tutup());
                            namaTempat.setText(model.getTempat_nama());
                            txtTemEmail.setText("Email : "+model.getTempat_email());
                            txtTemTelp.setText("Telp : "+model.getTempat_telp());
                            txtTemKat.setText("Kategori : "+model.getTempat_owner());
                            locTempat.setText(model.getTempat_lokasi());
                            timeTempat.setText(model.getTempat_buka() + " sampai " +model.getTempat_tutup());
                            tList = model.getTempat_img();

                            //Setup ViewPager
                            Adapter_Image_Slide adapter_image_viewPager = new Adapter_Image_Slide(Activity_Tempat_Detail.this, tList, "foto tempat");
                            imgTempat.setAdapter(adapter_image_viewPager);
                            imgTempat.setOffscreenPageLimit(2);

                            //setup tab layout with viewpager
                            imgIndicator.setupWithViewPager(imgTempat);

                            //Check favorite
                            firebaseFirestore.collection(PROFILE)
                                    .document(mUser.getUid())
                                    .collection(FAVORITE)
                                    .document(tempatId)
                                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value,
                                                            @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                                            if (error != null) {
                                                Toast.makeText(Activity_Tempat_Detail.this, "Error loading data", Toast.LENGTH_SHORT).show();
                                                Log.d("TAG_FAV", error.toString());
                                            } else if (value.exists()) {
                                                btn_fav.setImageResource(R.drawable.circle_2);
                                                btn_fav.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        firebaseFirestore.collection(PROFILE)
                                                                .document(mUser.getUid())
                                                                .collection(FAVORITE)
                                                                .document(tempatId)
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
                                                        item.put("fav_nama", model.getTempat_nama());
                                                        item.put("fav_desk", model.getTempat_lokasi());
                                                        item.put("fav_img", model.getTempat_img());
                                                        item.put("fav_type", "tempat");

                                                        firebaseFirestore.collection(PROFILE)
                                                                .document(mUser.getUid())
                                                                .collection(FAVORITE)
                                                                .document(tempatId)
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
                        }
                    }
                });
            }
        }
    }
}