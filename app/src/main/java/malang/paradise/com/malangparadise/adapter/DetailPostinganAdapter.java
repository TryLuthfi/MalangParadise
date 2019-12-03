package malang.paradise.com.malangparadise.adapter;

import android.app.Activity;
import android.app.DownloadManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import malang.paradise.com.malangparadise.R;
import malang.paradise.com.malangparadise.inteface.RecyclerViewListClicked;
import malang.paradise.com.malangparadise.json.Gambar;
import malang.paradise.com.malangparadise.json.Kategori;
import malang.paradise.com.malangparadise.konfigurasi.konfigurasi;

import static malang.paradise.com.malangparadise.activity.HomePage.imagee;

public class DetailPostinganAdapter extends RecyclerView.Adapter<DetailPostinganAdapter.ProductViewHolder>{

    private static final String TAG = "DetailPostinganAdapter";


    private Activity mCtx;
    private List<Gambar> gambarList;
    public Activity mContext;
    String id_content;
    DownloadManager downloadManager;

    public DetailPostinganAdapter(Activity mCtx, List<Gambar> gambarList) {
        this.mCtx = mCtx;
        this.gambarList = gambarList;

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.preview_image, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        final Gambar gambar = gambarList.get(position);
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);

        Glide.with(Objects.requireNonNull(mCtx)).load("https://malang-paradise.000webhostapp.com/" + gambar.getGambar()).apply(requestOptions).into(holder.gambar);
        holder.gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mCtx, "https://malang-paradise.000webhostapp.com/" + gambar.getGambar(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return gambarList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView gambar;
        View view;


        public ProductViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            gambar = itemView.findViewById(R.id.gambar);
        }
    }

}
