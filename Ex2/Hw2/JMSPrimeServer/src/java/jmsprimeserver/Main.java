/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmsprimeserver;

/**
 *
 * @author Bukhoree
 */
import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.TemporaryQueue;

public class Main implements MessageListener {
  @Resource(mappedName = "jms/ConnectionFactory")
  private static ConnectionFactory connectionFactory;
  @Resource(mappedName = "jms/SimpleJMSQueue")
  private static Queue queue;
  
  public static void main(String[] args) {
  Main main = new Main();
  try {
    main.start();
   } catch (JMSException e) {
    e.printStackTrace();
   }
  }

  public void start() throws JMSException {
    Connection connection = connectionFactory.createConnection();
    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    MessageConsumer consumer = session.createConsumer(queue);
    consumer.setMessageListener(this);
    connection.start();
  }

  @Override
  public void onMessage(Message message) {
    if (message instanceof TextMessage) {
      TextMessage textMessage = (TextMessage) message;
      try {
        String[] range = textMessage.getText().split(",");
        int lower = Integer.parseInt(range[0].trim());
        int upper = Integer.parseInt(range[1].trim());

        int count = countPrimes(lower, upper);

        TemporaryQueue replyQueue = (TemporaryQueue) textMessage.getJMSReplyTo();
        Session session = connectionFactory.createConnection().createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer producer = session.createProducer(replyQueue);
        TextMessage reply = session.createTextMessage(String.valueOf(count));
        producer.send(reply);

        session.close();
      } catch (JMSException e) {
        e.printStackTrace();
      }
    }
  }

  private int countPrimes(int lower, int upper) {
    int count = 0;
    for (int i = lower; i <= upper; i++) {
      if (isPrime(i)) {
        count++;
      }
    }
    return count;
  }

  private boolean isPrime(int n) {
    if (n <= 1) {
      return false;
    }
    for (int i = 2; i <= Math.sqrt(n); i++) {
      if (n % i == 0) {
        return false;
      }
    }
    return true;
  }
}

