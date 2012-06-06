package com.restfully.shop.services;

import org.judcon.CachingService;
import org.switchyard.quickstarts.demo.multiapp.Order;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Path("/caching")
public class InfinispanResource {
	private AtomicInteger idCounter = new AtomicInteger();
	
	CachingService caching;

	public InfinispanResource() {
		caching = new CachingService();
	}

	@POST
	@Consumes("application/xml")
	public Response createOrder(InputStream is) {
		Order order = readOrder(is);
		System.out.println("!!! POST " + order );
		order.setId(idCounter.incrementAndGet());
		caching.put(order.getId(), order.getOrderId());
		System.out.println("Created order " + order.getId());
		return Response.created(URI.create("/caching/" + order.getId()))
				.build();

	}

	@GET
	@Path("{id}")
	@Produces("application/xml")
	public StreamingOutput getOrder(@PathParam("id") String id) {
		System.out.println("!!! GET");
		final Order order = (Order)caching.get(id);
		if (order == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return new StreamingOutput() {
			public void write(OutputStream outputStream) throws IOException,
					WebApplicationException {
				outputOrder(outputStream, order);
			}
		};
	}

	@PUT
	@Path("{id}")
	@Consumes("application/xml")
	public void updateOrder(@PathParam("id") String id, InputStream is) {
		Order update = readOrder(is);
		Order current = (Order)caching.get(id);
		if (current == null)
			throw new WebApplicationException(Response.Status.NOT_FOUND);

		current.setOrderId(update.getOrderId());
		current.setItemId(update.getItemId());
		current.setQuantity(update.getQuantity());
	}

	protected void outputOrder(OutputStream os, Order order)
			throws IOException {
		PrintStream writer = new PrintStream(os);
		writer.println("<order id=\"" + order.getId() + "\">");
		writer.println("   <order-id>" + order.getOrderId() + "</order-id>");
		writer.println("   <item-id>" + order.getItemId() + "</item-id>");
		writer.println("   <quantity>" + order.getQuantity() + "</quantity>");
		writer.println("</order>");
	}

	protected Order readOrder(InputStream is) {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = builder.parse(is);
			Element root = doc.getDocumentElement();
			Order order = new Order();
			if (root.getAttribute("id") != null
					&& !root.getAttribute("id").trim().equals(""))
				order.setId(Integer.valueOf(root.getAttribute("id")));
			NodeList nodes = root.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) nodes.item(i);
				if (element.getTagName().equals("order-id")) {
					order.setOrderId(element.getTextContent());
				} else if (element.getTagName().equals("item-id")) {
					order.setItemId(element.getTextContent());
				} else if (element.getTagName().equals("quantity")) {
					order.setQuantity(Integer.parseInt(element.getTextContent()));
				} 
			}
			return order;
		} catch (Exception e) {
			throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
		}
	}

}
