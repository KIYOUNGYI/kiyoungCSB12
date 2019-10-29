package src.test.java;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
public class Receiver {
  private final static String QUEUE_NAME = "thumbQueue1";
  public static void main(String[] argv) throws IOException, TimeoutException {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    factory.setVirtualHost("dummy");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    
    HashMap<String, Object> arguments = new HashMap<String, Object>();
    arguments.put("x-queue-type","classic");//명시해줘야 에라가 안난다!!!!!! 
    
    
    channel.queueDeclare(QUEUE_NAME, true, false, false, arguments);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope,
          AMQP.BasicProperties properties, byte[] body) throws IOException {
        String message = new String(body, "UTF-8");
        System.out.println(" [x] Received '" + message + "'");
      }
    };
    channel.basicConsume(QUEUE_NAME, true, consumer);
  }
}