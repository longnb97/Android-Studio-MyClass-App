package longnb.xda.edu.btlandroid;

import android.content.Intent;
import android.support.design.widget.Snackbar;
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

public class ChangePassword extends AppCompatActivity {
    RelativeLayout load;
    EditText oldPassword, password, retypePasword;
    Button change;
    Boolean sendingRequest;
    String url = "https://btlandroid.herokuapp.com/auth/password";
    JSONObject jsonObject;
    String email;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        try {
            if (getIntent().hasExtra("email")) {
                email = getIntent().getStringExtra("email");
                init();
            } else {
                Toast.makeText(ChangePassword.this, "Email not found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            return;
        }

    }

    private static ChangePassword context;

    public ChangePassword getInstance() {
        return context;
    }

    public void init() {
        context = this;
        sendingRequest = false;
        load = findViewById(R.id.loadingPanel);
        oldPassword = findViewById(R.id.oldPassword);
        password = findViewById(R.id.password);
        retypePasword = findViewById(R.id.retypePasword);
        change = findViewById(R.id.change);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        load.setVisibility(View.GONE);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pd = password.getText().toString();
                String repd = retypePasword.getText().toString();
                String opd = oldPassword.getText().toString();
                jsonObject = new JSONObject();
                if (pd.equals("") || repd.equals("") || opd.equals("")) {
                    Toast.makeText(ChangePassword.this, "Hãy nhập đầy đủ thông tin", Toast.LENGTH_LONG).show();

                } else {
                    if (!repd.equals(pd)) {
                        Toast.makeText(ChangePassword.this, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            load.setVisibility(View.VISIBLE);
                            jsonObject.put("email", email);
                            jsonObject.put("password", opd);
                            jsonObject.put("newPassword", pd);

                        } catch (JSONException e) {
                            load.setVisibility(View.GONE);
                        }
                        postToApi(jsonObject);
                        sendingRequest = true;
                    }
                }
            }
        });
    }

    public void postToApi(JSONObject jsonObj) {
        if (sendingRequest) {
            Toast.makeText(ChangePassword.this, "xin chờ 1 chút rồi thử lại", Toast.LENGTH_SHORT).show();
        } else {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            load.setVisibility(View.GONE);
                            sendingRequest = false;
                            //handle response

                            try {
                                String data = response.getString("message");
                                if (data.equals("200")) {
                                    Toast.makeText(ChangePassword.this, "Đổi mật khẩu thành công!, xin đăng nhập lại", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(ChangePassword.this, MainActivity.class);
                                    context.getInstance().startActivity(intent);
                                } else if (data.equals("204")) {
                                    Toast.makeText(ChangePassword.this, "Sai mật khẩu hiện hành", Toast.LENGTH_LONG).show();

                                } else if (data.equals("400")) {
                                    Toast.makeText(ChangePassword.this, "Mật khẩu mới phải khác mật khẩu cũ", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(ChangePassword.this, "Đổi mật khẩu thất bại", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {

                            } finally {
                                emptyText();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            load.setVisibility(View.GONE);
                            sendingRequest = false;
                            Toast.makeText(ChangePassword.this, "Lỗi hệ thống", Toast.LENGTH_LONG).show();
                        }
                    });
            requestQueue.add(jsObjRequest);
        }
    }

    public void emptyText() {
        password.setText("");
        retypePasword.setText("");
        oldPassword.setText("");
    }
}
