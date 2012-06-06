package com.restfully.shop.services;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/services")
public class OrderApplication extends Application {
   private Set<Object> singletons = new HashSet<Object>();

   public OrderApplication() {
      singletons.add(new InfinispanResource());
      singletons.add(new JmsResource());
   }

   @Override
   public Set<Object> getSingletons() {
      return singletons;
   }
}
