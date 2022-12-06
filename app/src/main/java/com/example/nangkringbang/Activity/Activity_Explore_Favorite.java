package com.example.nangkringbang.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nangkringbang.Adapter.Adapter_Favorite;
import com.example.nangkringbang.Fragment.Fragment_Explore;
import com.example.nangkringbang.Model.Model_Favorit;
import com.example.nangkringbang.Model.Model_Menu;
import com.example.nangkringbang.databinding.ActivityViewBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Activity_Explore_Favorite extends AppCompatActivity {

    private ActivityViewBinding main;
    private TextView txtTitle, txtTitleDesc;
    private RecyclerView recyclerView;
    private DisplayMetrics metrics;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Query query;
    private Adapter_Favorite adpt_f;

    private String menuKatId, katName;
    private static final String PROFILE = "users";
    private static final String FAVORITE = "favorit";
    private static final String TAG = "Activity_Explore_Favorite";

    @Override
    public void onStart() {
        super.onStart();
        adpt_f.startListening();
        adpt_f.notifyDataSetChanged();
        recyclerView.getRecycledViewPool().clear();
    }

    @Override
    public void onPause() {
        super.onPause();
        adpt_f.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        adpt_f.startListening();
        adpt_f.notifyDataSetChanged();
        recyclerView.getRecycledViewPool().clear();
    }

    @Override
    public void onStop() {
        super.onStop();
        adpt_f.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main                = ActivityViewBinding.inflate(getLayoutInflater());
        setContentView(main.getRoot());

        txtTitle = main.txtViewTitle;
        recyclerView = main.recyclerView11;
        txtTitleDesc = main.txtViewTitleDesc;

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        if (mUser != null){
            if (getIntent() != null){
//                katName = getIntent().getExtras().getString("KEY_CATEGORY_DETAIL_NAME");
//                menuKatId = getIntent().getExtras().getString("KEY_CATEGORY_DETAIL_ID");

                txtTitle.setText("Favorites");
//                txtTitleDesc.setText(katName);
//                txtTitleDesc.setVisibility(View.VISIBLE);

                metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);

                query = firebaseFirestore.collection(PROFILE).document(mUser.getUid()).collection(FAVORITE);
                FirestoreRecyclerOptions<Model_Favorit> options = new FirestoreRecyclerOptions.Builder<Model_Favorit>()
                        .setQuery(query, Model_Favorit.class)
                        .build();

//        AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(Fragment_Home.this.getActivity());
                GridLayoutManager layoutManager = new GridLayoutManager(Activity_Explore_Favorite.this, 2, GridLayoutManager.VERTICAL, false);
                adpt_f = new Adapter_Favorite(options, metrics);
                recyclerView.setAdapter(adpt_f);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setNestedScrollingEnabled(true);

                adpt_f.setOnItemClickListener(new Adapter_Favorite.OnItemClickListener() {
                    @Override
                    public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                        if (documentSnapshot != null) {
                            Model_Favorit model = documentSnapshot.toObject(Model_Favorit.class);
                            if (model != null){
                                if (model.getFav_type().equals("menu")){
                                    Intent placeIntent = new Intent(Activity_Explore_Favorite.this, Activity_Menu_Detail.class);
                                    placeIntent.putExtra("KEY_MENU_ID", documentSnapshot.getId());
                                    if (placeIntent.resolveActivity(Activity_Explore_Favorite.this.getPackageManager()) != null) {
                                        startActivity(placeIntent);
                                    }
                                }else if (model.getFav_type().equals("tempat")){
                                    Intent placeIntent = new Intent(Activity_Explore_Favorite.this, Activity_Tempat_Detail.class);
                                    placeIntent.putExtra("KEY_TEMPAT_ID", documentSnapshot.getId());
                                    if (placeIntent.resolveActivity(Activity_Explore_Favorite.this.getPackageManager()) != null) {
                                        startActivity(placeIntent);
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }
    }
}