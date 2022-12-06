package com.example.nangkringbang.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.nangkringbang.Adapter.Adapter_Menu;
import com.example.nangkringbang.Model.Model_Menu;
import com.example.nangkringbang.databinding.ActivityViewBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Activity_Category_Menus extends AppCompatActivity {

    private ActivityViewBinding main;
    private TextView txtTitle, txtTitleDesc;
    private RecyclerView recyclerView;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Query query;
    private Adapter_Menu adpt_m;

    private String menuKatId, katName;
    private static final String MENU = "menu";
    private static final String TAG = "Activity_Category_Menus";

    @Override
    public void onStart() {
        super.onStart();
        adpt_m.startListening();
        adpt_m.notifyDataSetChanged();
        recyclerView.getRecycledViewPool().clear();
    }

    @Override
    public void onPause() {
        super.onPause();
        adpt_m.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        adpt_m.startListening();
        adpt_m.notifyDataSetChanged();
        recyclerView.getRecycledViewPool().clear();
    }

    @Override
    public void onStop() {
        super.onStop();
        adpt_m.stopListening();
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
                katName = getIntent().getExtras().getString("KEY_CATEGORY_DETAIL_NAME");
                menuKatId = getIntent().getExtras().getString("KEY_CATEGORY_DETAIL_ID");

                txtTitle.setText("Kategori");
                txtTitleDesc.setText(katName);
                txtTitleDesc.setVisibility(View.VISIBLE);

                query = firebaseFirestore.collection(MENU).whereEqualTo("menu_kategori", menuKatId);
                FirestoreRecyclerOptions<Model_Menu> options = new FirestoreRecyclerOptions.Builder<Model_Menu>()
                        .setQuery(query, Model_Menu.class)
                        .build();

//        AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(Fragment_Home.this.getActivity());
                GridLayoutManager layoutManager = new GridLayoutManager(Activity_Category_Menus.this, 2, GridLayoutManager.VERTICAL, false);
                adpt_m = new Adapter_Menu(options, mUser, main.getRoot());
                recyclerView.setAdapter(adpt_m);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setNestedScrollingEnabled(true);

                adpt_m.setOnItemClickListener(new Adapter_Menu.OnItemClickListener() {
                    @Override
                    public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                        if (documentSnapshot != null) {
                            Intent menuIntent = new Intent(Activity_Category_Menus.this, Activity_Menu_Detail.class);
                            menuIntent.putExtra("KEY_MENU_ID", documentSnapshot.getId());
                            if (menuIntent.resolveActivity(Activity_Category_Menus.this.getPackageManager()) != null) {
                                Activity_Category_Menus.this.startActivity(menuIntent);
                            }
                        }
                    }
                });
            }
        }
    }
}