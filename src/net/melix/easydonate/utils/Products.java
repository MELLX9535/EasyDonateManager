package net.melix.easydonate.utils;

import cn.nukkit.Server;
import net.melix.easydonate.event.EasyDonateUpdateEvent;

import java.util.Map;

public class Products {

   private Map<String, Object> productData;

   private boolean update = false;

   public Products(Map<String, Object> data){
       this.productData = data;
   }

   public Map<String, Object> getProductsData(){
       return this.productData;
   }

   public boolean isUpdate(){
       return this.update;
   }

   public void updateConfigData(Map<String, Object> data){
       this.productData = data;
       this.update = true;
       EasyDonateUpdateEvent event = new EasyDonateUpdateEvent(data);
       Server.getInstance().getPluginManager().callEvent(event);

   }

}
