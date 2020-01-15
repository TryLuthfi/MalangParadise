package malang.paradise.com.malangparadise.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
import java.util.Objects;

import malang.paradise.com.malangparadise.R;
import malang.paradise.com.malangparadise.json.Gambar;
import uk.co.senab.photoview.PhotoViewAttacher;

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
                final Dialog dialog = new Dialog(mCtx);
                dialog.setCancelable(true);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.custom_dialog);
                ImageView gambarDialog = dialog.findViewById(R.id.gambar);
                Glide.with((mCtx)).load("https://malang-paradise.000webhostapp.com/" + gambar.getGambar()).into(gambarDialog);
                PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(gambarDialog);
                photoViewAttacher.update();
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
