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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

public class Main {
    @Resource(mappedName = "jms/ConnectionFactory")
    private static ConnectionFactory connectionFactory;
    @Resource(mappedName = "jms/SimpleJMSQueue")
    private static Queue queue;

    public static void main(String[] args) {
        Connection connection = null;

        try {
            connection = connectionFactory.createConnection();
            connection.start();

            QueueSession session = (QueueSession) connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            String line = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            while (!line.equals("q")) {
                System.out.print("Enter the range (lower,upper) or 'q' to quit: ");
                line = reader.readLine();

                if (line.equals("q")) {
                    break;
                }

                String[] range = line.split(",");
                if (range.length != 2) {
                    System.out.println("Invalid input, try again");
                    continue;
                }

                int lower = Integer.parseInt(range[0].trim());
                int upper = Integer.parseInt(range[1].trim());

                PrimeNumberListener listener;
                try (MessageConsumer consumer = session.createConsumer(queue)) {
                    CountDownLatch latch = new CountDownLatch(1);
                    listener = new PrimeNumberListener(latch);
                    consumer.setMessageListener(listener);
                    TextMessage message = session.createTextMessage();
                    message.setIntProperty("lower", lower);
                    message.setIntProperty("upper", upper);
                    session.createProducer(queue).send(message);
                    latch.await();
                }

                System.out.println("The number of prime numbers in the range " + lower + " to " + upper + " is: "
                        + listener.getCount());
            }
        } catch (JMSException | IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

