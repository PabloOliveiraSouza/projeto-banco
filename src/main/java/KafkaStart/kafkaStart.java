package KafkaStart;

import java.io.IOException;

public class kafkaStart {

    public static void zookeperStart() {
        ProcessBuilder zookeeper = new ProcessBuilder("C:\\Kafka\\batch\\Start_Zookeeper.bat");
        try {
            zookeeper.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void serversStart() {
        ProcessBuilder server1 = new ProcessBuilder("C:\\Kafka\\batch\\Start_Kafka_Server1.bat");
        ProcessBuilder server2 = new ProcessBuilder("C:\\Kafka\\batch\\Start_Kafka_Server2.bat");
        ProcessBuilder consumer = new ProcessBuilder("C:\\Kafka\\batch\\Consumer_product_topic.bat.bat");
        try {
            server1.start();
            server2.start();
            consumer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
