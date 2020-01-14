package malang.paradise.com.malangparadise.adapter;

import android.app.Activity;
import android.app.DownloadManager;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import malang.paradise.com.malangparadise.R;
import malang.paradise.com.malangparadise.inteface.RecyclerViewListClicked;
import malang.paradise.com.malangparadise.json.Kategori;
import malang.paradise.com.malangparadise.konfigurasi.konfigurasi;

import static malang.paradise.com.malangparadise.activity.HomePage.imagee;

public class KategoriAdapter extends RecyclerView.Adapter<KategoriAdapter.ProductViewHolder>{

    private static final String TAG = "KategoriAdapter";

    private static RecyclerViewListClicked itemListener;

    private Activity mCtx;
    private List<Kategori> kategoriList;
    public Activity mContext;
    String id_content;
    DownloadManager downloadManager;
    int row_index = -1;

    public KategoriAdapter(Activity mCtx, List<Kategori> kategoriList, RecyclerViewListClicked itemListener) {
        this.mCtx = mCtx;
        this.kategoriList = kategoriList;
        this.itemListener = itemListener;

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.preview_kategori, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        final Kategori kategori = kategoriList.get(position);
        final RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.color.colorBlack);

        if(kategori.getNama().equals("Semua")){
            Glide.with(Objects.requireNonNull(mCtx)).load(konfigurasi.PROFILE_IMAGE_URL+imagee).apply(requestOptions).into(holder.gambar);
        } else{
            Glide.with(Objects.requireNonNull(mCtx)).load("https://malang-paradise.000webhostapp.com/kategori/kategori-" + kategori.getGambar()).apply(requestOptions).into(holder.gambar);
        }
        holder.nama.setText(kategori.getNama());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.recyclerViewListClicked(v, position);
                row_index=position;
                notifyDataSetChanged();
            }
        });

        if(row_index==position){
            Glide.with(Objects.requireNonNull(mCtx)).load("https://htmlcolors.com/color-image/2196f3.png").apply(requestOptions).into(holder.gambarLuar);
        }
        else
        {
            Glide.with(Objects.requireNonNull(mCtx)).load("https://htmlcolors.com/color-image/ffffff.png").apply(requestOptions).into(holder.gambarLuar);
        }
    }

    @Override
    public int getItemCount() {
        return kategoriList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        CircleImageView gambar, gambarLuar;
        TextView nama;
        View view;
        LinearLayout kategori;


        public ProductViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            gambar = itemView.findViewById(R.id.gambar);
            gambarLuar = itemView.findViewById(R.id.gambarLuar);
            nama = itemView.findViewById(R.id.nama);
            kategori = itemView.findViewById(R.id.kategori);
        }
    }

}
