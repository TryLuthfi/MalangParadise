package malang.paradise.com.malangparadise.json;

public class User {

     String id_postingan;
     String id_user;
     String id_kategori;
     String id_gambar;
     String gambar;
     String nama;
     String berita;
     String lokasi;
     String tanggal_upload;
     float nilai_rating;

    public User(){

    }

    public String getId_postingan() {
        return id_postingan;
    }

    public String getId_user() {
        return id_user;
    }

    public String getId_kategori() {
        return id_kategori;
    }


    public String getId_gambar() {
        return id_gambar;
    }

    public String getGambar() {
        return gambar;
    }

    public String getNama() {
        return nama;
    }

    public String getBerita() {
        return berita;
    }

    public String getLokasi() {
        return lokasi;
    }

    public String getTanggal_upload() {
        return tanggal_upload;
    }

    public float getNilai_rating() {return nilai_rating;}
}
