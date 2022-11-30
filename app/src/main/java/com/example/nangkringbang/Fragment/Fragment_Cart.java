package com.example.nangkringbang.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nangkringbang.Activity.Activity_Checkout;
import com.example.nangkringbang.Adapter.Adapter_Cart;
import com.example.nangkringbang.Model.Model_Keranjang;
import com.example.nangkringbang.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.NumberFormat;
import java.util.Locale;

public class Fragment_Cart extends Fragment{
    private View view;
    private Context context;
    private RecyclerView recyclerView;
    private TextView txtCheck1;
    private Button btnCheck;
    public int total = 0;

    private Adapter_Cart adpt_c;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Query query;

    private static final String CART = "keranjang";
    private static final String PROFILE = "users";
    private static final String TAG = "Fragment_Cart";

    @Override
    public void onStart() {
        super.onStart();
        adpt_c.startListening();
        adpt_c.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        adpt_c.startListening();
        adpt_c.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        adpt_c.stopListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adpt_c.stopListening();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_keranjang, container, false);
        recyclerView = view.findViewById(R.id.recyclerView5);
        txtCheck1 = view.findViewById(R.id.txtCheck1);
        btnCheck = view.findViewById(R.id.btnCheck);
        context = inflater.getContext();

        firebaseFirestore   = FirebaseFirestore.getInstance();
        mAuth               = FirebaseAuth.getInstance();
        mUser               = mAuth.getCurrentUser();
        mUser               = mAuth.getCurrentUser();
        getCart();
        return view;
    }

    private String formatRupiah(int number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

    private void getCart(){
        if (mUser != null){
            query = firebaseFirestore.collection(PROFILE).document(mUser.getUid()).collection(CART);
            FirestoreRecyclerOptions<Model_Keranjang> options = new FirestoreRecyclerOptions.Builder<Model_Keranjang>()
                    .setQuery(query, Model_Keranjang.class)
                    .build();

            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            adpt_c = new Adapter_Cart(options, mUser);
            recyclerView.setAdapter(adpt_c);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setNestedScrollingEnabled(true);

            firebaseFirestore.collection(PROFILE).document(mUser.getUid()).collection(CART)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    int sum = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Model_Keranjang model = document.toObject(Model_Keranjang.class);
                        sum = sum + model.getCart_total();
                        txtCheck1.setText(formatRupiah(sum));
                    }
                }
            });

            btnCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentCheckout = new Intent(Fragment_Cart.this.getActivity(), Activity_Checkout.class);
                    startActivity(intentCheckout);
                }
            });
        }
    }

    public void totalHarga(int harga) {
        Fragment_Cart.this.txtCheck1.setText(formatRupiah(harga));
    }

}
