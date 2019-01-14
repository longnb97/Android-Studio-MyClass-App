package longnb.xda.edu.btlandroid;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ForgotPassword extends AppCompatActivity {
    RelativeLayout loadingPanel;
    Button submit;
    EditText email;
    Boolean sendingRequest;
    Button login;
    Boolean btnCooldown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        init();
        try {
            if (getIntent().hasExtra("email")) {
                email.setText(getIntent().getStringExtra("email"));
            }
        } catch (Exception e) {

        }
    }
    public static ForgotPassword context;
    public static ForgotPassword getInstance(){ return context; }
    public void init() {
        context = this;
        final RequestQueue requestQueue = Volley.newRequestQueue(ForgotPassword.this);
        sendingRequest = btnCooldown = false;
        login = findViewById(R.id.login);
        loadingPanel = findViewById(R.id.loadingPanel);
        loadingPanel.setVisibility(View.GONE);
        submit = findViewById(R.id.submit);
        email = findViewById(R.id.email);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sendingRequest || btnCooldown) {
                    Toast.makeText(ForgotPassword.this, "Xin chờ một chút rồi thử lại", Toast.LENGTH_LONG).show();
                } else {
                    loadingPanel.setVisibility(View.VISIBLE);
                    String mail = email.getText().toString();
                    if (mail.equals("")) {
                        loadingPanel.setVisibility(View.GONE);
                        Toast.makeText(ForgotPassword.this, "Mail không được để trống", Toast.LENGTH_LONG).show();
                    } else {
                        loadingPanel.setVisibility(View.VISIBLE);
                        sendingRequest = true;
                        postData(mail);
                    }
                }
                btnCooldown = true;
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                btnCooldown = false;
                            }
                        }, 2000);
            }

            public void postData(String mail) {
                RequestQueue requestQueue = Volley.newRequestQueue(ForgotPassword.this);
                String url = "https://btlandroid.herokuapp.com/auth/forgot-password?email=" + mail;

                JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                loadingPanel.setVisibility(View.GONE);
                        sendingRequest = false;
                                Toast.makeText(ForgotPassword.this, "Tin nhắn đã được gửi đến mail của bạn, xin kiểm tra hòm thư", Toast.LENGTH_LONG).show();
                                startApplication();
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                loadingPanel.setVisibility(View.GONE);
                                sendingRequest = false;
                                Toast.makeText(ForgotPassword.this, "Lỗi hệ thống", Toast.LENGTH_LONG).show();
                            }
                        });
                requestQueue.add(jsObjRequest);
                jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            }
        });
    }

    public void startApplication(){
        try
        {
            Intent mailClient = new Intent(Intent.ACTION_VIEW);
            mailClient.setClassName("com.google.android.gm", "com.google.android.gm.ConversationListActivity");
            startActivity(mailClient);
        }
        catch (Exception e)
        {
            Toast.makeText(ForgotPassword.this, "Can't find Gmail", Toast.LENGTH_LONG).show();
        }
    }
}