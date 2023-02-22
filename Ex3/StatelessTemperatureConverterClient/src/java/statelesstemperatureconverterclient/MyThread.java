/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package statelesstemperatureconverterclient;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import mybean.StatelessTemperatureConverterBeanRemote;

/**
 *
 * @author Bukhoree
 */
public class MyThread extends Thread {
    private static StatelessTemperatureConverterBeanRemote converter;
    private float temp;

    public MyThread(float fahrenheit) {
        temp = fahrenheit;
    }

    @Override
    public void run() {
        try {
            
            Context context = new InitialContext();
            converter = (StatelessTemperatureConverterBeanRemote) context.lookup("java:global/TemperatureConverter/StatelessTemperatureConverterBean!temperatureconverter.StatelessTemperatureConverterBeanRemote");

            float celsius = converter.fToC(temp);
            System.out.println(temp + " Fahrenheit = " + celsius + " Celsius");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    

    private StatelessTemperatureConverterBeanRemote lookupStatelessTemperatureConverterBeanRemote() {
        try {
            Context c = new InitialContext();
            return (StatelessTemperatureConverterBeanRemote) c.lookup("java:comp/env/StatelessTemperatureConverterBean");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
}
