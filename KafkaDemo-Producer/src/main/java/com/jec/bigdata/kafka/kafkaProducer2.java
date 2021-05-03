package com.jec.bigdata.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Properties;

import java.text.SimpleDateFormat;
import java.util.Date;
//import java.util.TimeZone;

public class kafkaProducer2 {

    public static void main(String[] args) throws InterruptedException{

        String topicName = "quickstart-events2";
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String,String> producer = new KafkaProducer <>(props);

        try{
            for (int i=1;i<=20;i++){

                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
                Date date = new Date(System.currentTimeMillis());
                //System.out.println(formatter.format(date));

                Thread.sleep(3000);

                int partition = producer.send(new ProducerRecord<>(topicName,"key:"+i, "Message "+i + ": "+formatter.format(date)))
                        .get()
                        .partition();
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