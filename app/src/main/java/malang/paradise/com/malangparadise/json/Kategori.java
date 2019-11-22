package malang.paradise.com.malangparadise.json;

public class Kategori {
    private String id_kategori;
    private String nama;
    private String gambar;

    public Kategori(String id_kategori, String nama, String gambar) {
        this.id_kategori = id_kategori;
        this.nama = nama;
        this.gambar = gambar;
    }

    public String getId_kategori() {
        return id_kategori;
    }

    public String getNama() {
        return nama;
    }

    public String getGambar() {
        return gambar;
    }
}
