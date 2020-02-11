package malang.paradise.com.malangparadise.json;

public class Postingan {
    private String id_postingan;
    private String id_user;
    private String id_kategori;
    private String id_gambar;
    private String gambar;
    private String nama;
    private String berita;
    private String lokasi;
    private String tanggal_upload;
    private String lat;
    private String lng;
    private String status;
    private String notife;
    private String nilai_rating;

    public Postingan(String id_postingan, String id_user, String id_kategori, String id_gambar, String gambar,String nama, String berita, String lokasi, String tanggal_upload,String lat, String lng,String status, String notife, String nilai_rating) {
        this.id_postingan = id_postingan;
        this.id_user = id_user;
        this.id_kategori = id_kategori;
        this.id_gambar = id_gambar;
        this.gambar = gambar;
        this.nama = nama;
        this.berita = berita;
        this.lokasi = lokasi;
        this.tanggal_upload = tanggal_upload;
        this.lat = lat;
        this.lng = lng;
        this.status = status;
        this.notife = notife;
        this.nilai_rating = nilai_rating;
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

    public String getLat() {
        return lat;
    }

    public String getLng(){
        return lng;
    }

    public String getStatus(){return status;}

    public String getNotife(){return notife;}

    public String getNilai_rating() { return nilai_rating;}
}
