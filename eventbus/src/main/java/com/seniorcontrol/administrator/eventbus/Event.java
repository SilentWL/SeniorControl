package com.seniorcontrol.administrator.eventbus;

import java.util.List;

/**
 * Created by Administrator on 2016/6/21 0021.
 */
public class Event {
    public static class ItemListEvent{
        private List<Item> items;

        public ItemListEvent(List<Item> items){
            this.items = items;
        }
        public List<Item> getItems(){
            return items;
        }
    }
}
