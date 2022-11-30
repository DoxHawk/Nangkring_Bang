package com.example.nangkringbang.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Fragment_Register extends Fragment {

    private View view;
    private Context context;
    private Fragment_Login _fragmentLogin;
    private TextInputEditText e_email, e_nama, e_pass, e_user;
    private TextInputLayout l_email, l_nama, l_pass, l_user;
    private String s_email, s_nama, s_pass, s_user;
    private Snackbar snackbar;
    private TextView txtLogin;
    private Button btnRegister;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;


    public void onViewCreated(View view2, Bundle savedInstanceState) {
        super.onViewCreated(view2, savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_register, container, false);
        view = inflate;
        btnRegister = (Button) inflate.findViewById(R.id.btnReg);
        txtLogin = (TextView) view.findViewById(R.id.txtLogin2);
        l_nama = (TextInputLayout) view.findViewById(R.id.txtNama);
        l_user = (TextInputLayout) view.findViewById(R.id.txtUser);
        l_email = (TextInputLayout) view.findViewById(R.id.txtEmail);
        l_pass = (TextInputLayout) view.findViewById(R.id.txtPass);
        e_nama = (TextInputEditText) view.findViewById(R.id.e_Nama);
        e_user = (TextInputEditText) view.findViewById(R.id.e_User);
        e_email = (TextInputEditText) view.findViewById(R.id.e_Email);
        e_pass = (TextInputEditText) view.findViewById(R.id.e_Pass);
        _fragmentLogin = new Fragment_Login();
        context = view.getContext();

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth             = FirebaseAuth.getInstance();

        txtLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment_Register _FragmentRegister = Fragment_Register.this;
                _FragmentRegister.load(_FragmentRegister._fragmentLogin);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                actionReg();
            }
        });
        return view;
    }

    private void load(Fragment fragment) {
        ((AppCompatActivity) this.context).getSupportFragmentManager().beginTransaction().replace(R.id.viewLayout, fragment).commit();
    }

    public void actionReg(){
        s_nama        = l_nama.getEditText().getText().toString().trim();
        s_email       = l_email.getEditText().getText().toString().trim();
        s_user        = l_user.getEditText().getText().toString().trim();
        s_pass        = l_pass.getEditText().getText().toString().trim();

        if (!validateName() | !validateEmail() | !validateUser() | !validatePass()){
            return;
        }
        regUser(s_nama, s_email, s_user, s_pass);
    }

    private boolean validateName(){
        if (s_nama.isEmpty()) {
            l_nama.setError(getResources().getString(R.string.s_e_empty));
            return false;
        }else {
            l_nama.setError(null);
            return true;
        }
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

    private boolean validateUser(){
        if (s_user.isEmpty()) {
            l_user.setError(getResources().getString(R.string.s_e_empty));
            return false;
        }else {
            l_user.setError(null);
            return true;
        }
    }

    private boolean validatePass(){
        if (s_pass.isEmpty()) {
            l_pass.setError(getResources().getString(R.string.s_e_empty));
            return false;
        }else if (s_pass.length() < 8) {
            l_pass.setError(getResources().getString(R.string.s_e_passmin));
            return false;
        }
//        else if (!passConf.equals(pass)) {
//            lPassConf.setError(getResources().getString(R.string.s_e_missmatch));
//            return false;
//        }
        else{
            l_pass.setError(null);
            return true;
        }
    }

    private void regUser(String name, String email, String username, String password) {

        ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Mohon Tunggu!");
        pd.show();

        mAuth.createUserWithEmailAndPassword(email , password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                // Create a new user with a first and last name
                Map<String, Object> user = new HashMap<>();
                user.put("user_nama", name);
                user.put("user_alamat", "default");
                user.put("user_email", email);
                user.put("user_telp", "default");
                user.put("user_type", "pengguna");
                user.put("user_username", username);
                user.put("user_img", "default");
                user.put("user_register", FieldValue.serverTimestamp());

                //Add new
                firebaseFirestore.collection("users").document(authResult.getUser().getUid())
                        .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()){
                            pd.dismiss();
                            Toast.makeText(context, "Selamat datang, silahkan lengkapi profil anda", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Fragment_Register.this.getActivity() , Activity_Main.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            Fragment_Register.this.getActivity().finish();
                        }else if (task.getException() instanceof FirebaseAuthUserCollisionException){
                            Toast.makeText(context, "Anda telah terdaftar, silahkan masuk", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context, getResources().getString(R.string.s_t_failed), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

//    private void validasi() {
//        if (e_nama.getText().toString().trim().isEmpty()) {
//            l_nama.setError("Please input your name");
//            l_nama.setErrorEnabled(true);
//        } else if (e_user.getText().toString().trim().isEmpty()) {
//            l_user.setError("Your username is empty");
//            l_user.setErrorEnabled(true);
//        } else if (e_email.getText().toString().trim().isEmpty()) {
//            l_email.setError("Email is invalid");
//            l_email.setErrorEnabled(true);
//        } else if (e_pass.getText().toString().trim().isEmpty()) {
//            l_pass.setError("Password cannot be empty");
//            l_pass.setErrorEnabled(true);
//        } else {
//            s_nama = e_nama.getText().toString();
//            s_user = e_user.getText().toString();
//            s_email = e_email.getText().toString();
//            s_pass = e_pass.getText().toString();
//            l_nama.setErrorEnabled(false);
//            l_user.setErrorEnabled(false);
//            l_email.setErrorEnabled(false);
//            l_pass.setErrorEnabled(false);
//            Volley.newRequestQueue(context).add(new StringRequest(1, getResources().getString(R.string.register), new Response.Listener<String>() {
//                public void onResponse(String response) {
//                    try {
//                        Log.d("tag", "onErrorResponse: " + response);
//                        if (new JSONArray(response).getJSONObject(0).getString("reply") == "1") {
//                            Snackbar snackbar = Fragment_Register.this.snackbar;
//                            Snackbar.make(Fragment_Register.this.view, (CharSequence) "Your account has registered", Snackbar.LENGTH_LONG).show();
//                            Fragment_Register.this.startActivity(new Intent(Fragment_Register.this.getActivity(), Activity_Main.class));
//                            Fragment_Register.this.getActivity().finish();
//                            new Handler().postDelayed(new Runnable() {
//                                public void run() {
//                                    Fragment_Register.this.load(Fragment_Register.this._fragmentLogin);
//                                }
//                            }, (long) (1 * 1000));
//                            return;
//                        }
//                        Snackbar snackbar2 = Fragment_Register.this.snackbar;
//                        Snackbar.make(Fragment_Register.this.view, (CharSequence) "Failed to regiter your account", Snackbar.LENGTH_LONG).show();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, new Response.ErrorListener() {
//                public void onErrorResponse(VolleyError error) {
//                    Log.d("tag", "onErrorResponse: " + error.getMessage());
//                }
//            }) {
//                /* access modifiers changed from: protected */
//                public Map<String, String> getParams() {
//                    Map<String, String> params = new HashMap<>();
//                    params.put("fullName", Fragment_Register.this.s_nama);
//                    params.put("userName", Fragment_Register.this.s_user);
//                    params.put(NotificationCompat.CATEGORY_EMAIL, Fragment_Register.this.s_email);
//                    params.put("passWord", Fragment_Register.this.s_pass);
//                    return params;
//                }
//            });
//        }
//    }
}
