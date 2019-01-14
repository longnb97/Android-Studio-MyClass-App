package longnb.xda.edu.btlandroid;


import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {


    RelativeLayout rellay1, rellay2, rellay3;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay3.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };

    RelativeLayout load;
    Boolean isBlurred;
    EditText us;
    EditText pw;
    Button login;
    Button getAll;
    Button forgot;
    String username;
    String password;
    String url = "https://btlandroid.herokuapp.com/auth/login";
    Boolean sendingRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);
        rellay3 = (RelativeLayout) findViewById(R.id.rellay3);

        load = findViewById(R.id.loadingPanel);
//        isBlurred = false;

        load.setVisibility(View.GONE);
        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash

        init();

    }
    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }
    public void init(){
        final UserDetails userDetails = UserDetails.getInstance();
        userDetails.empty();

        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        sendingRequest = false;

        login = findViewById(R.id.login);
        forgot = findViewById(R.id.forgot);
        us = findViewById(R.id.txtUsername);
        pw = findViewById(R.id.txtPassword);
        us.setText("thanhbanat@gmail.com");
        pw.setText("b");
        ButtonEffect btn = new ButtonEffect(login);
        btn.setEffect();

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ForgotPassword.class);
                intent.putExtra("email",us.getText().toString());
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sendingRequest){
                    Toast.makeText(MainActivity.this, "request is sending, please wait", Toast.LENGTH_LONG).show();
                }
                else {
                    sendingRequest = true;
//                    blurall(isBlurred);
                load.setVisibility(View.VISIBLE);
                    final JSONObject jsonObj = new JSONObject();
                    username = us.getText().toString();
                    password = pw.getText().toString();
                    if(username.equals("") ||  password.equals("")){
                        Toast.makeText(MainActivity.this, "Thiếu thông tin đăng nhập", Toast.LENGTH_LONG).show();
                    load.setVisibility(View.GONE);
//                        blurall(isBlurred);
                    }
                    else{
                        try{
                            jsonObj.put("email", username );
                            jsonObj.put("password", password );

                        }catch (JSONException e){
                        load.setVisibility(View.GONE);
//                            blurall(isBlurred);
                        }
                        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,url,jsonObj,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                    load.setVisibility(View.GONE);
                                        sendingRequest = false;
                                        checkAuth(response);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                    load.setVisibility(View.GONE);
//                                        blurall(isBlurred);
                                        sendingRequest = false;
                                        Toast.makeText(MainActivity.this, "Lỗi hệ thống", Toast.LENGTH_LONG).show();
                                    }
                                });
                        requestQueue.add(jsObjRequest);
                    }
                }


            }
        });
    }

    public void checkAuth(JSONObject data){
        if (!data.equals("")){
            try{
                String check = data.getString("success");
                JSONObject userData = data.getJSONObject("userFound");
                String avatarUrl = userData.getString("avatarUrl");
                String email = userData.getString("email");
                String displayname = userData.getString("displayname");
                String classname = userData.getString("class");
                String mainSubject = userData.getString("mainSubject");
                String dob = userData.getString("dob");

                try {
                    UserDetails userDetails = UserDetails.getInstance();
                    userDetails.set(avatarUrl, email, displayname, classname, mainSubject, dob);
                }catch (Exception e){
                    Toast.makeText(MainActivity.this, "!mmmmmmmmmmmm ", Toast.LENGTH_LONG).show();
                }

                if (check == "1") {
                    Toast.makeText(MainActivity.this, "Đăng nhập thành công! ", Toast.LENGTH_LONG).show();
                    // navigate to app dash board
                    Intent intent = new Intent(this, AllStudents.class);
                    startActivity(intent);
                }
                else if(check =="0"){
                    Toast.makeText(MainActivity.this, "Lỗi đăng nhập (success:0)" , Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(MainActivity.this, "Lỗi đăng nhập ", Toast.LENGTH_LONG).show();
                }
            }catch (JSONException e){
                Toast.makeText(MainActivity.this, "Sai thông tin đăng nhập", Toast.LENGTH_LONG).show();
            }
//            blurall(isBlurred);
        }
        else{
            Toast.makeText(MainActivity.this, "data: null", Toast.LENGTH_LONG).show();
        }
    }

//    private void blurall(Boolean isBlurred) {
//
//
//        if (isBlurred) {
//            Blurry.delete((ViewGroup) findViewById(R.id.content));
//        } else {
//            long startMs = System.currentTimeMillis();
//            Blurry.with(MainActivity.this)
//                    .radius(25)
//                    .sampling(2)
//                    .async()
//                    .animate(500)
//                    .onto((ViewGroup) findViewById(R.id.content));
//        }
//        this.isBlurred = !isBlurred;
//    }

}
