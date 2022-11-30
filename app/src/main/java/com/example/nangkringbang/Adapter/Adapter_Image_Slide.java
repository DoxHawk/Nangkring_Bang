package com.example.nangkringbang.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.nangkringbang.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_Image_Slide extends PagerAdapter {
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private String child;
    Context mContext;
    List<String> mListScreen;

    public Adapter_Image_Slide(Context mCOntext, List<String> mListScreen, String child){
        this.mContext = mCOntext;
        this.mListScreen = mListScreen;
        this.child = child;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position){
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen       = inflater.inflate(R.layout.model_imagepager, null);
        ImageView imgSlide      = layoutScreen.findViewById(R.id.intro_img);

        firebaseStorage         = FirebaseStorage.getInstance();
        storageReference        = firebaseStorage.getReference();
        for (String url : mListScreen) {
            storageReference.child(child).child(url).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.placeholder_img)
                            .fit().centerInside().into(imgSlide, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {

                            Picasso.get().load(uri).fit().centerCrop().into(imgSlide);
                        }
                    });
                    Log.d("TAG_SLIDER", "Sukses : "+uri);
                }
            });
        }

        Log.d("TAG_SLIDER", "GAGAL");
        imgSlide.setImageResource(R.color.colorAccent);

        container.addView(layoutScreen);
        return layoutScreen;
    }

    @Override
    public int getCount(){
        return mListScreen.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o){
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object){
        container.removeView((View) object);
    }
}
