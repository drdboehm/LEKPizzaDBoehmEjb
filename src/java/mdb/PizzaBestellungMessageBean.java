/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
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

    public PizzaBestellungMessageBean() {
    }

    @Override
    public void onMessage(Message message) {
        ObjectMessage myMsg = (ObjectMessage) message;
        System.out.println("Message auf Queue erkannt!" + myMsg.toString());

    }

}
