package com.example.nangkringbang.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nangkringbang.Activity.Activity_Checkout;
import com.example.nangkringbang.Activity.Activity_Menu_Detail;
import com.example.nangkringbang.Adapter.Adapter_Cart;
import com.example.nangkringbang.Model.Model_Keranjang;
import com.example.nangkringbang.Model.Model_Profile;
import com.example.nangkringbang.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Fragment_Cart extends Fragment {
    private View view;
    private Context context;
    private RecyclerView recyclerView;
    private TextView txtCheck1;
    private Button btnCheck;
    public int sum = 0;

    private Adapter_Cart adpt_c;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Query query;

    private static final String ORDERS = "pesanan";
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

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUser = mAuth.getCurrentUser();
        getCart();
        return view;
    }

    private String formatRupiah(int number) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

    private void getCart() {
        if (mUser != null) {
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
//                    Intent intentCheckout = new Intent(Fragment_Cart.this.getActivity(), Activity_Checkout.class);
//                    startActivity(intentCheckout);
                    if (mUser != null) {
                        firebaseFirestore.collection(PROFILE)
                                .document(mUser.getUid())
                                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot value,
                                                        @Nullable FirebaseFirestoreException error) {
                                        if (error != null) {
                                            Toast.makeText(context, "Error loading profile", Toast.LENGTH_SHORT).show();
                                            Log.d("TAG_PROFILE", error.toString());
                                        } else if (value != null) {
                                            Model_Profile profile = value.toObject(Model_Profile.class);

                                            firebaseFirestore.collection(PROFILE)
                                                    .whereEqualTo("user_type", "admin")
                                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onEvent(@Nullable QuerySnapshot value,
                                                                            @Nullable FirebaseFirestoreException error) {
                                                            if (error != null) {
                                                                Log.d(TAG, error.toString());
                                                            } else if (value.getDocuments().size() > 0) {
                                                                //Insert pesanan admin
                                                                Map<String, Object> itemAdmin = new HashMap<>();
                                                                itemAdmin.put("pesanan_driver", "");
                                                                itemAdmin.put("pesanan_alamat", profile.getUser_alamat());
                                                                itemAdmin.put("pesanan_bukti", "");
                                                                itemAdmin.put("pesanan_catatan", "");
                                                                itemAdmin.put("pesanan_metode", "Cash");
                                                                itemAdmin.put("pesanan_status", "Proses");
                                                                itemAdmin.put("pesanan_sub", sum);
                                                                itemAdmin.put("pesanan_tgl_order", FieldValue.serverTimestamp());
                                                                itemAdmin.put("pesanan_tgl_bayar", FieldValue.serverTimestamp());
                                                                firebaseFirestore.collection(PROFILE).document(mUser.getUid()).collection(ORDERS).
                                                                        add(itemAdmin);

                                                            }
                                                        }
                                                    });

                                            //Insert pesanan user
                                            Map<String, Object> itemUser = new HashMap<>();
                                            itemUser.put("pesanan_bukti", "");
                                            itemUser.put("pesanan_catatan", "");
                                            itemUser.put("pesanan_metode", "Cash");
                                            itemUser.put("pesanan_status", "Proses");
                                            itemUser.put("pesanan_sub", sum);
                                            itemUser.put("pesanan_tgl_order", FieldValue.serverTimestamp());
                                            itemUser.put("pesanan_tgl_bayar", FieldValue.serverTimestamp());
                                            firebaseFirestore.collection(PROFILE).document(mUser.getUid()).collection(ORDERS).
                                                    add(itemUser).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                                            if (error != null) {
                                                                Toast.makeText(context, "Error loading data", Toast.LENGTH_SHORT).show();
                                                                Log.d("TAG_INPUT_CART", error.toString());
                                                            } else if (value.exists()) {
                                                                firebaseFirestore.collection(PROFILE).document(mUser.getUid()).
                                                                        collection(CART).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                                            @Override
                                                                            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value,
                                                                                                @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                                                                                if (error != null){
                                                                                    Log.d(TAG, "onEvent: getDataCart"+error);
                                                                                }else if (value.size() > 0){
                                                                                    for (DocumentSnapshot snapshot : value.getDocuments()){
                                                                                        moveFirestoreDocument(firebaseFirestore.collection(PROFILE).document(mUser.getUid()).collection(CART).document(snapshot.getId()),
                                                                                                firebaseFirestore.collection(PROFILE).document(mUser.getUid()).collection(ORDERS).document(task.getResult().getId()).collection("detail_pesanan").document(snapshot.getId()));
                                                                                    }
                                                                                }
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
            });
        }
    }

    public void moveFirestoreDocument(DocumentReference fromPath, final DocumentReference toPath) {
        fromPath.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        toPath.set(document.getData())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully written!");
                                        fromPath.delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error deleting document", e);
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error writing document", e);
                                    }
                                });
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void totalHarga(int harga) {
        Fragment_Cart.this.txtCheck1.setText(formatRupiah(harga));
    }

}
