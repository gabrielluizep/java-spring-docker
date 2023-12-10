package eng.tele.std;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

@SpringBootApplication
public class StdApplication {
	public static Channel channel;

	public static void main(String[] args) {
		ConnectionFactory factory = RabbitMqConnection.getConnectionFactory();

		try (Connection connection = factory.newConnection()) {
			StdApplication.channel = connection.createChannel();
		} catch (Exception e) {
			System.err.println("Could not stablish connection with RabbitMQ\n");
		}

		SpringApplication.run(StdApplication.class, args);
	}
}
