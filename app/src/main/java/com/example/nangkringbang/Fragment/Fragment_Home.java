package com.example.nangkringbang.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nangkringbang.Activity.Activity_Menu_Detail;
import com.example.nangkringbang.Activity.Activity_Profile;
import com.example.nangkringbang.Activity.Activity_Tempat_Detail;
import com.example.nangkringbang.Adapter.Adapter_Menu;
import com.example.nangkringbang.Adapter.Adapter_Places;
import com.example.nangkringbang.Model.Model_Menu;
import com.example.nangkringbang.Model.Model_Profile;
import com.example.nangkringbang.Model.Model_Tempat;
import com.example.nangkringbang.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_Home extends Fragment {
    Adapter_Places adpt_t;
    Adapter_Menu adpt_m;
    private View view;
    private ImageButton btnSearch;
    private Context context;
    private RecyclerView recyclerView1, recyclerView2;
    private TextView txtGreetingUser, txtTanggal, txtGreetDay;
    private DisplayMetrics metrics;
    private CircleImageView imgProfile;

    private static final String MENU = "menu";
    private static final String PLACES = "tempat";
    private static final String PROFILE = "users";
    private static final String TAG = "Fragment_Home";
    private Query query1, query2;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private StorageReference storageReference;

    @Override
    public void onStart() {
        super.onStart();
        adpt_m.startListening();
        adpt_t.startListening();
        adpt_m.notifyDataSetChanged();
        adpt_t.notifyDataSetChanged();
        recyclerView1.getRecycledViewPool().clear();
        recyclerView2.getRecycledViewPool().clear();
    }

    @Override
    public void onPause() {
        super.onPause();
        adpt_m.stopListening();
        adpt_t.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        adpt_m.startListening();
        adpt_t.startListening();
        adpt_m.notifyDataSetChanged();
        adpt_t.notifyDataSetChanged();
        recyclerView1.getRecycledViewPool().clear();
        recyclerView2.getRecycledViewPool().clear();
    }

    @Override
    public void onStop() {
        super.onStop();
        adpt_m.stopListening();
        adpt_t.stopListening();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_beranda, container, false);
        recyclerView1 = view.findViewById(R.id.recyclerView1);
        recyclerView2 = view.findViewById(R.id.recyclerView2);
        imgProfile = view.findViewById(R.id.imgUserHome);
        btnSearch = view.findViewById(R.id.btnSearch);
        txtGreetingUser = view.findViewById(R.id.txtGreetingUser);
        txtTanggal = view.findViewById(R.id.txtTanggal);
        txtGreetDay = view.findViewById(R.id.txtGreetDay);
        metrics = new DisplayMetrics();
        context = inflater.getContext();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference  = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        SimpleDateFormat sfd = new SimpleDateFormat("EEEE, dd LLLL yyyy");
        Date now = new Date();
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        txtTanggal.setText(sfd.format(now));
        if(timeOfDay >= 0 && timeOfDay < 12){
            txtGreetDay.setText("Selamat pagi");
        }else if(timeOfDay >= 12 && timeOfDay < 15){
            txtGreetDay.setText("Selamat siang");
        }else if(timeOfDay >= 15 && timeOfDay < 18){
            txtGreetDay.setText("Selamat sore");
        }else if(timeOfDay >= 18 && timeOfDay < 24){
            txtGreetDay.setText("Selamat malam");
        }


        getMenus();
        getPlace();
        profile();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

    private void profile() {
        if (mAuth.getCurrentUser() != null) {
            firebaseFirestore.collection(PROFILE)
                    .document(mUser.getUid())
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                Toast.makeText(context, "Error loading profile", Toast.LENGTH_SHORT).show();
                                Log.d("TAG_PROFILE", error.toString());
                            } else if (value != null) {
                                Model_Profile model = value.toObject(Model_Profile.class);
                                txtGreetingUser.setText(model.getUser_nama());

                                if (!model.getUser_img().equals("default")) {
                                    storageReference.child("foto profile").child(model.getUser_img()).getDownloadUrl()
                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    Picasso.get().load(uri).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.placeholder_img)
                                                            .fit().centerInside().into(imgProfile, new Callback() {
                                                        @Override
                                                        public void onSuccess() {

                                                        }

                                                        @Override
                                                        public void onError(Exception e) {
                                                            Picasso.get().load(uri).fit().centerInside().into(imgProfile);
                                                        }
                                                    });
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            imgProfile.setImageResource(R.drawable.placeholder_img);
                                        }
                                    });
                                }
                            }
                        }
                    });
        }

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Fragment_Home.this.getActivity() , Activity_Profile.class);
                Fragment_Home.this.getActivity().startActivity(intent);
            }
        });
    }

    private void getMenus() {
        query1 = firebaseFirestore.collection(MENU).orderBy("menu_stok", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Model_Menu> options = new FirestoreRecyclerOptions.Builder<Model_Menu>()
                .setQuery(query1, Model_Menu.class)
                .build();

//        AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(Fragment_Home.this.getActivity());
        GridLayoutManager layoutManager = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
        adpt_m = new Adapter_Menu(options, mUser);
        recyclerView2.setAdapter(adpt_m);
        recyclerView2.setLayoutManager(layoutManager);
        recyclerView2.setNestedScrollingEnabled(true);

        adpt_m.setOnItemClickListener(new Adapter_Menu.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                if (documentSnapshot != null) {
                    Intent menuIntent = new Intent(Fragment_Home.this.getActivity(), Activity_Menu_Detail.class);
                    menuIntent.putExtra("KEY_MENU_ID", documentSnapshot.getId());
                    if (menuIntent.resolveActivity(Fragment_Home.this.getActivity().getPackageManager()) != null) {
                        Fragment_Home.this.getActivity().startActivity(menuIntent);
                    }
                }
            }
        });
    }

    private void getPlace() {
        query2 = firebaseFirestore.collection(PLACES).orderBy("tempat_buka", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Model_Tempat> options = new FirestoreRecyclerOptions.Builder<Model_Tempat>()
                .setQuery(query2, Model_Tempat.class)
                .build();

        LinearLayoutManager layoutManager = new LinearLayoutManager(Fragment_Home.this.getActivity(), LinearLayoutManager.HORIZONTAL, false);
        adpt_t = new Adapter_Places(options, metrics);
        recyclerView1.setAdapter(adpt_t);
        recyclerView1.setLayoutManager(layoutManager);
        recyclerView1.setNestedScrollingEnabled(true);

        adpt_t.setOnItemClickListener(new Adapter_Places.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                if (documentSnapshot != null) {
                    Intent placeIntent = new Intent(Fragment_Home.this.getActivity(), Activity_Tempat_Detail.class);
                    placeIntent.putExtra("KEY_TEMPAT_ID", documentSnapshot.getId());
                    if (placeIntent.resolveActivity(Fragment_Home.this.getActivity().getPackageManager()) != null) {
                        Fragment_Home.this.getActivity().startActivity(placeIntent);
                    }
                }
            }
        });
    }
}
