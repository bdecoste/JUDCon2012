/*
 * JBoss, Home of Professional Open Source Copyright 2009, Red Hat Middleware
 * LLC, and individual contributors by the @authors tag. See the copyright.txt
 * in the distribution for a full listing of individual contributors.
 * 
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 */
package org.switchyard.quickstarts.demo.multiapp.consumer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.jms.BytesMessage;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.milyn.io.StreamUtils;
import org.switchyard.quickstarts.rules.camel.cbr.Widget;
import org.switchyard.test.mixins.HTTPMixIn;
import org.switchyard.test.mixins.HornetQMixIn;

/**
 * JMS-based client for submitting orders to the OrderService.
 */
public final class OrderIntakeClient {
    
    private static final String ORDER_QUEUE_NAME = "OrderRequestQueue";
    private static final String ORDERACK_QUEUE_NAME = "OrderReplyQueue";
    private static final String RULES_QUEUE_NAME = "RulesTriggerQueue";
    
    private static final String IP = "127.6.58.1";
    private static final String HOSTNAME = "jboss1-wdecoste1.rhcloud.com";
    
    private static final String[][] TESTS = new String[][] {
        new String[] {"FF0000-ABC-123", "Red"},
        new String[] {"00FF00-DEF-456", "Green"},
        new String[] {"0000FF-GHI-789", "Blue"}
    };
   
    public static void main(final String[] args) throws Exception {
    	
    	if (args[0].equals("rest")) {
    		System.out.println("------- REST -------");
    		testRestJms();
    	} else if (args[0].equals("http")) {
    	
	    	System.out.println("------- HTTP -------");
	    	testHttp();
    	} else if (args[0].equals("jms")) { 	
	    	System.out.println("------- JMS -------");
	    	testJms();
    	} else if (args[0].equals("drools")) {	
	    	for (String[] test : TESTS) {
	    		System.out.println("------- RULES -------");
	    		testRules(test);
	    	}
    	} else if (args[0].equals("bpm")) {	
	    	System.out.println("------- BPM -------");
	    	testBpm();
    	} else {
    		System.out.println(args[0] + " not supported");
    	}
    	
    }
    
    public static void testRestCaching() throws Exception {
	      System.out.println("*** Testing Infinispan Caching via REST ***");
	      // Create a new order
	      String newOrder = "<order>"
	              + "<order-id>1234</order-id>"
	              + "<item-id>BUTTER</item-id>"
	              + "<quantity>5</quantity>"
	              + "</order>";
	      
	      System.out.println("  Sending Order ...");
	      URL postUrl = new URL("http://" + HOSTNAME + "/services/caching");
	      //URL postUrl = new URL("http://jboss1-bdecoste20a.dev.rhcloud.com/services/caching");
	      HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
	      connection.setDoOutput(true);
	      connection.setInstanceFollowRedirects(false);
	      connection.setRequestMethod("POST");
	      connection.setRequestProperty("Content-Type", "application/xml");
	      OutputStream os = connection.getOutputStream();
	      os.write(newOrder.getBytes());
	      os.flush();
	      int response = connection.getResponseCode();
	      System.out.println("  Received response: " + response);

    }
    
    public static void testRestJms() throws Exception {
	      System.out.println("*** Testing JMS via REST ***");
	      // Create a new order
	      String newOrder = "<order>"
	              + "<order-id>1234</order-id>"
	              + "<item-id>BUTTER</item-id>"
	              + "<quantity>5</quantity>"
	              + "</order>";
	      
	      URL postUrl = new URL("http://" + HOSTNAME + "/services/jms");
	      //URL postUrl = new URL("http://jboss1-bdecoste20a.dev.rhcloud.com/services/jms");
	      HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
	      connection.setDoOutput(true);
	      connection.setDoInput(true);
	      connection.setInstanceFollowRedirects(false);
	      connection.setRequestMethod("POST");
	      connection.setRequestProperty("Content-Type", "application/xml");
	      
	      OutputStream os = connection.getOutputStream();
	      os.write(newOrder.getBytes());
	      os.flush();
	      int response = connection.getResponseCode();
	      System.out.println("  Received POST response " + response + " " + connection.getResponseMessage());
	      
	      String location = connection.getHeaderField("Location");
	      System.out.println("  Location: " + location);
	      
	      URL getUrl = new URL(location);
	      connection = (HttpURLConnection) getUrl.openConnection();
	      connection.setRequestMethod("GET");
	      System.out.println("Content-Type: " + connection.getContentType());

	      BufferedReader reader = new BufferedReader(new
	              InputStreamReader(connection.getInputStream()));

	      System.out.println("  Received POST response " + response + " " + connection.getResponseMessage());
	      String line = reader.readLine();
	      while (line != null)
	      {
	         System.out.println(line);
	         line = reader.readLine();
	      }
	      
	      connection.disconnect();


  }
    
