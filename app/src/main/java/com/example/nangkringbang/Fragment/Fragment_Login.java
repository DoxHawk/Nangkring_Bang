package com.example.nangkringbang.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.nangkringbang.Activity.Activity_Main;
import com.example.nangkringbang.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Fragment_Login extends Fragment {
    private View view;
    private String s_email, s_pass;
    private TextInputLayout l_email, l_pass;
    private Snackbar snackbar;
    private TextView txtRegister;
    private Button btnLogin;
    private Context context;
    private SharedPreferences.Editor editor;

    private Fragment_Register _fragmentRegister;
    private SharedPreferences sharedPreferences;

    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        btnLogin = view.findViewById(R.id.btnLogin);
        txtRegister = view.findViewById(R.id.txtRegister2);
        l_email= view.findViewById(R.id.txtEmail);
        l_pass = view.findViewById(R.id.txtPass);
        sharedPreferences = view.getContext().getSharedPreferences("Pref", 0);
        _fragmentRegister = new Fragment_Register();
        context = view.getContext();

        mAuth   = FirebaseAuth.getInstance();

        sharedPreferences = view.getContext().getSharedPreferences("Pref", 0);
        txtRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((AppCompatActivity) Fragment_Login.this.context).getSupportFragmentManager().beginTransaction().replace(R.id.viewLayout, Fragment_Login.this._fragmentRegister).commit();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loginAuth();
            }
        });
        return view;
    }

    public void loginAuth(){
        s_email    = l_email.getEditText().getText().toString().trim();
        s_pass     = l_pass.getEditText().getText().toString().trim();

        if (!validateEmail() | !validatePass()){
            return;
        }
        authUser(s_email , s_pass);
    }

    private boolean validateEmail(){
        if (s_email.isEmpty()) {
            l_email.setError(getResources().getString(R.string.s_e_empty));
            return false;
        }else {
            l_email.setError(null);
            return true;
        }
    }

    private boolean validatePass(){
        if (s_pass.isEmpty()) {
            l_pass.setError(getResources().getString(R.string.s_e_empty));
            return false;
        }else {
            l_pass.setError(null);
            return true;
        }
    }

    private void authUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Fragment_Login.this.context, "Selamat Datang", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Fragment_Login.this.getActivity() , Activity_Main.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    Fragment_Login.this.getActivity().finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Fragment_Login.this.context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

//    /* access modifiers changed from: private */
//    public void validasi() {
//        if (this.e_user.getText().toString().trim().isEmpty()) {
//            this.l_user.setError("Username cannot be empty");
//            this.l_user.setErrorEnabled(true);
//        } else if (this.e_pass.getText().toString().trim().isEmpty()) {
//            this.l_user.setError("Password cannot be empty");
//            this.l_user.setErrorEnabled(true);
//        } else {
//            this.s_user = this.e_user.getText().toString();
//            this.s_pass = this.e_pass.getText().toString();
//            this.l_user.setErrorEnabled(false);
//            this.l_pass.setErrorEnabled(false);
//            Profile();
//        }
//    }
//
//    private void Profile() {
//        Volley.newRequestQueue(this.context).add(new StringRequest(1, getResources().getString(R.string.login), new Response.Listener<String>() {
//            String email;
//            String fullName;
//            String imgPengguna;
//            String user;
//
//            public void onResponse(String response) {
//                try {
//                    Log.d("tag", "onErrorResponse: " + response);
//                    JSONObject jsonObject = new JSONArray(response).getJSONObject(0);
//                    this.fullName = jsonObject.getString("nama");
//                    this.email = jsonObject.getString(NotificationCompat.CATEGORY_EMAIL);
//                    this.user = jsonObject.getString("username");
//                    this.imgPengguna = jsonObject.getString("img_pengguna");
//                    Snackbar snackbar = Fragment_Login.this.snackbar;
//                    View view = Fragment_Login.this.view;
//                    Snackbar.make(view, (CharSequence) "Welcome, " + this.user, Snackbar.LENGTH_LONG).show();
//                    new Handler().postDelayed(new Runnable() {
//                        public void run() {
//                            Fragment_Login.this.startActivity(new Intent(Fragment_Login.this.getActivity(), Activity_Main.class));
//                            Fragment_Login.this.getActivity().finish();
//                        }
//                    }, (long) (1 * 1000));
//                    Fragment_Login.this.editor = Fragment_Login.this.sharedPreferences.edit();
//                    Fragment_Login.this.editor.putString("KEY_USER", this.user);
//                    Fragment_Login.this.editor.putString("KEY_EMAIL", this.email);
//                    Fragment_Login.this.editor.commit();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            public void onErrorResponse(VolleyError error) {
//                Log.d("tag", "onErrorResponse: " + error.getMessage());
//            }
//        }) {
//            /* access modifiers changed from: protected */
//            public Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("userName", Fragment_Login.this.s_user);
//                params.put("passWord", Fragment_Login.this.s_pass);
//                return params;
//            }
//        });
//    }
}
