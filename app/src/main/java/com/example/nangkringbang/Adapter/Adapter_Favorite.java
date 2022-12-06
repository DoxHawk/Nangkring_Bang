package com.example.nangkringbang.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nangkringbang.Model.Model_Favorit;
import com.example.nangkringbang.Model.Model_Kategori;
import com.example.nangkringbang.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class Adapter_Favorite extends FirestoreRecyclerAdapter<Model_Favorit, Adapter_Favorite.FavoritHolder> {

    private StorageReference storageReference;
    private OnItemClickListener listener;
    private Context context;
    private DisplayMetrics metrics;

    public Adapter_Favorite(@NonNull @NotNull FirestoreRecyclerOptions<Model_Favorit> options, DisplayMetrics metrics) {
        super(options);
        this.metrics = metrics;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull FavoritHolder holder, int position,
                                    @NonNull @NotNull Model_Favorit model) {

        holder.txt_kategori.setText(model.getFav_nama());
        if (model.getFav_type().equals("menu")){
            if (model.getFav_img() != null){
                storageReference.child("foto makanan").child(model.getFav_img().get(0)).getDownloadUrl().
                        addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.get().load(uri).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.placeholder_img)
                                        .fit().centerInside().into(holder.img_kategori, new Callback() {
                                            @Override
                                            public void onSuccess() {

                                            }

                                            @Override
                                            public void onError(Exception e) {

                                                Picasso.get().load(uri).fit().centerInside().into(holder.img_kategori);
                                            }
                                        });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                holder.img_kategori.setImageResource(R.drawable.placeholder_img);
                            }
                        });
            }
        }else{
            storageReference.child("foto tempat").child(model.getFav_img().get(0)).getDownloadUrl().
                    addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.placeholder_img)
                                    .fit().centerInside().into(holder.img_kategori, new Callback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onError(Exception e) {

                                            Picasso.get().load(uri).fit().centerInside().into(holder.img_kategori);
                                        }
                                    });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            holder.img_kategori.setImageResource(R.drawable.placeholder_img);
                        }
                    });
        }

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
    public FavoritHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        storageReference    = FirebaseStorage.getInstance().getReference();
        context             = view.getContext();
//        view.getLayoutParams().width = (int) (metrics.heightPixels/2.5);
//        view.requestLayout();
        return new FavoritHolder(view);
    }

    class FavoritHolder extends RecyclerView.ViewHolder{
        TextView txt_kategori;
        ImageView img_kategori;

        public FavoritHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            txt_kategori    = itemView.findViewById(R.id.namaKategori);
            img_kategori    = itemView.findViewById(R.id.imgKategori);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

}