/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package livescore;

/**
 *
 * @author Bukhoree
 */
import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import java.util.Scanner;

public class Main {

    @Resource(mappedName = "jms/SimpleJMSTopic")
    private static Topic topic;
    @Resource(mappedName = "jms/ConnectionFactory")
    private static ConnectionFactory connectionFactory;
    @Resource(mappedName = "jms/SimpleJMSQueue")
    private static Queue queue;

    public static void main(String[] args) {
        System.out.println("Live Score Program");
        String destType = args[0];

        System.out.println("Destination type is " + destType);
        Destination dest = (Destination) topic;
        switch (destType) {
            case "queue":
                dest = (Destination) queue;
                break;
            case "topic":
                dest = (Destination) topic;
                break;
            default:
                System.err.println("Argument must be \"queue\" or " + "\"topic\"");
                System.exit(1);
                return;
        }

        Scanner sc = new Scanner(System.in);
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(dest);
            TextMessage message = session.createTextMessage();

            String text;
            do {
                System.out.print("Enter Live Score (q to exit) : ");
                text = sc.nextLine();
                if (!text.equals("q")) {
                    message.setText(text);
                    producer.send(message);
                }
            } while (!text.equals("q"));

            producer.send(session.createMessage());
        } catch (JMSException e) {
            System.err.println("Exception occurred: " + e.toString());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                }
            }
        }
    }
}
