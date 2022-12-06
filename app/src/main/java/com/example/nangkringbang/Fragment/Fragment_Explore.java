package com.example.nangkringbang.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nangkringbang.Activity.Activity_Category_Menus;
import com.example.nangkringbang.Activity.Activity_Category_Places;
import com.example.nangkringbang.Activity.Activity_Explore_Category;
import com.example.nangkringbang.Activity.Activity_Explore_Favorite;
import com.example.nangkringbang.Activity.Activity_Menu_Detail;
import com.example.nangkringbang.Activity.Activity_Tempat_Detail;
import com.example.nangkringbang.Adapter.Adapter_Category;
import com.example.nangkringbang.Adapter.Adapter_Favorite;
import com.example.nangkringbang.Adapter.Adapter_Menu;
import com.example.nangkringbang.Adapter.Adapter_Places;
import com.example.nangkringbang.Model.Model_Favorit;
import com.example.nangkringbang.Model.Model_Kategori;
import com.example.nangkringbang.Model.Model_Menu;
import com.example.nangkringbang.Model.Model_Tempat;
import com.example.nangkringbang.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class Fragment_Explore extends Fragment {

    private View view;
    private Context context;
    private TextView txtKatView, txtFavView;
    private RecyclerView recyclerView1, recyclerView2, recyclerView3, recyclerView4;
    private Adapter_Menu adpt_m;
    private Adapter_Places adpt_t;
    private Adapter_Category adpt_c;
    private Adapter_Favorite adpt_f;
    private DisplayMetrics metrics;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Query query1, query2, query3, query4;

    private static final String PROFILE = "users";
    private static final String MENU = "menu";
    private static final String PLACES = "tempat";
    private static final String CATEGORY = "kategori";
    private static final String FAVORITE = "favorit";
    private static final String TAG = "Fragment_Explore";

    public Fragment_Explore() {
    }

    @Override
    public void onStart() {
        super.onStart();
        adpt_m.startListening();
        adpt_t.startListening();
        adpt_c.startListening();
        adpt_f.startListening();
        adpt_m.notifyDataSetChanged();
        adpt_t.notifyDataSetChanged();
        adpt_c.notifyDataSetChanged();
        adpt_f.notifyDataSetChanged();
        recyclerView1.getRecycledViewPool().clear();
        recyclerView2.getRecycledViewPool().clear();
        recyclerView3.getRecycledViewPool().clear();
        recyclerView4.getRecycledViewPool().clear();
    }

    @Override
    public void onPause() {
        super.onPause();
        adpt_m.stopListening();
        adpt_c.startListening();
        adpt_t.stopListening();
        adpt_f.startListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        adpt_m.startListening();
        adpt_t.startListening();
        adpt_c.startListening();
        adpt_f.startListening();
        adpt_m.notifyDataSetChanged();
        adpt_t.notifyDataSetChanged();
        adpt_c.notifyDataSetChanged();
        adpt_f.notifyDataSetChanged();
        recyclerView1.getRecycledViewPool().clear();
        recyclerView2.getRecycledViewPool().clear();
        recyclerView3.getRecycledViewPool().clear();
        recyclerView4.getRecycledViewPool().clear();
    }

    @Override
    public void onStop() {
        super.onStop();
        adpt_m.stopListening();
        adpt_t.stopListening();
        adpt_c.stopListening();
        adpt_f.stopListening();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_explore, container, false);
        context = view.getContext();
        recyclerView1 = view.findViewById(R.id.recyclerView7);
        recyclerView2 = view.findViewById(R.id.recyclerView8);
        recyclerView3 = view.findViewById(R.id.recyclerView10);
        recyclerView4 = view.findViewById(R.id.recyclerView12);
        txtFavView = view.findViewById(R.id.txtFavView);
        txtKatView = view.findViewById(R.id.txtKatView);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        getMenus();
        getPlace();
        getCategory();
        getFavs ();
        return view;
    }

    private void getMenus() {
        query1 = firebaseFirestore.collection(MENU).orderBy("menu_harga", Query.Direction.DESCENDING).limit(4);
        FirestoreRecyclerOptions<Model_Menu> options = new FirestoreRecyclerOptions.Builder<Model_Menu>()
                .setQuery(query1, Model_Menu.class)
                .build();

//        AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(Fragment_Home.this.getActivity());
        GridLayoutManager layoutManager = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
        adpt_m = new Adapter_Menu(options, mUser, view);
        recyclerView1.setAdapter(adpt_m);
        recyclerView1.setLayoutManager(layoutManager);
        recyclerView1.setNestedScrollingEnabled(true);

        adpt_m.setOnItemClickListener(new Adapter_Menu.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                if (documentSnapshot != null) {
                    Intent menuIntent = new Intent(Fragment_Explore.this.getActivity(), Activity_Menu_Detail.class);
                    menuIntent.putExtra("KEY_MENU_ID", documentSnapshot.getId());
                    if (menuIntent.resolveActivity(Fragment_Explore.this.getActivity().getPackageManager()) != null) {
                        Fragment_Explore.this.getActivity().startActivity(menuIntent);
                    }
                }
            }
        });
    }

    private void getPlace() {
        query2 = firebaseFirestore.collection(PLACES);
        FirestoreRecyclerOptions<Model_Tempat> options = new FirestoreRecyclerOptions.Builder<Model_Tempat>()
                .setQuery(query2, Model_Tempat.class)
                .build();

        LinearLayoutManager layoutManager = new LinearLayoutManager(Fragment_Explore.this.getActivity(), LinearLayoutManager.HORIZONTAL, false);
        adpt_t = new Adapter_Places(options, metrics);
        recyclerView2.setAdapter(adpt_t);
        recyclerView2.setLayoutManager(layoutManager);
        recyclerView2.setNestedScrollingEnabled(true);

        adpt_t.setOnItemClickListener(new Adapter_Places.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                if (documentSnapshot != null) {
                    Intent placeIntent = new Intent(Fragment_Explore.this.getActivity(), Activity_Tempat_Detail.class);
                    placeIntent.putExtra("KEY_TEMPAT_ID", documentSnapshot.getId());
                    if (placeIntent.resolveActivity(Fragment_Explore.this.getActivity().getPackageManager()) != null) {
                        Fragment_Explore.this.getActivity().startActivity(placeIntent);
                    }
                }
            }
        });
    }

    private void getCategory() {
        query3 = firebaseFirestore.collection(CATEGORY).limit(4);
        FirestoreRecyclerOptions<Model_Kategori> options = new FirestoreRecyclerOptions.Builder<Model_Kategori>()
                .setQuery(query3, Model_Kategori.class)
                .build();

        LinearLayoutManager layoutManager = new LinearLayoutManager(Fragment_Explore.this.getActivity(), LinearLayoutManager.HORIZONTAL, false);
        adpt_c = new Adapter_Category(options, metrics);
        recyclerView3.setAdapter(adpt_c);
        recyclerView3.setLayoutManager(layoutManager);
        recyclerView3.setNestedScrollingEnabled(true);

        adpt_c.setOnItemClickListener(new Adapter_Category.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Intent intent;
                if (documentSnapshot != null) {
                    Model_Kategori model = documentSnapshot.toObject(Model_Kategori.class);
                    if (model != null) {
                        if (model.getKategori_type().equals("menu")) {
                            intent = new Intent(Fragment_Explore.this.getActivity(), Activity_Category_Menus.class);
                            intent.putExtra("KEY_CATEGORY_DETAIL_NAME", model.getKategori_name());
                            intent.putExtra("KEY_CATEGORY_DETAIL_ID", documentSnapshot.getId());
                            startActivity(intent);
                        } else if (model.getKategori_type().equals("tempat")) {
                            intent = new Intent(Fragment_Explore.this.getActivity(), Activity_Category_Places.class);
                            intent.putExtra("KEY_CATEGORY_DETAIL_NAME", model.getKategori_name());
                            intent.putExtra("KEY_CATEGORY_DETAIL_ID", documentSnapshot.getId());
                            startActivity(intent);
                        }
                    }
                }
            }
        });

        txtKatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Fragment_Explore.this.getActivity(), Activity_Explore_Category.class);
                startActivity(intent);
            }
        });

    }

    private void getFavs (){
        query4 = firebaseFirestore.collection(PROFILE).document(mUser.getUid()).collection(FAVORITE).limit(4);
        FirestoreRecyclerOptions<Model_Favorit> options = new FirestoreRecyclerOptions.Builder<Model_Favorit>()
                .setQuery(query4, Model_Favorit.class)
                .build();

//        AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(Fragment_Home.this.getActivity());
        GridLayoutManager layoutManager = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
        adpt_f = new Adapter_Favorite(options, metrics);
        recyclerView4.setAdapter(adpt_f);
        recyclerView4.setLayoutManager(layoutManager);
        recyclerView4.setNestedScrollingEnabled(true);

        adpt_f.setOnItemClickListener(new Adapter_Favorite.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                if (documentSnapshot != null) {
                    Model_Favorit model = documentSnapshot.toObject(Model_Favorit.class);
                    if (model != null){
                        if (model.getFav_type().equals("menu")){
                            Intent placeIntent = new Intent(Fragment_Explore.this.getActivity(), Activity_Menu_Detail.class);
                            placeIntent.putExtra("KEY_MENU_ID", documentSnapshot.getId());
                            if (placeIntent.resolveActivity(Fragment_Explore.this.getActivity().getPackageManager()) != null) {
                                startActivity(placeIntent);
                            }
                        }else if (model.getFav_type().equals("tempat")){
                            Intent placeIntent = new Intent(Fragment_Explore.this.getActivity(), Activity_Tempat_Detail.class);
                            placeIntent.putExtra("KEY_TEMPAT_ID", documentSnapshot.getId());
                            if (placeIntent.resolveActivity(Fragment_Explore.this.getActivity().getPackageManager()) != null) {
                                startActivity(placeIntent);
                            }
                        }
                    }
                }
            }
        });

        txtFavView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Fragment_Explore.this.getActivity(), Activity_Explore_Favorite.class);
                startActivity(intent);
            }
        });
    }
}
