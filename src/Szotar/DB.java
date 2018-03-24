package Szotar;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Fabiant Tamas 1.0
 */
public class DB {

    final String user = "tanulo";
    final String pass = "tanulo";
    String dbUrl;
    
    public DB() {
        url_be();
    }
    
/**
     * Beolvassa a config.properties fileból az adatbázis IP címét
     * és beállítja a dbUrl-t, ha nincs fájl, hibaüzenetet küld és
     * a localhost-ot használja.
     */
    private void url_be() {
        Properties beallitasok = new Properties();
        try (FileInputStream be = new FileInputStream("config.properties")) {
            beallitasok.load(be);
            String ip = beallitasok.getProperty("ip");
            dbUrl = "jdbc:mysql://" + ip + ":3306/szotar"
                    + "?useUnicode=true&characterEncoding=UTF-8";
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            dbUrl = "jdbc:mysql://localhost:3306/szotar"
                   + "?useUnicode=true&characterEncoding=UTF-8";
        }
    }
    
    public void beolvas(JTable tbl, String s) {
        final DefaultTableModel tm = (DefaultTableModel)tbl.getModel();

        try (Connection kapcs = DriverManager.getConnection(dbUrl,user,pass);
             PreparedStatement parancs = kapcs.prepareStatement(s);
             ResultSet eredmeny = parancs.executeQuery()) {
            tm.setRowCount(0);
            while (eredmeny.next()) {
                Object sor[] = {
                    eredmeny.getInt("szoID"),
                    eredmeny.getString("lecke"),
                    eredmeny.getString("angol"),
                    eredmeny.getString("magyar")
                };
                tm.addRow(sor);
            }            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            System.exit(1);
        }
    }
    
    public String levag (String s, int n) {
        return s.length() > n ? s.substring(0, n) : s;
    }
    
    public int hozzaad(String lecke, String angol, String magyar) {
        if (angol.isEmpty() || magyar.isEmpty())
            return 0;
        String s = "INSERT INTO szavak (lecke,angol,magyar) VALUES(?,?,?);";
        try (Connection kapcs = DriverManager.getConnection(dbUrl, user, pass);
                PreparedStatement parancs = kapcs.prepareStatement(s)) {
            if (lecke.isEmpty())
                parancs.setNull(1, java.sql.Types.VARCHAR);
            else
                parancs.setString(1, levag(lecke.trim(), 10));
            parancs.setString(2, levag(angol.trim(), 60));
            parancs.setString(3, levag(magyar.trim(), 60));
            return parancs.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            return 0;
        }        
    }
    
    public int modosit(int szoid, String lecke, String angol, String magyar) {
        if (angol.isEmpty() || magyar.isEmpty())
            return 0;
        String s = "UPDATE szavak SET lecke=?, angol=?, magyar=? "
                 + "WHERE szoid=?";
        try (Connection kapcs = DriverManager.getConnection(dbUrl, user, pass);
                PreparedStatement parancs = kapcs.prepareStatement(s)) {
            if (lecke.isEmpty())
                parancs.setNull(1, java.sql.Types.VARCHAR);
            else
                parancs.setString(1, levag(lecke.trim(), 10));
            parancs.setString(2, levag(angol.trim(), 60));
            parancs.setString(3, levag(magyar.trim(), 60));
            parancs.setInt(4, szoid);
            return parancs.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            return 0;
        }        
    }
    
    public void torol(int szoid) {
        String s = "DELETE FROM szavak WHERE szoid=?;";
        try (Connection kapcs = DriverManager.getConnection(dbUrl, user, pass);
                PreparedStatement parancs = kapcs.prepareStatement(s)) {
            parancs.setInt(1, szoid);
            parancs.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }                
    }
}
