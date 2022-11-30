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

import com.example.nangkringbang.Model.Model_Tempat;
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

public class Adapter_Places extends FirestoreRecyclerAdapter<Model_Tempat, Adapter_Places.TempatHolder> {

    private StorageReference storageReference;
    private OnItemClickListener listener;
    private Context context;
    private DisplayMetrics metrics;

    public Adapter_Places(@NonNull @NotNull FirestoreRecyclerOptions<Model_Tempat> options, DisplayMetrics metrics) {
        super(options);
        this.metrics = metrics;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull TempatHolder holder, int position,
                                    @NonNull @NotNull Model_Tempat model) {

        if(model.getTempat_nama() != null){
            holder.txt_nama.setText(model.getTempat_nama());
        }

        if(model.getTempat_lokasi() != null){
//            if(model.getMenu_harga().matches("\\d+(?:\\.\\d+)?"))
            holder.txt_lokasi.setText(model.getTempat_lokasi());
//        else
//        {
//            holder.txt_price.setText(model.getBiaya());
//        }
        }
        if(model.getTempat_img() != null){
            storageReference.child("foto tempat").child(model.getTempat_img().get(0)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
    public TempatHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tempat, parent, false);
        storageReference    = FirebaseStorage.getInstance().getReference();
        context             = view.getContext();
        view.getLayoutParams().width = (int) (metrics.heightPixels/2.5);
        view.requestLayout();
        return new TempatHolder(view);
    }

    class TempatHolder extends RecyclerView.ViewHolder{
        TextView txt_nama, txt_lokasi;
//        ShapeableImageView img;
        ImageView img;

        public TempatHolder(@NonNull @NotNull View itemView) {
            super(itemView);
//            float radius = context.getResources().getDimension(R.dimen._11sdp);
            txt_nama   = itemView.findViewById(R.id.namaTempat);
            txt_lokasi = itemView.findViewById(R.id.lokasiTempat);
            img        = itemView.findViewById(R.id.imgTempat);
//            img.setShapeAppearanceModel(img.getShapeAppearanceModel()
//                    .toBuilder()
//                    .setAllCorners(CornerFamily.ROUNDED, radius)
//                    .build());

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