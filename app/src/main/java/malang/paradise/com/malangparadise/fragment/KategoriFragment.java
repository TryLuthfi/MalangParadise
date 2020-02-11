package malang.paradise.com.malangparadise.fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import malang.paradise.com.malangparadise.R;
import malang.paradise.com.malangparadise.activity.HomePage;
import malang.paradise.com.malangparadise.adapter.SemuaPostinganAdapter;
import malang.paradise.com.malangparadise.json.Postingan;
import malang.paradise.com.malangparadise.konfigurasi.konfigurasi;

/**
 * A simple {@link Fragment} subclass.
 */
public class KategoriFragment extends Fragment {
    List<Postingan> postinganList;
    RecyclerView recyclerViewPostingan;
    View view;
    public HomePage intent;
    private String namaIntent;
    ProgressBar loading;


    public KategoriFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_pendakian, container, false);

        intent = (HomePage) getActivity();

        namaIntent = intent.kategoriIntent;

        loading = view.findViewById(R.id.loading);


        recyclerViewPostingan = view.findViewById(R.id.postingantRecyler);
        recyclerViewPostingan.setHasFixedSize(true);
        recyclerViewPostingan.setLayoutManager(new LinearLayoutManager(getActivity()));

        postinganList = new ArrayList<>();

        loadPostingan();

        return view;
    }

    private void loadPostingan() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, konfigurasi.URL_PRODUCTS_POSTINGAN,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);


                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);

                                //adding the product to product list
                                if(namaIntent.equals(product.getString("id_kategori")) && product.getString("status").equals("aktif")) {
                                    postinganList.add(new Postingan(
                                            product.getString("id_postingan"),
                                            product.getString("id_user"),
                                            product.getString("id_kategori"),
                                            product.getString("id_gambar"),
                                            product.getString("gambar"),
                                            product.getString("nama"),
                                            product.getString("berita"),
                                            product.getString("lokasi"),
                                            product.getString("tanggal_upload"),
                                            product.getString("lat"),
                                            product.getString("lng"),
                                            product.getString("status"),
                                            product.getString("notife"),
                                            product.getString("nilai_rating")
                                    ));
                                }
                            }

                            SemuaPostinganAdapter adapter= new SemuaPostinganAdapter(getActivity(), postinganList);

                            if (adapter != null){
                                recyclerViewPostingan.setAdapter(adapter);
                                loading.setVisibility(View.INVISIBLE);

                            }else {
                                Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
                            }

//                            loading.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(Objects.requireNonNull(getActivity())).add(stringRequest);
    }

}
