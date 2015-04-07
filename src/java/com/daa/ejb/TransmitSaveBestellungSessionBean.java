/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.daa.ejb;

import com.daa.model.Bestellung;
import com.daa.model.Gericht;
import com.daa.util.GConnection;
import java.sql.Connection;
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
                System.err.println("Anzahl Gerichte: " + gerichte.size());
            }

        } catch (SQLException ex) {
            Logger.getLogger(TransmitSaveBestellungSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return gerichte;
    }

    @PostConstruct
    public void init() {
        conn = GConnection.getConnection();
//      gerichte =  this.initializeMenu();
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
