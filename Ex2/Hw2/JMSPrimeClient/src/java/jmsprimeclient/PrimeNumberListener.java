/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmsprimeclient;

/**
 *
 * @author Bukhoree
 */
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.concurrent.CountDownLatch;

public class PrimeNumberListener implements MessageListener {
    private int count;
    private final CountDownLatch latch;

    public PrimeNumberListener(CountDownLatch latch) {
        this.count = 0;
        this.latch = latch;
    }

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            count += Integer.parseInt(text);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            latch.countDown();
        }
    }

    public int getCount() {
        return count;
    }
}

