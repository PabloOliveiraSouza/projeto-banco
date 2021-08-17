package KafkaProducer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;


import java.util.Properties;
import java.util.concurrent.ExecutionException;


public class KafkaProducerSaques {
    public void EnviarDadosClienteSaque(String Cpf) throws ExecutionException, InterruptedException {
        var producer = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(properties());
        var value = Cpf;
        var record = new ProducerRecord<>("BANCO_NOVO_SAQUE", value, value);
        producer.send(record, (data, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                return;
            }
            //observer
            System.out.println("Mensagem Enviada Com sucesso: " + data.topic() + ":::partition: " + data.partition() + "/offset: " + data.offset() + "/timestamp: " + data.timestamp());
        }).get();
    }

    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }

}