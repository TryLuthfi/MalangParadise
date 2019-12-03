package malang.paradise.com.malangparadise.json;

public class Gambar {
    private String id_gambar;
    private String id_postingan;
    private String gambar;

    public Gambar(String id_gambar, String id_postingan, String gambar) {
        this.id_gambar = id_gambar;
        this.id_postingan = id_postingan;
        this.gambar = gambar;
    }

    public String getId_gambar() {
        return id_gambar;
    }

    public String getId_postingan() {
        return id_postingan;
    }

    public String getGambar() {
        return gambar;
    }
}
