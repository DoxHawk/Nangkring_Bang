package com.example.nangkringbang.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nangkringbang.Activity.Activity_Menu_Detail;
import com.example.nangkringbang.Activity.Activity_Profile;
import com.example.nangkringbang.Model.Model_Keranjang;
import com.example.nangkringbang.Model.Model_Menu;
import com.example.nangkringbang.Model.Model_Menu_Detail;
import com.example.nangkringbang.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Adapter_Cart extends FirestoreRecyclerAdapter<Model_Keranjang, Adapter_Cart.CartHolder> {

    private FirebaseUser mUser;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private Context context;
    private int total = 0;

    private jumlahHarga mJumlahHarga;
    jumlahHarga jumlahharga;

    private static final String PROFILE = "users";
    private static final String FAVORITE = "favorit";
    private static final String MENU = "menu";
    private static final String CART = "keranjang";

    public interface jumlahHarga {
        void totalHarga(int i);
    }

    public Adapter_Cart(@NonNull @NotNull FirestoreRecyclerOptions<Model_Keranjang> options, FirebaseUser mUser) {
        super(options);
        this.mUser = mUser;
    }

    public void setJumlahHarga(jumlahHarga jml) {
        this.mJumlahHarga = jml;
    }

    private String formatRupiah(int number) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

    @Override
    protected void onBindViewHolder(@NonNull CartHolder holder, int position,
                                    @NonNull Model_Keranjang model) {
        holder.namaMenu.setText(model.getCart_nama());
        holder.hrgMenu.setText(formatRupiah(model.getCart_harga()));
        holder.qty.setText(String.valueOf(model.getCart_qty()));
        holder.total.setText(formatRupiah(model.getCart_total()));

        if (model.getCart_img() != null) {
            storageReference.child("foto makanan").child(model.getCart_img().get(0)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.placeholder_img)
                            .fit().centerInside().into(holder.imgCart, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {

                            Picasso.get().load(uri).fit().centerInside().into(holder.imgCart);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    holder.imgCart.setImageResource(R.drawable.placeholder_img);
                }
            });
        }

        //checkStok
        firebaseFirestore.collection(MENU).document(getSnapshots().getSnapshot(position).getId())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value,
                                        @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(context, "Error load stok", Toast.LENGTH_SHORT).show();
                            Log.d("TAG_KERJANG", error.toString());
                        } else if (value != null) {
                            Model_Menu menu = value.toObject(Model_Menu.class);
                            if (menu.getMenu_stok() - 1 <= 0){
                                holder.btn_add.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Toast.makeText(context, "Tidak ada sisa produk", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else if (model.getCart_qty() == 1){
                                holder.btn_min.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        AlertDialog.Builder alert = new AlertDialog.Builder(context);

                                        alert.setTitle("Hapus dari keranjang");
                                        alert.setMessage("Anda yakin ?");

                                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            String id = getSnapshots().getSnapshot(position).getId();
                                            int stok = model.getCart_qty();

                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                firebaseFirestore.collection(PROFILE)
                                                        .document(mUser.getUid())
                                                        .collection(CART)
                                                        .document(id)
                                                        .delete()
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                firebaseFirestore.collection(MENU).document(id).update("menu_stok", menu.getMenu_stok()+stok);
                                                            }
                                                        });
                                            }
                                        });

                                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                            }
                                        });

                                        alert.show();
                                    }
                                });
                            }
                            holder.btn_add.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Map<String, Object> item = new HashMap<>();
                                    item.put("cart_qty", model.getCart_qty() + 1);
                                    item.put("cart_total", (model.getCart_qty() + 1) * menu.getMenu_harga());
                                    firebaseFirestore.collection(PROFILE).document(mUser.getUid()).collection(CART).document(getSnapshots().getSnapshot(position).getId()).update(item);
                                    firebaseFirestore.collection(MENU).document(getSnapshots().getSnapshot(position).getId()).update("menu_stok", menu.getMenu_stok() - 1);
                                }
                            });
                            holder.btn_min.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Map<String, Object> item = new HashMap<>();
                                    item.put("cart_qty", model.getCart_qty() - 1);
                                    item.put("cart_total", (model.getCart_qty() - 1) * menu.getMenu_harga());
                                    firebaseFirestore.collection(PROFILE).document(mUser.getUid()).collection(CART).document(getSnapshots().getSnapshot(position).getId()).update(item);
                                    firebaseFirestore.collection(MENU).document(getSnapshots().getSnapshot(position).getId()).update("menu_stok", menu.getMenu_stok() + 1);
                                }
                            });
                            holder.btn_del.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    AlertDialog.Builder alert = new AlertDialog.Builder(context);

                                    alert.setTitle("Hapus dari keranjang");
                                    alert.setMessage("Anda yakin ?");

                                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        String id = getSnapshots().getSnapshot(position).getId();
                                        int stok = model.getCart_qty();

                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            firebaseFirestore.collection(PROFILE)
                                                    .document(mUser.getUid())
                                                    .collection(CART)
                                                    .document(id)
                                                    .delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            firebaseFirestore.collection(MENU).document(id).update("menu_stok", menu.getMenu_stok()+stok);
                                                        }
                                                    });
                                        }
                                    });

                                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                        }
                                    });

                                    alert.show();
                                }
                            });
                        }
                    }
                });

        int total = +model.getCart_total();
        if (jumlahharga != null) {
            jumlahharga.totalHarga(total);
        }
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        jumlahharga = this.mJumlahHarga;
        context = view.getContext();
        return new CartHolder(view);
    }

    public class CartHolder extends RecyclerView.ViewHolder {
        TextView a_rating;
        TextView a_ulasan;
        TextView hrgMenu;
        ImageView imgCart;
        TextView namaMenu;
        EditText qty;
        RatingBar ratingBar;
        TextView total;
        ImageButton btn_del, btn_min, btn_add;

        public CartHolder(View itemView) {
            super(itemView);
            namaMenu = itemView.findViewById(R.id.cartName);
            a_rating = itemView.findViewById(R.id.cartArating);
            a_ulasan = itemView.findViewById(R.id.cartReview);
            hrgMenu = itemView.findViewById(R.id.cartHrg);
            qty = itemView.findViewById(R.id.cartHrg2);
            ratingBar = itemView.findViewById(R.id.rating1);
            imgCart = itemView.findViewById(R.id.imgCart);
            total = itemView.findViewById(R.id.cartTotal);
            btn_del = itemView.findViewById(R.id.btn_del);
            btn_min = itemView.findViewById(R.id.keranjangMin);
            btn_add = itemView.findViewById(R.id.keranjangAdd);
        }
    }
}
