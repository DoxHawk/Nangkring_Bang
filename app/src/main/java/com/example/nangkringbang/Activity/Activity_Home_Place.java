package com.example.nangkringbang.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nangkringbang.Adapter.Adapter_Places_Full;
import com.example.nangkringbang.Model.Model_Tempat;
import com.example.nangkringbang.databinding.ActivityViewBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Activity_Home_Place extends AppCompatActivity {

    private ActivityViewBinding main;
    private DisplayMetrics metrics;
    private TextView txtTitle;
    private RecyclerView recyclerView;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Query query;
    private Adapter_Places_Full adpt_t;

    private static final String PLACES = "tempat";
    private static final String TAG = "Activity_Home_Place";

    @Override
    public void onStart() {
        super.onStart();
        adpt_t.startListening();
        adpt_t.notifyDataSetChanged();
        recyclerView.getRecycledViewPool().clear();
    }

    @Override
    public void onPause() {
        super.onPause();
        adpt_t.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        adpt_t.startListening();
        adpt_t.notifyDataSetChanged();
        recyclerView.getRecycledViewPool().clear();
    }

    @Override
    public void onStop() {
        super.onStop();
        adpt_t.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main                = ActivityViewBinding.inflate(getLayoutInflater());
        setContentView(main.getRoot());

        txtTitle = main.txtViewTitle;
        recyclerView = main.recyclerView11;
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        txtTitle.setText("Popular Places");

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        if (mUser != null){
            query = firebaseFirestore.collection(PLACES).orderBy("tempat_buka", Query.Direction.DESCENDING);
            FirestoreRecyclerOptions<Model_Tempat> options = new FirestoreRecyclerOptions.Builder<Model_Tempat>()
                    .setQuery(query, Model_Tempat.class)
                    .build();

            LinearLayoutManager layoutManager = new LinearLayoutManager(Activity_Home_Place.this, LinearLayoutManager.VERTICAL, false);
            adpt_t = new Adapter_Places_Full(options);
            recyclerView.setAdapter(adpt_t);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setNestedScrollingEnabled(true);

            adpt_t.setOnItemClickListener(new Adapter_Places_Full.OnItemClickListener() {
                @Override
                public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                    if (documentSnapshot != null) {
                        Intent placeIntent = new Intent(Activity_Home_Place.this, Activity_Tempat_Detail.class);
                        placeIntent.putExtra("KEY_TEMPAT_ID", documentSnapshot.getId());
                        if (placeIntent.resolveActivity(Activity_Home_Place.this.getPackageManager()) != null) {
                            Activity_Home_Place.this.startActivity(placeIntent);
                        }
                    }
                }
            });
        }
    }
}
