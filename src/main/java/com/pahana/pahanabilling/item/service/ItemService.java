package com.pahana.pahanabilling.item.service;

import com.pahana.pahanabilling.item.dao.ItemDAO;
import com.pahana.pahanabilling.item.entity.Item;

import java.util.List;

public class ItemService {
    private final ItemDAO itemDAO = new ItemDAO();

    public void addItem(Item item) throws Exception {
        itemDAO.save(item);
    }

    public List<Item> listItems() throws Exception {
        return itemDAO.getAllItems();
    }
}

