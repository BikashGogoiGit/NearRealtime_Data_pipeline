package com.jec.bigdata.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

//Cassandra Related
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
//import com.datastax.driver.core.Row;


import java.util.Collections;
import java.util.Properties;

public class orderConsumer {


    public static void main(String[] args) throws Exception {
        System.out.println("Starting Main");
        runConsumer();
    }


    static void runConsumer() throws InterruptedException {

        System.out.println("Starting runConsumer");
        final Consumer<String, String> consumer = createConsumer();

        final int giveUp = 10000000;
        int noRecordsCount = 0;

        while (true) {
            final ConsumerRecords<String, String> consumerRecords =
                    consumer.poll(1000);

            //Consumer Terminating Condition
            if (consumerRecords.count()==0) {
                noRecordsCount++;
                if (noRecordsCount > giveUp)
                    break;
                else
                    continue;
            }

            consumerRecords.forEach(record -> {
                System.out.println("Order Message Received: "+ record.value());
                String orderStr = record.value().toString();

                String[] tokens = orderStr.split("~~~");




                //Write into Cassandra
                persist(tokens);

            });
            consumer.commitAsync();
            Thread.sleep(30000);
        }
        consumer.close();
        System.out.println("DONE");
    }

    private static Consumer<String, String> createConsumer() {

        System.out.println("Starting Consumer");

        String topicName = "JEC-Order-Topic";
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        props.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaExampleConsumer1");

        // Create the consumer using props.
        final Consumer<String, String> consumer = new KafkaConsumer<>(props);

        // Subscribe to the topic.
        consumer.subscribe(Collections.singletonList(topicName));
        return consumer;
    }

    private static void persist(String[] inputMessage){

        String orderNumber=inputMessage[0];
        String dateEntered=inputMessage[1];
        String customerNumber=inputMessage[2];
        String itemTotal=inputMessage[3];
        String shippingTotal=inputMessage[4];
        String handlingTotal=inputMessage[5];
        String taxTotal=inputMessage[6];
        String merchandiseDiscount=inputMessage[7];
        String nonmerchandiseDiscount=inputMessage[8];
        String orderTotal=inputMessage[9];
        String merchandiseDiscountType=inputMessage[10];
        String nonMerchandiseDiscountType=inputMessage[11];
        String nonMerchandiseDiscountDescription=inputMessage[12];
        String couponCode=inputMessage[13];
        String status=inputMessage[14];
        String source=inputMessage[15];
        String storeNumber=inputMessage[16];
        String associatedId=inputMessage[17];
        String shipDate=inputMessage[18];
        String shippingMethod=inputMessage[19];
        String trackingNumber=inputMessage[20];
        String shippingFirstName=inputMessage[21];
        String shippingLastName=inputMessage[22];
        String shippingAddress1=inputMessage[23];
        String shippingAddress2=inputMessage[24];
        String shippingAddress3=inputMessage[25];
        String shippingCity=inputMessage[26];
        String shippingState=inputMessage[27];
        String shippingZipcode=inputMessage[28];
        String shippingCountry=inputMessage[29];
        String shippingPhone=inputMessage[30];
        String billingFirstName=inputMessage[31];
        String billingLastName=inputMessage[32];
        String billingAddress1=inputMessage[33];
        String billingAddress2=inputMessage[34];
        String billingAddress3=inputMessage[35];
        String billingCity=inputMessage[36];
        String billingState=inputMessage[37];
        String billingZipcode=inputMessage[38];
        String billingCountry=inputMessage[39];
        String billingPhone=inputMessage[40];
        String createDate=inputMessage[41];
        String modifiedDate=inputMessage[42];


        String serverIP = "127.0.0.1";
        String keyspace = "jec";

        Cluster cluster = Cluster.builder()
                .addContactPoints(serverIP)
                .build();

        Session session = cluster.connect(keyspace);

        String cqlInsert = "INSERT INTO jec.order2 (order_number, date_entered, customer_number, item_total, shipping_total, handling_total, tax_total, merchandise_discount, nonmerchandise_discount, order_total, merchandise_discount_type, non_merchandise_discount_type, non_merchandise_discount_description, coupon_code, status, source, store_number,associated_id,ship_date,shipping_method,tracking_number, shipping_first_name, shipping_last_name, shipping_address1, shipping_address2, shipping_address3,shipping_city, shipping_state, shipping_zipcode, shipping_country, shipping_phone, billing_first_name, billing_last_name, billing_address1, billing_address2, billing_address3, billing_city, billing_state, billing_zipcode, billing_country, billing_phone, create_date, modified_date) " +
                "VALUES ("+
                "'"+orderNumber+"', "+
                "'"+dateEntered+"', "+
                "'"+customerNumber+"', "+
                itemTotal+", "+
                shippingTotal+", "+
                handlingTotal+", "+
                taxTotal+", "+
                merchandiseDiscount+", "+
                nonmerchandiseDiscount+", "+
                orderTotal+", "+
                "'"+merchandiseDiscountType+"', "+
                "'"+nonMerchandiseDiscountType+"', "+
                "'"+nonMerchandiseDiscountDescription+"', "+
                "'"+couponCode+"', "+
                status+", "+
                "'"+source+"', "+
                storeNumber+", "+
                "'"+associatedId+"', "+
                "'"+shipDate+"', "+
                "'"+shippingMethod+"', "+
                "'"+trackingNumber+"', "+
                "'"+shippingFirstName+"', "+
                "'"+shippingLastName+"', "+
                "'"+shippingAddress1+"', "+
                "'"+shippingAddress2+"', "+
                "'"+shippingAddress3+"', "+
                "'"+shippingCity+"', "+
                "'"+shippingState+"', "+
                "'"+shippingZipcode+"', "+
                "'"+shippingCountry+"', "+
                "'"+shippingPhone+"', "+
                "'"+billingFirstName+"', "+
                "'"+billingLastName+"', "+
                "'"+billingAddress1+"', "+
                "'"+billingAddress2+"', "+
                "'"+billingAddress3+"', "+
                "'"+billingCity+"', "+
                "'"+billingState+"', "+
                "'"+billingZipcode+"', "+
                "'"+billingCountry+"', "+
                "'"+billingPhone+"', "+
                "'"+createDate+"', "+
                "'"+modifiedDate+"');";

        System.out.println("cqlInsert::: "+cqlInsert);

        session.execute(cqlInsert);
    }


}
