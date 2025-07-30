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

    public Item getItemById(String itemId) throws Exception {
        return itemDAO.findById(itemId);
    }

    public void updateItem(Item item) throws Exception {
        itemDAO.update(item);
    }

    public void deleteItem(String itemId) throws Exception {
        itemDAO.delete(itemId);
    }

    public boolean itemExists(String itemId) throws Exception {
        return itemDAO.exists(itemId);
    }

    public List<Item> searchItems(String keyword) throws Exception {
        return itemDAO.searchByName(keyword);
    }
}