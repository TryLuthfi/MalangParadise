package malang.paradise.com.malangparadise.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import de.hdodenhof.circleimageview.CircleImageView;
import malang.paradise.com.malangparadise.R;
import malang.paradise.com.malangparadise.konfigurasi.konfigurasi;
import malang.paradise.com.malangparadise.request.Utils;

public class EditProfile extends AppCompatActivity {

    CircleImageView imageProfile;
    EditText Ename,Eusername,Epassword;

    private String mPostKeyId = null, mPostkeyUsername = null, mPostKeyNama = null,
            mPostKeyPassword = null, mPostKeyImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Utils.darkenStatusBar(this,R.color.colorPrimary);

        Ename = findViewById(R.id.name);
        Eusername = findViewById(R.id.username);
        Epassword = findViewById(R.id.password);
        imageProfile = findViewById(R.id.imageProfile);

        mPostKeyId = getIntent().getExtras().getString("id_userI");
        mPostkeyUsername = getIntent().getExtras().getString("usernameI");
        mPostKeyNama = getIntent().getExtras().getString("nameI");
        mPostKeyPassword = getIntent().getExtras().getString("passwordI");
        mPostKeyImage = getIntent().getExtras().getString("imageI");

        Ename.setText(mPostKeyNama);
        Eusername.setText(mPostkeyUsername);
        Epassword.setText(mPostKeyPassword);

        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_launcher_background);
        Glide.with(getApplicationContext()).load(konfigurasi.PROFILE_IMAGE_URL+mPostKeyImage).apply(requestOptions).into(imageProfile);
    }
}
