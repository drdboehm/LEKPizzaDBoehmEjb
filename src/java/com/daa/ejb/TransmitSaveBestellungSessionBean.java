/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.daa.ejb;

import com.daa.model.BestellWrapper;
import com.daa.model.Bestellung;
import com.daa.model.Gericht;
import com.daa.model.Kunde;
import com.daa.model.Orderposition;
import com.daa.util.GConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

/**
 *
 * @author teilnehmer
 */
@Stateless
public class TransmitSaveBestellungSessionBean extends GConnection implements TransmitSaveBestellungSessionBeanRemote {

    @PersistenceUnit(unitName = "PizzaService-ejbPU")
    private EntityManagerFactory emf;//=Persistence.createEntityManagerFactory("PizzaService-ejbPU");
    private Connection conn;
    private Statement stmt;
    private ResultSet rs;
    private PreparedStatement pStmt;
    private static Integer counter = 0;

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
            System.out.println("Durchlauf: " + (++counter));
        } catch (SQLException ex) {
            Logger.getLogger(TransmitSaveBestellungSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return gerichte;
    }

    @PostConstruct
    public void init() {
        conn = getConnection();
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
    public boolean storeEjb(BestellWrapper bw) {
        Bestellung bestellung = bw.getBestellung();
        bestellung.setOrderDate(new Date());
        Kunde kunde = bw.getKunde();
        List<Gericht> orderedGerichte = bw.getGerichte();
        /*
         Set timespamp fields - necessary ?
         */
        kunde.setFirstEntryDate(new Date());
        kunde.setLastEntryDate(new Date());
        boolean success = true;
        EntityManager em = emf.createEntityManager();

        em.persist(kunde);
        em.flush();
        // KundeId is set ! - bestellung takes Kunde-Object, not the integer ID only !
        bestellung.setKeyKunde(kunde);
        em.persist(bestellung);
        em.flush();
        Orderposition orderpositions = new Orderposition();
        // set the Key 
        orderpositions.setKeyOrder(bestellung);
        for (Gericht g : orderedGerichte) {
            /*
             show the entries for TS
             */
            System.out.println("OrderId: " + orderpositions.getKeyOrder().getIdOrder());
            System.out.println("GerichtId: " + g.getGerichtId());
            System.out.println("Anzahl: " + g.getAmount());
            orderpositions.setKeyGericht(g);
            orderpositions.setAmountPosition(g.getAmount());
            em.persist(orderpositions);
            em.flush();
        }

        // Get back the Integer orderId from persistence !
//     pStmt.setInt(2, temp.getGerichtId());
//            pStmt.setInt(3, temp.getAmount());
//            rows = pStmt.executeUpdate();
//            conn.commit();
//            success = (rows == 1);
//        }
//        return success;
//        
//        try {
//            /*
//             store Kunde-Object FIRSTand get the kundeID back
//             */
//            pStmt = conn.prepareStatement("INSERT INTO Kunde (username, vorname, "
//                    + "nachname, strasse, hausnr, PLZ, Ort, firstEntryDate, lastEntryDate) "
//                    + "VALUES(?,?,?,?,?,?,?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");
//            pStmt.setString(1, bestellung.getKeyKunde().getUsername().trim());
//            pStmt.setString(2, bestellung.getKeyKunde().getVorname().trim());
//            pStmt.setString(3, bestellung.getKeyKunde().getNachname().trim());
//            pStmt.setString(4, bestellung.getKeyKunde().getStrasse().trim());
//            pStmt.setString(5, bestellung.getKeyKunde().getHausnr().trim());
//            pStmt.setString(6, bestellung.getKeyKunde().getPlz().trim());
//            pStmt.setString(7, bestellung.getKeyKunde().getOrt().trim());
//            int rows = pStmt.executeUpdate();
//            conn.commit();
//            success = (rows == 1);
//            /*
//             store  the last_insert_id of the Kunde object in lastId, and later 
//             in the Kunde Object of bestellung
//             */
//            rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
//            if (rs.next()) {
//                bestellung.getKeyKunde().setLastId(rs.getInt(1));
//            } else {
//                success = false;
//                // throw an exception from here
//            }
//            /*
//             is the lastKundeId set ?
//             */
//            System.out.println("lastKundeIdSetIn bestellung before Setting? "
//                    + bestellung.getKeyKunde().getLastId());
//            /*
//             From here on, store the Bestellung
//             */
////            try {
////                init();
////                if (conn == null) {
////                    return success;
////                }
//            pStmt = conn.prepareStatement("INSERT INTO Bestellung (keyKunde, ipAddress, "
//                    + "orderDate, sessionId, isOrdered, totalPay) "
//                    + "VALUES(?,?,CURRENT_TIMESTAMP,?,?,?)");
//            pStmt.setInt(1, bestellung.getKeyKunde().getLastId());
//            pStmt.setString(2, bestellung.getIpAddress().trim());
//            pStmt.setString(3, bestellung.getSessionId().trim());
//            pStmt.setBoolean(4, bestellung.isIsOrdered());
//            pStmt.setBigDecimal(5, bestellung.getTotalPay());
//            rows = pStmt.executeUpdate();
//            conn.commit();
//            success = (rows == 1);
//            // store the last_insert_id of the Bestellung object in lastId,
//            rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
//            if (rs.next()) {
//                bestellung.setIdOrder(rs.getInt(1));
//            } else {
//                success = false;
//                // throw an exception from here
//            }
//            /**
//             * Now save the OrderPositions to Table OrderPosition which is a n:m
//             * relation of Table Bestellung and Table Gericht.
//             */
//
//            pStmt = conn.prepareStatement("INSERT INTO OrderPosition (keyOrder, keyGericht, "
//                    + "amountPosition) "
//                    + "VALUES(?,?, ?)");
//            /**
//             * The lastId from the Bestellung-Table is the key forOrder in Table
//             * OrderPosition .. and it is a single value for all Gerichte, so we
//             * can set ot outside the for loop
//             */
//            pStmt.setInt(1, bestellung.getIdOrder());
//
//            for (Gericht temp : orderedGerichte) {
//                pStmt.setInt(2, temp.getGerichtId());
//                pStmt.setInt(3, temp.getAmount());
//                rows = pStmt.executeUpdate();
//                conn.commit();
//                success = (rows == 1);
//            }
//            return success;
////         
//        } catch (SQLException ex) {
//            Logger.getLogger(TransmitSaveBestellungSessionBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return success;

    } // end method
} // end  class

