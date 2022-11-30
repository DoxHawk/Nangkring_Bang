package com.example.nangkringbang.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nangkringbang.Model.Model_Menu;
import com.example.nangkringbang.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;
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
import java.util.Locale;
import java.util.Map;

public class Adapter_Menu extends FirestoreRecyclerAdapter<Model_Menu, Adapter_Menu.MenuHolder> {

    private StorageReference storageReference;
    private OnItemClickListener listener;
    private Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser mUser;

    public Adapter_Menu(@NonNull @NotNull FirestoreRecyclerOptions<Model_Menu> options, FirebaseUser mUser) {
        super(options);
        this.mUser = mUser;
    }

    private String formatRupiah(int number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull MenuHolder holder, int position,
                                    @NonNull @NotNull Model_Menu model) {

            holder.txt_title.setText(model.getMenu_nama());
            holder.txt_price.setText(formatRupiah(model.getMenu_harga()));

        if(model.getMenu_img() != null){
            storageReference.child("foto makanan").child(model.getMenu_img().get(0)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.placeholder_img)
                            .fit().centerInside().into(holder.img, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {

                            Picasso.get().load(uri).fit().centerInside().into(holder.img);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    holder.img.setImageResource(R.drawable.placeholder_img);
                }
            });
        }

        //Check favorite
        firebaseFirestore.collection("users")
                .document(mUser.getUid())
                .collection("favorit")
                .document(getSnapshots().getSnapshot(position).getId())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value,
                                        @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(context, "Error loading data", Toast.LENGTH_SHORT).show();
                            Log.d("TAG_FAV", error.toString());
                        } else if (value.exists()) {
                            holder.btn_fav.setImageResource(R.drawable.circle_2);
                            holder.btn_fav.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    firebaseFirestore.collection("users")
                                            .document(mUser.getUid())
                                            .collection("favorit")
                                            .document(getSnapshots().getSnapshot(position).getId())
                                            .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                holder.btn_fav.setImageResource(R.drawable.circle_1);
                                                holder.btn_fav.invalidate();
                                                Toast.makeText(context, "Berhasil menghapus item", Toast.LENGTH_SHORT).show();
                                            } else if (!task.isSuccessful()) {
                                                Toast.makeText(context, "Gagal menghapus item", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        } else {
                            holder.btn_fav.setImageResource(R.drawable.circle_1);
                            holder.btn_fav.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Map<String, Object> item = new HashMap<>();
                                    item.put("fav_nama", model.getMenu_nama());
                                    item.put("fav_desk", model.getMenu_harga());
                                    item.put("fav_img", model.getMenu_img());
                                    item.put("fav_type", "menu");

                                    firebaseFirestore.collection("users")
                                            .document(mUser.getUid())
                                            .collection("favorit")
                                            .document(getSnapshots().getSnapshot(position).getId())
                                            .set(item).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                holder.btn_fav.setImageResource(R.drawable.circle_2);
                                                holder.btn_fav.invalidate();
                                                Toast.makeText(context, "Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
                                            } else if (!task.isSuccessful()) {
                                                Toast.makeText(context, "Gagal Ditambahkan", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position != RecyclerView.NO_POSITION && listener != null){
                    listener.onItemClick(getSnapshots().getSnapshot(position), position);
                }
            }
        });

    }

    @NonNull
    @NotNull
    @Override
    public MenuHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        firebaseFirestore   = FirebaseFirestore.getInstance();
        storageReference    = FirebaseStorage.getInstance().getReference();
        context             = view.getContext();
        return new MenuHolder(view);
    }

    class MenuHolder extends RecyclerView.ViewHolder{
        TextView txt_title, txt_desc, txt_age, txt_price;
        ShapeableImageView img;
        ImageButton btn_fav;

        public MenuHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            float radius = context.getResources().getDimension(R.dimen._11sdp);
            txt_title = itemView.findViewById(R.id.namaMenu);
//            txt_desc  = itemView.findViewById(R.id.txtDesc);
//            txt_age   = itemView.findViewById(R.id.txtAge);
            txt_price = itemView.findViewById(R.id.hrgMenu);
            btn_fav   = itemView.findViewById(R.id.btn_fav);
            img       = itemView.findViewById(R.id.coverImage);
            img.setShapeAppearanceModel(img.getShapeAppearanceModel()
                    .toBuilder()
                    .setAllCorners(CornerFamily.ROUNDED, radius)
                    .build());

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int position = getBindingAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION && listener != null){
//                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
//                    }
//                }
//            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

}