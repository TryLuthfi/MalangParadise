package malang.paradise.com.malangparadise.adapter;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import malang.paradise.com.malangparadise.R;
import malang.paradise.com.malangparadise.activity.DetailPostingan;
import malang.paradise.com.malangparadise.json.Postingan;

public class SemuaPostinganAdapter extends RecyclerView.Adapter<SemuaPostinganAdapter.ProductViewHolder>{
    private static final String TAG = "PostinganAdapter";

    private Activity mCtx;
    private List<Postingan> postinganList;
    public Activity mContext;
    String id_content;
    DownloadManager downloadManager;

    public SemuaPostinganAdapter(Activity mCtx, List<Postingan> postinganList) {
        this.mCtx = mCtx;
        this.postinganList = postinganList;

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.preview_postingan, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
        final Postingan postingan = postinganList.get(position);
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);

        Glide.with(Objects.requireNonNull(mCtx)).load("http://malang-paradise.000webhostapp.com/" + postingan.getGambar()).apply(requestOptions).into(holder.gambar);
        holder.nama.setText(postingan.getNama());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, DetailPostingan.class);
                intent.putExtra("nama", postingan.getNama());
                intent.putExtra("gambar", postingan.getGambar());
                intent.putExtra("berita", postingan.getBerita());
                intent.putExtra("rating", postingan.getRating());
                mCtx.startActivity(intent);
            }
        });

     }

    @Override
    public int getItemCount() {
        return postinganList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView gambar;
        TextView nama;


        public ProductViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            gambar = view.findViewById(R.id.gambar);
            nama = view.findViewById(R.id.nama);

        }
    }

}
