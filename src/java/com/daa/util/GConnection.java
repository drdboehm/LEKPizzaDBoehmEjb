/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 */
package com.daa.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Schulung
 */
public class GConnection {

    /**
     * Holt eine Datenbankverbindung.
     *
     * @return SQL Connection oder null wenn Fehler.
     */
    protected static Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            //Get a connection
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbpizza", 
                    "root", "5bA0PoKa");
            conn.setAutoCommit(false);
            return conn;
        } catch (SQLException ex) {
            System.out.println(ex.getStackTrace());
              Logger.getLogger(GConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            //Ausgaben zum Loggen
              Logger.getLogger(GConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            //Ausgaben zum Loggen
              Logger.getLogger(GConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            //Ausgaben zum Loggen
              Logger.getLogger(GConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
