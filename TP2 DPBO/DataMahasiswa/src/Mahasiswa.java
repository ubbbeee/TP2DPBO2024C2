//    Saya Alfen Fajri Nurulhaq (2201431) mengerjakan TP2 DPBO dalam mata
//    kuliah OOP untuk keberkahanNya maka saya tidak
//    melakukan kecurangan seperti yang telah dispesikasikan.Aamiin
public class Mahasiswa {
    private String nim;
    private String nama;
    private String jenisKelamin;
    private String kelas;

    public Mahasiswa(String nim, String nama, String jenisKelamin, String kelas) {
        this.nim = nim;
        this.nama = nama;
        this.jenisKelamin = jenisKelamin;
        this.kelas = kelas;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public void setKelas(String kelas) { this.kelas = kelas; }

    public String getNim() {
        return this.nim;
    }

    public String getNama() {
        return this.nama;
    }

    public String getJenisKelamin() {
        return this.jenisKelamin;
    }

    public String getKelas() { return this.kelas; }
}
