package malang.paradise.com.malangparadise.adapter;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
import java.util.Objects;

import malang.paradise.com.malangparadise.R;
import malang.paradise.com.malangparadise.activity.Coba;
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
                .placeholder(R.color.colorPrimary);

        Glide.with(Objects.requireNonNull(mCtx)).load("https://malang-paradise.000webhostapp.com/" + postingan.getGambar()).apply(requestOptions).into(holder.gambar);
        holder.nama.setText(postingan.getNama());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, Coba.class);
                intent.putExtra("id_postingan", postingan.getId_postingan());
                intent.putExtra("nama", postingan.getNama());
                intent.putExtra("gambar", postingan.getGambar());
                intent.putExtra("berita", postingan.getBerita());
                intent.putExtra("rating", postingan.getNilai_rating());
                intent.putExtra("rating2", postingan.getNilai_rating());
                intent.putExtra("lokasi", postingan.getLokasi());
                intent.putExtra("lat", postingan.getLat());
                intent.putExtra("longg", postingan.getLongg());
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
