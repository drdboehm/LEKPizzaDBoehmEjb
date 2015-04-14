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
//import com.daa.util.GConnection;
import java.util.Date;
import java.util.List;
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
public class TransmitSaveBestellungSessionBean implements TransmitSaveBestellungSessionBeanRemote {

    @PersistenceUnit(unitName = "PizzaService-ejbPU")
    private EntityManagerFactory emf;//=Persistence.createEntityManagerFactory("PizzaService-ejbPU");

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public List<Gericht> initializeMenu() {
        EntityManager em = emf.createEntityManager();
        return em.createNamedQuery("Gericht.findAll").getResultList();
    }

    @PostConstruct
    public void init() {
//        conn = getConnection();
    }

    @PreDestroy
    public void delete() {
//        try {
//            conn.close();
//        } catch (SQLException ex) {
//            Logger.getLogger(TransmitSaveBestellungSessionBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @Override
    public boolean storeEjb(BestellWrapper bw) {
        Bestellung bestellung = bw.getBestellung();
        bestellung.setOrderDate(new Date());
        Kunde kunde = bw.getKunde();
        List<Gericht> orderedGerichte = bw.getGerichte();
        /*
         Set timespamp fields - later distinguish if Kunde is present, then 
        leave firstEntryDate as is
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
        Orderposition orderpositions;
        for (Gericht g : orderedGerichte) {
            orderpositions = new Orderposition();
            // set the Key 
            orderpositions.setKeyOrder(bestellung);
            orderpositions.setKeyGericht(g);
            orderpositions.setAmountPosition(g.getAmount());
            em.persist(orderpositions);
        }
        em.flush();
        return success;

    } // end method
} // end  class

