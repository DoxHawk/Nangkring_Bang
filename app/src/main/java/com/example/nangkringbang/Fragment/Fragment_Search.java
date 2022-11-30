package com.example.nangkringbang.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nangkringbang.Adapter.Adapter_Menu;
import com.example.nangkringbang.Function.AutoFitGridLayoutManager;
import com.example.nangkringbang.Model.Model_Menu;
import com.example.nangkringbang.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fragment_Search extends Fragment {
    /* access modifiers changed from: private */
    public Adapter_Menu adpt_m;
    /* access modifiers changed from: private */
    public Context context;
    /* access modifiers changed from: private */

    /* renamed from: fm */
    public FragmentManager f30fm;
    /* access modifiers changed from: private */
    public List<Model_Menu> modelMenus;
    /* access modifiers changed from: private */
    public RecyclerView recyclerView;
    /* access modifiers changed from: private */
    public SharedPreferences sharedPreferences;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_cari, container, false);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView7);
        TextInputLayout textInputLayout = (TextInputLayout) view.findViewById(R.id.txtSearch);
        this.sharedPreferences = view.getContext().getSharedPreferences("Pref", 0);
        ((ImageButton) view.findViewById(R.id.btnCart)).setVisibility(View.GONE);
        ((ImageButton) view.findViewById(R.id.btnSearch)).setVisibility(View.GONE);
        ((TextView) view.findViewById(R.id.txtBar)).setText("Pencarian");
        this.context = inflater.getContext();
        this.modelMenus = new ArrayList();
//        Utama();
//        ((ImageButton) view.findViewById(R.id.btnBack)).setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                AppCompatActivity activity = (AppCompatActivity) view.getContext();
//                FragmentManager unused = Fragment_Search.this.f30fm = activity.getSupportFragmentManager();
//                if (Fragment_Search.this.f30fm.getBackStackEntryCount() > 0) {
//                    Fragment_Search.this.f30fm.popBackStack();
//                    activity.finish();
//                    return;
//                }
//                activity.finish();
//            }
//        });
//        ((TextInputEditText) view.findViewById(R.id.e_Search)).addTextChangedListener(new TextWatcher() {
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                if (s == null) {
//                    Fragment_Search.this.recyclerView.setVisibility(View.GONE);
//                }
//            }
//
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Fragment_Search.this.adpt_m.getFilter().filter(s);
//                Fragment_Search.this.recyclerView.setVisibility(View.GONE);
//            }
//
//            public void afterTextChanged(Editable s) {
//            }
//        });
//        view.setOnKeyListener(new View.OnKeyListener() {
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode != 4) {
//                    return false;
//                }
//                Fragment_Search.this.f30fm.popBackStack();
//                return true;
//            }
//        });
        return view;
    }

//    private void Utama() {
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
//                        Fragment_Search.this.modelMenus.add(modelMenu);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                Fragment_Search.this.recyclerView.setLayoutManager(new AutoFitGridLayoutManager(Fragment_Search.this.context));
//                Adapter_Menu unused = Fragment_Search.this.adpt_m = new Adapter_Menu(Fragment_Search.this.context, Fragment_Search.this.modelMenus);
//                Fragment_Search.this.recyclerView.setAdapter(Fragment_Search.this.adpt_m);
//                Fragment_Search.this.recyclerView.setNestedScrollingEnabled(true);
//                Fragment_Search.this.recyclerView.setVisibility(View.GONE);
//            }
//        }, new Response.ErrorListener() {
//            public void onErrorResponse(VolleyError error) {
//                Log.d("tag", "onErrorResponse: " + error.getMessage());
//            }
//        }) {
//            /* access modifiers changed from: protected */
//            public Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("userName", Fragment_Search.this.sharedPreferences.getString("KEY_USER", ""));
//                params.put("level", "Men");
//                return params;
//            }
//        });
//    }
}
