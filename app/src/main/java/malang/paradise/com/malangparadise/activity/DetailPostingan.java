package malang.paradise.com.malangparadise.activity;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.Objects;

import malang.paradise.com.malangparadise.R;

public class DetailPostingan extends AppCompatActivity {
    private String mPostKeyNama = null, mPostKeyGambar = null, mPostKeyBerita = null,
    mPostKeyRating = null;
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView iv_header;
    private TextView berita;
    private TextView rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_postingan);

        berita = findViewById(R.id.berita);
        rating = findViewById(R.id.rating);
        iv_header = findViewById(R.id.iv_header);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        mPostKeyNama = getIntent().getExtras().getString("nama");
        mPostKeyGambar = getIntent().getExtras().getString("gambar");
        mPostKeyBerita = getIntent().getExtras().getString("berita");
        mPostKeyRating = getIntent().getExtras().getString("rating");

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);

        berita.setText(mPostKeyBerita);
        rating.setText(mPostKeyRating);
        collapsingToolbar.setTitle(mPostKeyNama);
        collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.colorBlack));
        Glide.with(Objects.requireNonNull(getApplicationContext())).load("http://malang-paradise.000webhostapp.com/" + mPostKeyGambar).apply(requestOptions).into(iv_header);

    }
}
