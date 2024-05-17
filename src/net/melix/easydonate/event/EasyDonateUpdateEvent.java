package net.melix.easydonate.event;

import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;

import java.util.Map;

public class EasyDonateUpdateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Map<String, Object> productsData;

    public EasyDonateUpdateEvent(Map<String, Object> data){
        this.productsData = data;
    }

    public Map<String, Object> getProductsData(){
        return this.productsData;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

}
