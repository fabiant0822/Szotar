package Szotar;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import javax.swing.JOptionPane;

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
}
