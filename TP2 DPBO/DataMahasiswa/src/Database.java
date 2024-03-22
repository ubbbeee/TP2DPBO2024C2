//    Saya Alfen Fajri Nurulhaq (2201431) mengerjakan TP2 DPBO dalam mata
//    kuliah OOP untuk keberkahanNya maka saya tidak
//    melakukan kecurangan seperti yang telah dispesikasikan.Aamiin
import java.sql.*;

public class Database {
    private Connection connection;
    private Statement statement;

    //constructor
    public Database() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_mahasiswa", "root", "");
            statement = connection.createStatement();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    //digunakan untuk select
    public ResultSet selectQuery(String sql){
        try {
            statement.executeQuery(sql);
            return statement.getResultSet();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    //digunakan untuk Insert, update dan delete
    public int insertUpdateDeleteQuery(String sql){
        try {
//            System.out.println("Jalan");
            return statement.executeUpdate(sql);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    //getter
    public Statement getStatement(){
        return statement;
    }
}