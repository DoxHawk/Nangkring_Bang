package com.example.nangkringbang.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nangkringbang.Adapter.Adapter_Menu;
import com.example.nangkringbang.Fragment.Fragment_Home;
import com.example.nangkringbang.Model.Model_Menu;
import com.example.nangkringbang.databinding.ActivityViewBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Activity_Home_Menu extends AppCompatActivity {

    private ActivityViewBinding main;
    private TextView txtTitle;
    private RecyclerView recyclerView;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Query query;
    private Adapter_Menu adpt_m;

    private static final String MENU = "menu";
    private static final String TAG = "Activity_Home_Menu";

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

        txtTitle.setText("Recommended Menus");

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        if (mUser != null){
            query = firebaseFirestore.collection(MENU).orderBy("menu_stok", Query.Direction.DESCENDING);
            FirestoreRecyclerOptions<Model_Menu> options = new FirestoreRecyclerOptions.Builder<Model_Menu>()
                    .setQuery(query, Model_Menu.class)
                    .build();

//        AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(Fragment_Home.this.getActivity());
            GridLayoutManager layoutManager = new GridLayoutManager(Activity_Home_Menu.this, 2, GridLayoutManager.VERTICAL, false);
            adpt_m = new Adapter_Menu(options, mUser, main.getRoot());
            recyclerView.setAdapter(adpt_m);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setNestedScrollingEnabled(true);

            adpt_m.setOnItemClickListener(new Adapter_Menu.OnItemClickListener() {
                @Override
                public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                    if (documentSnapshot != null) {
                        Intent menuIntent = new Intent(Activity_Home_Menu.this, Activity_Menu_Detail.class);
                        menuIntent.putExtra("KEY_MENU_ID", documentSnapshot.getId());
                        if (menuIntent.resolveActivity(Activity_Home_Menu.this.getPackageManager()) != null) {
                            Activity_Home_Menu.this.startActivity(menuIntent);
                        }
                    }
                }
            });
        }
    }

}
