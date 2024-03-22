//    Saya Alfen Fajri Nurulhaq (2201431) mengerjakan TP2 DPBO dalam mata
//    kuliah OOP untuk keberkahanNya maka saya tidak
//    melakukan kecurangan seperti yang telah dispesikasikan.Aamiin
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Menu extends JFrame{
    public static void main(String[] args) {
        // buat object window
        Menu window = new Menu();
        // atur ukuran window
        window.setSize(480, 560);
        // letakkan window di tengah layar
        window.setLocationRelativeTo(null);
        // isi window
        window.setContentPane(window.mainPanel);
        // ubah warna background
        window.getContentPane().setBackground(Color.lightGray);
        // tampilkan window
        window.setVisible(true);
        // agar program ikut berhenti saat window diclose
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    // index baris yang diklik
    private int selectedIndex = -1;
    // list untuk menampung semua mahasiswa
    private ArrayList<Mahasiswa> listMahasiswa;

    private Database database;

    private JPanel mainPanel;
    private JTextField nimField;
    private JTextField namaField;
    private JTable mahasiswaTable;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JComboBox jenisKelaminComboBox;
    private JButton deleteButton;
    private JLabel titleLabel;
    private JLabel nimLabel;
    private JLabel namaLabel;
    private JLabel jenisKelaminLabel;
    private JLabel kelasLabel;//label untuk kelas
    private JRadioButton c1RadioButton;//component radio button untuk kelas
    private JRadioButton c2RadioButton;
    private ButtonGroup kelasButtonGroups;//button group untuk kelas

    // constructor
    public Menu() {
        // inisialisasi listMahasiswa
        listMahasiswa = new ArrayList<>();

        // instansiasi dari database
        database = new Database();

        // isi tabel mahasiswa
        mahasiswaTable.setModel(setTable());

        // ubah styling title
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));

        // atur isi combo box
        String[] jenisKelaminData = {"", "Laki-laki", "Perempuan"};
        jenisKelaminComboBox.setModel(new DefaultComboBoxModel(jenisKelaminData));

        kelasButtonGroups = new ButtonGroup();//instansiasi button group untuk kelas
        kelasButtonGroups.add(c1RadioButton);//menambah ke button group radio button c1
        kelasButtonGroups.add(c2RadioButton);//menambah ke button group radio button c1


        // sembunyikan button delete
        deleteButton.setVisible(false);

        // saat tombol add/update ditekan
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            if (selectedIndex == -1){
                insertData();
                }else {
                updateData();
                }

            }
        });
        // saat tombol delete ditekan
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            if(selectedIndex >= 0){
                deleteData();
            }
            }
        });
        // saat tombol cancel ditekan
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            clearForm();
            }
        });
        // saat salah satu baris tabel ditekan
        mahasiswaTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // ubah selectedIndex menjadi baris tabel yang diklik
                selectedIndex = mahasiswaTable.getSelectedRow();

                // simpan value textfield dan combo box serta button
                String selectedNim = mahasiswaTable.getModel().getValueAt(selectedIndex, 1).toString();
                String selectedNama = mahasiswaTable.getModel().getValueAt(selectedIndex, 2).toString();
                String selectedJenisKelamin = mahasiswaTable.getModel().getValueAt(selectedIndex, 3).toString();
                String selectedKelas = mahasiswaTable.getModel().getValueAt(selectedIndex, 4).toString();



                // ubah isi textfield dan combo box dan radiobutton
                nimField.setText(selectedNim);
                namaField.setText(selectedNama);
                jenisKelaminComboBox.setSelectedItem(selectedJenisKelamin);

                if (selectedKelas.equalsIgnoreCase("c1")){
                    c1RadioButton.setSelected(true);
                } else if(selectedKelas.equalsIgnoreCase("c2")){
                    c2RadioButton.setSelected(true);
                }else {
                    c1RadioButton.setSelected(false);
                    c2RadioButton.setSelected(false);
                }

                // ubah button "Add" menjadi "Update"
                addUpdateButton.setText("Update");
                // tampilkan button delete
                deleteButton.setVisible(true);
            }
        });
    }

    public final DefaultTableModel setTable() {
        // tentukan kolom tabel
        Object[] column = {"No", "NIM", "Nama", "Jenis Kelamin", "Kelas"};

        // buat objek tabel dengan kolom yang sudah dibuat
        DefaultTableModel temp = new DefaultTableModel(null, column);

        try {
            ResultSet resultSet = database.selectQuery("SELECT * FROM mahasiswa");//tampilkan tabel mahasiswa
            int i = 0;
            while (resultSet.next()) {
                Object[] row = new Object[5];
                row[0] = i + 1;
                row[1] = resultSet.getString("nim");
                row[2] = resultSet.getString("nama");
                row[3] = resultSet.getString("jenis_kelamin");
                row[4] = resultSet.getString("kelas");

                temp.addRow(row);
                i++;
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return temp; // return juga harus diganti
    }

    public void insertData() {
        // ambil value dari textfield dan combobox serta radiobutton
        String nim = nimField.getText();
        String nama = namaField.getText();
        String jenisKelamin = jenisKelaminComboBox.getSelectedItem().toString();
        String kelas;
        if (c1RadioButton.isSelected()){
            kelas = "C1";
        }else if(c2RadioButton.isSelected()){
            kelas = "C2";
        }else {
            kelas = "";
        }
        //cek semua atribut ada isinya apa ngga
        if (nim.isEmpty() || nama.isEmpty() || jenisKelamin.isEmpty() || kelas.isEmpty()){
            JOptionPane.showMessageDialog(null, "Tolong Lengkapi Inputan Anda","Error", JOptionPane.ERROR_MESSAGE);//kalau gaada kasih alert lengkapi inputan
            return;
        }

        // Mengecek apakah NIM sudah ada dalam database atau belum
        String checkQuery = "SELECT COUNT(*) FROM mahasiswa WHERE nim = '" + nim + "'";
        // Menjalankan query untuk melakukan pengecekan ke database
        ResultSet resultSet = database.selectQuery(checkQuery);
        try {
            // Memeriksa apakah hasil query mengembalikan baris
            if (resultSet.next()) {
                // flaging
                int count = resultSet.getInt(1);
                // Jika count lebih besar dari 0, berarti NIM sudah ada dalam database
                if (count > 0) {
                    // Menampilkan pesan kesalahan jika NIM sudah ada dalam database
                    JOptionPane.showMessageDialog(null, "NIM sudah ada dalam database! Tolong tambahkan data dengan NIM yang belum ada", "Error", JOptionPane.ERROR_MESSAGE);
                    // Mengembalikan kontrol dari metode insertData()
                    return;
                }
            }
            // Menutup resultSet setelah penggunaannya
            resultSet.close();
        // Menangani pengecualian jika terjadi kesalahan saat menjalankan query
        } catch (SQLException e) {
            e.printStackTrace();
        }



        // tambahkan data ke dalam database
        String sql = "INSERT INTO mahasiswa VALUES (null, '" + nim + "', '" + nama + "', '" + jenisKelamin + "', '" + kelas + "');";
        database.insertUpdateDeleteQuery(sql);

        // update tabel
        mahasiswaTable.setModel(setTable());

        // bersihkan form
        clearForm();

        // feedback
        System.out.println("Insert Berhasil!");
        JOptionPane.showMessageDialog(null, "Data Berhasil ditambahkan");
    }

    public void updateData() {
        // ambil data dari form
        String nim = nimField.getText();
        String nama = namaField.getText();
        String jenisKelamin = jenisKelaminComboBox.getSelectedItem().toString();
        String kelas;
        if (c1RadioButton.isSelected()){
            kelas = "C1";
        } else if(c2RadioButton.isSelected()){
            kelas = "C2";
        } else {
            kelas = "";
        }
        // Simpan NIM sebelumnya
        String previousNim = mahasiswaTable.getModel().getValueAt(selectedIndex, 1).toString();


        if (nim.isEmpty() || nama.isEmpty() || jenisKelamin.isEmpty() || kelas.isEmpty()){
            JOptionPane.showMessageDialog(null, "Tolong Lengkapi Inputan Anda","Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Mengecek apakah NIM yang ingin diupdate sudah ada dalam database
        String checkQuery = "SELECT COUNT(*) FROM mahasiswa WHERE nim = '" + nim + "'";
        ResultSet resultSet = database.selectQuery(checkQuery);
        try {
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                // Jika NIM yang ingin diupdate bukan NIM sebelumnya dan NIM tersebut sudah ada dalam database
                if (count > 0 && !nim.equals(mahasiswaTable.getModel().getValueAt(selectedIndex, 1).toString())) {
                    JOptionPane.showMessageDialog(null, "NIM sudah ada dalam database! Anda mengupdate NIM dengan NIM yang sudah ada", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Ubah data mahasiswa di database
        String sql = "UPDATE mahasiswa SET nim = '" + nim + "', nama = '" + nama + "', jenis_kelamin = '" + jenisKelamin + "', kelas = '" + kelas + "' WHERE nim = '" + previousNim + "';";
//        System.out.println(sql);
        database.insertUpdateDeleteQuery(sql);

        // Update tabel
        mahasiswaTable.setModel(setTable());

        // Bersihkan form
        clearForm();

        // Feedback
        System.out.println("Update Berhasil!");
        JOptionPane.showMessageDialog(null, "Data Berhasil diubah!");
    }


    public void deleteData() {
        //memberikan alert menggunakan option pane ketika ingin menghapus data
        int option = JOptionPane.showConfirmDialog(null, "Yakin ingin menghapus data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            // Hapus data dari list
            String nim = nimField.getText();
            String sql = "DELETE FROM mahasiswa WHERE nim = '" + nim + "';";
            database.insertUpdateDeleteQuery(sql);

            // Update tabel
            mahasiswaTable.setModel(setTable());

            // Bersihkan form
            clearForm();

            // Feedback
            System.out.println("Delete Berhasil");
            JOptionPane.showMessageDialog(null, "Data Berhasil dihapus!");
        }
    }

    public void clearForm() {
        // kosongkan semua texfield dan combo box
        nimField.setText("");
        namaField.setText("");
        jenisKelaminComboBox.setSelectedItem("");
        kelasButtonGroups.clearSelection();

        // ubah button "Update" menjadi "Add"
        addUpdateButton.setText("Add");
        // sembunyikan button delete
        deleteButton.setVisible(false);
        // ubah selectedIndex menjadi -1 (tidak ada baris yang dipilih)
        selectedIndex = -1;
    }
}
