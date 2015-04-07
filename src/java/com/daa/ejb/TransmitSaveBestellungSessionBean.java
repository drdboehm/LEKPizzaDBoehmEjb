/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.daa.ejb;

import com.daa.model.Bestellung;
import com.daa.model.Gericht;
import com.daa.model.Kunde;
import com.daa.util.GConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;

/**
 *
 * @author teilnehmer
 */
@Stateless
public class TransmitSaveBestellungSessionBean extends GConnection implements TransmitSaveBestellungSessionBeanRemote {

    private Connection conn;
    private Statement stmt;
    private ResultSet rs;
    private PreparedStatement pStmt;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public List<Gericht> initializeMenu() {
        List<Gericht> gerichte = new ArrayList<Gericht>();
        try {

            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT idGericht, bezeichnung, preis FROM Gericht");
            while (rs.next()) {
                Gericht gericht = new Gericht(rs.getInt("idGericht"), rs.getString("BEZEICHNUNG"),
                        rs.getDouble("PREIS"));
                gerichte.add(gericht);
                System.out.println("Anzahl Gerichte: " + gerichte.size());
            }

        } catch (SQLException ex) {
            Logger.getLogger(TransmitSaveBestellungSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return gerichte;
    }

    @PostConstruct
    public void init() {
        conn = GConnection.getConnection();
    }

    @PreDestroy
    public void delete() {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(TransmitSaveBestellungSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean storeEjb(Bestellung bestellung) {
        boolean success = false;
        Kunde tempKunde = storeKunde(bestellung.getKeyKunde());
        System.out.println("LastKundeIs: " + tempKunde.getLastId());
        if (tempKunde != null) {
            success = true;
        }
        return success;
    }

    private Kunde storeKunde(Kunde currentKunde) {
        ResultSet rs = null;
        boolean success = false;
        try {
            if (conn == null) {
                return null;
            }
            pStmt = conn.prepareStatement("INSERT INTO Kunde (username, vorname, "
                    + "nachname, strasse, hausnr, PLZ, Ort, firstEntryDate, lastEntryDate) "
                    + "VALUES(?,?,?,?,?,?,?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");
            pStmt.setString(1, currentKunde.getUsername().trim());
            pStmt.setString(2, currentKunde.getVorname().trim());
            pStmt.setString(3, currentKunde.getNachname().trim());
            pStmt.setString(4, currentKunde.getStrasse().trim());
            pStmt.setString(5, currentKunde.getHausnr().trim());
            pStmt.setString(6, currentKunde.getPlz().trim());
            pStmt.setString(7, currentKunde.getOrt().trim());
            int rows = pStmt.executeUpdate();
            conn.commit();
            success = (rows == 1);
            // storeKunde the last_insert_id of the Kunde object in lastId, 
            rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
            if (rs.next()) {
                currentKunde.setLastId(rs.getInt(1));
            } else {
                // throw an exception from here
            }

        } catch (SQLException ex) {
            org.jboss.logging.Logger.getLogger(Kunde.class.getName()).log(org.jboss.logging.Logger.Level.FATAL, null, ex);
            success = false;
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(Kunde.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                conn.close();
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(Kunde.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return currentKunde;
    }
}
