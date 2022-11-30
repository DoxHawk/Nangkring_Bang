package com.example.nangkringbang.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.nangkringbang.Model.Ulasan;
import com.example.nangkringbang.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Adapter_Ulasan extends RecyclerView.Adapter<Adapter_Ulasan.ViewHolder> {
    Date date;
    SimpleDateFormat dateFormat1;
    SimpleDateFormat dateFormat2;
    LayoutInflater inflater;
    String tanggal;
    List<Ulasan> ulasans;

    public Adapter_Ulasan(Context ctx, List<Ulasan> ulasans2) {
        this.inflater = LayoutInflater.from(ctx);
        this.ulasans = ulasans2;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.inflater.inflate(R.layout.item_rev, parent, false);
        this.dateFormat1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        this.dateFormat2 = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.namaUser.setText(this.ulasans.get(position).getNamaUser());
        try {
            Date parse = this.dateFormat1.parse(this.ulasans.get(position).getTanggal());
            this.date = parse;
            this.tanggal = this.dateFormat2.format(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tanggal.setText(this.tanggal);
        holder.ratingBar.setRating(this.ulasans.get(position).getRating());
        if (this.ulasans.get(position).getImgUser().isEmpty()) {
            holder.imgUser.setBackgroundResource(R.drawable.ic_launcher_background);
        } else {
            Picasso.get().load(this.ulasans.get(position).getImgUser()).fit().centerInside().into(holder.imgUser);
        }
        holder.ulasan.setText(this.ulasans.get(position).getReview());
    }

    public int getItemCount() {
        return this.ulasans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgUser;
        TextView namaUser;
        RatingBar ratingBar;
        TextView tanggal;
        TextView ulasan;

        public ViewHolder(View itemView) {
            super(itemView);
            this.namaUser = (TextView) itemView.findViewById(R.id.txtNamaUser);
            this.tanggal = (TextView) itemView.findViewById(R.id.txtTanggal);
            this.ulasan = (TextView) itemView.findViewById(R.id.txtReviewUser);
            this.ratingBar = (RatingBar) itemView.findViewById(R.id.ratingUser);
            this.imgUser = (ImageView) itemView.findViewById(R.id.imgUser);
            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int pos = ViewHolder.this.getAdapterPosition();
                    Context context = v.getContext();
                    Toast.makeText(context, "Ini adalah menu " + pos, 0).show();
                }
            });
        }
    }
}
