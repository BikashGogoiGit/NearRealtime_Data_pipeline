package com.jec.bigdata.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Properties;

public class kafkaProducer1 {

    public static void main(String[] args) throws InterruptedException{

        String topicName = "quickstart-events1";
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String,String> producer = new KafkaProducer <>(props);

        try{
            for (int i=1;i<=10;i++){

                int partition = producer.send(new ProducerRecord<>(topicName, i + "")).get().partition();
                System.out.println("Sent to partition : " +  partition + " Message " + i);
            }
        }
        catch(Exception ex){

            System.out.println("Intrupted"+ex.getMessage());
        }
        finally{
            producer.close();
        }
    }
}