package src.test.java;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
public class Publisher {
  private final static String QUEUE_NAME = "thumbQueue1";
  
  public static void main(String[] args) throws IOException, TimeoutException {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    factory.setVirtualHost("dummy");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    
    String message = "Hello World2";
    channel.basicPublish("thumb1", QUEUE_NAME, null, message.getBytes()); 
    System.out.println(" [x] Sent '" + message + "'");
    channel.close();
    connection.close();
    
    
  }
}