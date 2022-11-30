package com.example.nangkringbang.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.nangkringbang.Model.Model_Menu;
import com.example.nangkringbang.R;

import java.util.List;

public class Adapter_Orders_Detail extends RecyclerView.Adapter<Adapter_Orders_Detail.ViewHolder> {
    LayoutInflater inflater;
    List<Model_Menu> modelMenus;

    public Adapter_Orders_Detail(Context ctx, List<Model_Menu> menus2) {
        this.inflater = LayoutInflater.from(ctx);
        this.modelMenus = menus2;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(this.inflater.inflate(R.layout.item_cart, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
//        holder.namaMenu.setText(this.modelMenus.get(position).getNama_menu());
//        TextView textView = holder.hrgMenu;
//        textView.setText("Rp. " + String.valueOf(this.modelMenus.get(position).getHrgMenu()));
//        holder.qty.setText(String.valueOf(this.modelMenus.get(position).getQtyMenu()));
//        holder.ratingBar.setRating(this.modelMenus.get(position).getRating());
//        if (this.modelMenus.get(position).getRating() > 0.0f) {
//            holder.a_rating.setText(String.valueOf(this.modelMenus.get(position).getRating()));
//        } else {
//            holder.a_rating.setVisibility(8);
//        }
//        TextView textView2 = holder.a_ulasan;
//        textView2.setText("(" + this.modelMenus.get(position).getJmlUlasan() + " Ulasan)");
//        Picasso picasso = Picasso.get();
//        picasso.load("http://expresseat.formulainfor.com/img/menu/" + this.modelMenus.get(position).getImgMenu()).fit().centerInside().into(holder.imgCart);
//        holder.total.setText(String.valueOf(this.modelMenus.get(position).getTotal()));
    }

    public int getItemCount() {
        return this.modelMenus.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView namaMenu, a_rating, a_ulasan, hrgMenu;
        ImageView imgCart;
        EditText qty;
        RatingBar ratingBar;
        TextView total;

        public ViewHolder(View itemView) {
            super(itemView);
            this.namaMenu = itemView.findViewById(R.id.cartName);
            this.a_rating = itemView.findViewById(R.id.cartArating);
            this.a_ulasan = itemView.findViewById(R.id.cartReview);
            this.hrgMenu = itemView.findViewById(R.id.cartHrg);
            this.qty = (EditText) itemView.findViewById(R.id.cartHrg2);
            this.ratingBar = (RatingBar) itemView.findViewById(R.id.rating1);
            this.imgCart = (ImageView) itemView.findViewById(R.id.imgCart);
            this.total = itemView.findViewById(R.id.cartTotal);
        }
    }
}
