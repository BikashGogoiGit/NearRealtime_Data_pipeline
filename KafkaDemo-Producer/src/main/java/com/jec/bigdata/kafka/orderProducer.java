package com.jec.bigdata.kafka;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class orderProducer {

    public static void main(String[] args) {

        //SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String currentDate = formatter.format(date);
        String startDate = "2020-01-01 00:00:00";

        System.out.println("Start Date::: "+startDate);
        System.out.println("Current Date::: "+currentDate);

        KafkaProducer<String,String> producer = createProducer();
        String topicName = "JEC-Order-Topic";

        while (true) {
            String query = "SELECT * from order2 where modified_date > '"+ startDate +"' and modified_date < '" +currentDate +"'";
            //String query = "SELECT * from order2";
            System.out.println("query::: "+query);

            try {
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/kafka", "root", "");
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(query); {
                if (rs.next()) {
                    //System.out.println(rs.getString(1));
                    //System.out.println(rs.getString(2));

                    String kafkaOutMessage = buildOrderMessage(rs);
                    //System.out.println("OrderMessage::: " +kafkaOutMessage);

                    try {
                        int partition = producer.send(new ProducerRecord<>(topicName, kafkaOutMessage))
                                .get()
                                .partition();
                        System.out.println("Sent to partition : " + partition + " Message::: " + kafkaOutMessage);
                        System.out.println("Message::: " + kafkaOutMessage);
                    } catch (ExecutionException eex) {
                        Logger lgr = Logger.getLogger(orderProducer.class.getName());
                        lgr.log(Level.SEVERE, eex.getMessage(), eex);
                    }
                }
                Thread.sleep(60000);
            }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(orderProducer.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
            catch (InterruptedException iex){
                Logger lgr = Logger.getLogger(orderProducer.class.getName());
                lgr.log(Level.SEVERE, iex.getMessage(), iex);
            }

            startDate = currentDate;
            Date newDate = new Date(System.currentTimeMillis());
            currentDate = formatter.format(newDate);
        }

    }

    private static KafkaProducer <String, String> createProducer() {

        System.out.println("Starting Producer");

        //String topicName = "JEC-Order-Topic";
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String,String> producer = new KafkaProducer <>(props);

        return producer;
    }

    private static Connection createConnection(){
        String url = "jdbc:mysql://localhost:3306/jec?useSSL=false";
        String user = "root";
        String password = "xxxxxxxxxxx";

        Connection con;

        try {
            con = DriverManager.getConnection(url, user, password);
            return con;
        }
        catch(SQLException sqlEx){

        }
        return null;

    }


    public static String buildOrderMessage (ResultSet rs) {
        try {
            return rs.getString(1) + "~~~" +
                    rs.getString(2) + "~~~"+
                    rs.getString(3) + "~~~"+
                    rs.getString(4) + "~~~"+
                    rs.getString(5) + "~~~"+
                    rs.getString(6) + "~~~"+
                    rs.getString(7) + "~~~"+
                    rs.getString(8) + "~~~"+
                    rs.getString(9) + "~~~"+
                    rs.getString(10) + "~~~"+
                    rs.getString(11) + "~~~"+
                    rs.getString(12) + "~~~"+
                    rs.getString(13) + "~~~"+
                    rs.getString(14) + "~~~"+
                    rs.getString(15) + "~~~"+
                    rs.getString(16) + "~~~"+
                    rs.getString(17) + "~~~"+
                    rs.getString(18) + "~~~"+
                    rs.getString(19) + "~~~"+
                    rs.getString(20) + "~~~"+
                    rs.getString(21) + "~~~"+
                    rs.getString(22) + "~~~"+
                    rs.getString(23) + "~~~"+
                    rs.getString(24) + "~~~"+
                    rs.getString(25) + "~~~"+
                    rs.getString(26) + "~~~"+
                    rs.getString(27) + "~~~"+
                    rs.getString(28) + "~~~"+
                    rs.getString(29) + "~~~"+
                    rs.getString(30) + "~~~"+
                    rs.getString(31) + "~~~"+
                    rs.getString(32) + "~~~"+
                    rs.getString(33) + "~~~"+
                    rs.getString(34) + "~~~"+
                    rs.getString(35) + "~~~"+
                    rs.getString(36) + "~~~"+
                    rs.getString(37) + "~~~"+
                    rs.getString(38) + "~~~"+
                    rs.getString(39) + "~~~"+
                    rs.getString(40) + "~~~"+
                    rs.getString(41) + "~~~"+
                    rs.getString(42) + "~~~"+
                    rs.getString(43) + "~~~";
        }
        catch (SQLException sqlEx)
        {
        }
        return "";
    }



}
