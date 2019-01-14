package longnb.xda.edu.btlandroid;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
        }
    };
    RelativeLayout rellay1, imgUser, view;
    LinearLayout linlay1;
    CircleImageView ava;
    TextView name, classname, email, dob, subject;
    Boolean isSecondTimeRight, isSecondTimeLeft;
    ImageView allStudentTab, profileTab, setting, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        init();
        initSwipe();
    }

    private static UserProfile context;

    public void init() {
        context = this;
        isSecondTimeRight = isSecondTimeLeft = false;
        UserDetails userDetails = UserDetails.getInstance();


        rellay1 = findViewById(R.id.rellay1);
        imgUser = findViewById(R.id.imgUser);
        linlay1 = findViewById(R.id.linlay1);
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Đăng xuất ?", Snackbar.LENGTH_LONG)
                        .setAction("Đồng ý", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(UserProfile.this, MainActivity.class);
                                context.startActivity(intent);
                            }
                        }).show();
                ;
            }
        });

        setting = findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this, ChangePassword.class);
                intent.putExtra("email", email.getText().toString());
                UserProfile.getInstance().startActivity(intent);
            }
        });

        allStudentTab = findViewById(R.id.allStudentTab);
        profileTab = findViewById(R.id.profileTab);
        allStudentTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this, AllStudents.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                UserProfile.getInstance().startActivity(intent);
            }
        });

        ava = findViewById(R.id.ava);
        subject = findViewById(R.id.subject);
        name = findViewById(R.id.name);

        classname = findViewById(R.id.classname);
        email = findViewById(R.id.email);
        dob = findViewById(R.id.dob);

        view = findViewById(R.id.view);

        Picasso.get()
                .load(userDetails.getAva())
                .into(ava);
        name.setText(userDetails.getDisplayName());
        classname.setText(userDetails.getClassname());
        email.setText(userDetails.getEmail());
        dob.setText(userDetails.getDob());
        subject.setText(userDetails.getMainSubject());
    }

    public void initSwipe() {

        view.setOnTouchListener(new OnSwipeTouchListener(UserProfile.this) {
            public void onSwipeRight() {
                if (!isSecondTimeRight) {
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    isSecondTimeRight = false;
                                }
                            }, 2000);
                    Toast.makeText(UserProfile.this, "Trượt lần nữa để back lại", Toast.LENGTH_LONG).show();
                    isSecondTimeRight = true;
                } else {
                    Intent intent = new Intent(UserProfile.this, MainActivity.class);
                    context.startActivity(intent);
                }
            }

            public void onSwipeLeft() {
                Intent intent = new Intent(UserProfile.this, AllStudents.class);
                context.startActivity(intent);

            }

        });
    }

    public static UserProfile getInstance() {
        return context;
    }
}