    public static void testBpm() throws Exception {
    	
    	System.out.println("*** Testing SwitchYard/jBPM via HTTP over Port-Forwarding ***");
    	
    	String orderTxt = readFileContent("/home/bdecoste/workspaces/JUDCon2012/bpm-service/src/test/resources/xml/soap-request.xml");
    	
    	HTTPMixIn httpMixIn = new HTTPMixIn();
    	httpMixIn.initialize();
    	
    	// Send a SOAP request and verify the SOAP reply is what we expected
    	String response =  httpMixIn.postString("http://" + IP + ":18001/swydws/ProcessOrder", orderTxt);
    	
    	System.out.println("  HTTP Response " + response);
    }
    
    protected static void testHttp() throws Exception {
    	
    	System.out.println("*** Testing SwitchYard via HTTP over Port-Forwarding ***");
    	
    	String orderTxt = readFileContent("/home/bdecoste/workspaces/JUDCon2012/order-service/src/test/resources/xml/soap-request.xml");
    	
    	HTTPMixIn httpMixIn = new HTTPMixIn();
    	httpMixIn.initialize();
    	
    	String response = httpMixIn.postString("http://" + IP + ":18001/quickstart-demo-multiapp/OrderService", orderTxt);
    	
    	System.out.println("  HTTP Response " + response);
    }
    
    protected static void testRules(String[] data) throws Exception {
    	
    	System.out.println("*** Testing SwitchYard/Drools via JMS over Port-Forwarding ***");
    	
    	String[] TEST = {"FF0000-ABC-123", "Red"};
    	
        HornetQMixIn hqMixIn = new HornetQMixIn(false)							
                                    .setHost(IP)
                                    .setPort(5445);
        hqMixIn.initialize();
        
        try {
            Session session = hqMixIn.getJMSSession();
            MessageProducer producer = session.createProducer(HornetQMixIn.getJMSQueue(RULES_QUEUE_NAME));
            
            System.out.println("Submitting Rules Message ... " + data[0]);
            
            ObjectMessage message = session.createObjectMessage();
            Widget widget = new Widget(data[0]);
            message.setObject(widget);
            producer.send(message);
            System.out.println("Rules triggered ... ");
      
        } finally {
            hqMixIn.uninitialize();
        }
    }
    
    protected static void testJms() throws Exception {
    	
    	System.out.println("*** Testing SwitchYard via JMS over Port-Forwarding ***");
    	
    	String orderTxt = readFileContent("/home/bdecoste/workspaces/JUDCon2012/order-consumer/src/test/resources/order.xml");
    	
        HornetQMixIn hqMixIn = new HornetQMixIn(false)							
                                    .setHost(IP)
                                    .setPort(5445);
        hqMixIn.initialize();
        
        try {
            Session session = hqMixIn.getJMSSession();
            MessageProducer producer = session.createProducer(HornetQMixIn.getJMSQueue(ORDER_QUEUE_NAME));
            
            System.out.println("   Submitting Order via JMS: " + orderTxt);
            producer.send(hqMixIn.createJMSMessage(orderTxt));
            MessageConsumer consumer = session.createConsumer(HornetQMixIn.getJMSQueue(ORDERACK_QUEUE_NAME));
            System.out.println("   Order submitted ... waiting for reply.");
            BytesMessage reply = (BytesMessage)consumer.receive(3000);
            if (reply == null) {
                System.out.println("   No reply received.");
            } else {
                byte[] buf = new byte[1024];
                int count = reply.readBytes(buf);
                String str = new String(buf, 0, count);
                System.out.println("   Received Reply via JMS: " + str);
            }
        } finally {
            hqMixIn.uninitialize();
        }
    }
    
    /**
     * Reads the contends of the {@link #MESSAGE_PAYLOAD} file.
     *
     * @param fileName the name of the file to read.
     * @return String the contents of {@link #MESSAGE_PAYLOAD} file.
     * @throws Exception if an exceptions occurs while reading the file.
     */
    public static String readFileContent(final String fileName) throws Exception {
        InputStream in = null;
        try {
            in = new FileInputStream(fileName);
            return StreamUtils.readStreamAsString(in);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
