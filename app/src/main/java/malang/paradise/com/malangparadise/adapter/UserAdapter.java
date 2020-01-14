package malang.paradise.com.malangparadise.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import malang.paradise.com.malangparadise.R;
import malang.paradise.com.malangparadise.json.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<User> contactList;
    private List<User> contactListFiltered;
    private ContactsAdapterListener listener;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nama, berita, ratingNumber;
        AppCompatRatingBar rate;
        public ImageView gambar;

        public MyViewHolder(View view) {
            super(view);
            nama = view.findViewById(R.id.nama);
            berita = view.findViewById(R.id.berita);
            rate = view.findViewById(R.id.rate);
            gambar = view.findViewById(R.id.gambar);
            ratingNumber = view.findViewById(R.id.ratingNumber);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public UserAdapter(Context context, List<User> contactList, ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final User contact = contactListFiltered.get(position);
        holder.nama.setText(contact.getNama());
        holder.berita.setText(contact.getBerita());

        if(contact.getGambar().equals("empty")){
            Glide.with(context).load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTQiZSMtcZ3iQz-C09z2XAkYukrdsxrXRvrRl6myil68joLMHUM").into(holder.gambar);
        }else {
            Glide.with(context)
                    .load("http://malang-paradise.000webhostapp.com/" + contact.getGambar())
                    .into(holder.gambar);
        }
        holder.rate.setRating(contact.getNilai_rating());
        holder.ratingNumber.setText(Float.toString(contact.getNilai_rating()));
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                } else {
                    List<User> filteredList = new ArrayList<>();
                    for (User row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getNama().toLowerCase().contains(charString.toLowerCase()) || row.getNama().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<User>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(User contact);
    }
}