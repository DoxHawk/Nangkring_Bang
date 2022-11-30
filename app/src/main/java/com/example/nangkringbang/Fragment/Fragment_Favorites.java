package com.example.nangkringbang.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nangkringbang.Adapter.Adapter_Menu;
import com.example.nangkringbang.Model.Model_Menu;
import com.example.nangkringbang.R;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Favorites extends Fragment {
    Adapter_Menu adpt_f;
    ImageButton btnBack;
    ImageButton btnCart;
    /* access modifiers changed from: private */
    public Context context;
    List<Model_Menu> modelMenus;
    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    TextView textView1;
    TextView textView2;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorit, container, false);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView4);
        this.btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        this.btnCart = (ImageButton) view.findViewById(R.id.btnCart);
        this.textView1 = (TextView) view.findViewById(R.id.txtBar);
        this.sharedPreferences = view.getContext().getSharedPreferences("Pref", 0);
        this.btnBack.setVisibility(View.GONE);
        this.textView1.setText("Favorit");
        this.context = inflater.getContext();
        this.modelMenus = new ArrayList();
//        Favorite();
        this.btnCart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((AppCompatActivity) v.getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.viewLayout, new Fragment_Cart()).addToBackStack((String) null).commit();
            }
        });
        return view;
    }

//    private void Favorite() {
//        Volley.newRequestQueue(this.context).add(new StringRequest(1, getResources().getString(R.string.menu), new Response.Listener<String>() {
//            Float rating;
//
//            public void onResponse(String response) {
//                try {
//                    JSONArray values = new JSONArray(response);
//                    for (int i = 0; i < values.length(); i++) {
//                        JSONObject jsonObject = values.getJSONObject(i);
//                        Model_Menu modelMenu = new Model_Menu();
//                        modelMenu.setId_menu(jsonObject.getString("id_menu"));
//                        modelMenu.setId_kategori(jsonObject.getString("id_kategori"));
//                        modelMenu.setNama_menu(jsonObject.getString("nama_menu"));
//                        modelMenu.setImg_menu(jsonObject.getString("img_menu"));
//                        modelMenu.setHrgMenu(jsonObject.getInt("harga_menu"));
//                        modelMenu.setFavSts(jsonObject.getInt("favSts"));
//                        modelMenu.setDeskripsi(jsonObject.getString("desc_menu"));
//                        Float valueOf = Float.valueOf(jsonObject.getString("rating"));
//                        this.rating = valueOf;
//                        modelMenu.setRating(valueOf.floatValue());
//                        modelMenu.setJmlUlasan(jsonObject.getInt("jmlUlasan"));
//                        Fragment_Favorites.this.modelMenus.add(modelMenu);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                Fragment_Favorites.this.recyclerView.setLayoutManager(new AutoFitGridLayoutManager(Fragment_Favorites.this.context));
//                Fragment_Favorites.this.adpt_f = new Adapter_Menu(Fragment_Favorites.this.context, Fragment_Favorites.this.modelMenus);
//                Fragment_Favorites.this.recyclerView.setAdapter(Fragment_Favorites.this.adpt_f);
//            }
//        }, new Response.ErrorListener() {
//            public void onErrorResponse(VolleyError error) {
//                Log.d("tag", "onErrorResponse: " + error.getMessage());
//            }
//        }) {
//            /* access modifiers changed from: protected */
//            public Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("userName", Fragment_Favorites.this.sharedPreferences.getString("KEY_USER", ""));
//                params.put("level", "Fav");
//                return params;
//            }
//        });
//    }
}
