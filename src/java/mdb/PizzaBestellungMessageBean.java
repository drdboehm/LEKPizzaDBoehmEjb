/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdb;

import com.daa.ejb.TransmitSaveBestellungSessionBeanRemote;
import com.daa.model.BestellWrapper;
import com.daa.model.Bestellung;
import com.daa.model.Gericht;
import com.daa.model.Kunde;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 *
 * @author dboehm
 */
@MessageDriven(mappedName = "jms/myPizzaBestellQueue", activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class PizzaBestellungMessageBean implements MessageListener {

    @EJB
    private TransmitSaveBestellungSessionBeanRemote transmitSaveBestellungSessionBean;
    private static final long serialVersionUID = 1L;

    public PizzaBestellungMessageBean() {
    }
    
    @Override
    public void onMessage(Message message) {
        try {
            ObjectMessage myMsg = (ObjectMessage) message;
//            System.out.println("ObjectMessage auf Queue erkannt!" + myMsg.toString());
            BestellWrapper b = (BestellWrapper) myMsg.getObject();
            System.out.println("BestellWrapper auf Queue erkannt!" + b.toString());
            Kunde kunde = b.getKunde();
            Bestellung bestellung = b.getBestellung();
            List<Gericht> gerichte = b.getGerichte();
            System.out.println("Kunde auf Queue erkannt!" + kunde.toString());
            System.out.println("Bestellung auf Queue erkannt!" + bestellung.toString());
            for (Gericht g : gerichte) {
//                System.out.println(g.getBezeichnung() + " " + g.getAmount());
            }
            
            transmitSaveBestellungSessionBean.storeEjb(b);
        } catch (JMSException ex) {
            Logger.getLogger(PizzaBestellungMessageBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
