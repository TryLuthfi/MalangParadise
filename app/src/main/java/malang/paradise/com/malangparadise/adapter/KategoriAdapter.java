package malang.paradise.com.malangparadise.adapter;

import android.app.Activity;
import android.app.DownloadManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import malang.paradise.com.malangparadise.R;
import malang.paradise.com.malangparadise.json.Kategori;

public class KategoriAdapter extends RecyclerView.Adapter<KategoriAdapter.ProductViewHolder>{
    private static final String TAG = "KategoriAdapter";

    private Activity mCtx;
    private List<Kategori> kategoriList;
    public Activity mContext;
    String id_content;
    DownloadManager downloadManager;

    public KategoriAdapter(Activity mCtx, List<Kategori> kategoriList) {
        this.mCtx = mCtx;
        this.kategoriList = kategoriList;

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.preview_kategori, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
        final Kategori kategori = kategoriList.get(position);
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);

        Glide.with(Objects.requireNonNull(mCtx)).load("https://malang-paradise.000webhostapp.com/kategori/kategori-" + kategori.getGambar()).apply(requestOptions).into(holder.gambar);
        holder.nama.setText(kategori.getNama());
    }

    @Override
    public int getItemCount() {
        return kategoriList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        CircleImageView gambar;
        TextView nama;
        View view;


        public ProductViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            gambar = itemView.findViewById(R.id.gambar);
            nama = itemView.findViewById(R.id.nama);
        }
    }

}
