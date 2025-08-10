package com.pahana.pahanabilling.item.servlet;

import com.pahana.pahanabilling.item.entity.Item;
import com.pahana.pahanabilling.item.service.ItemService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/items")
public class ItemServlet extends HttpServlet {
    private final ItemService itemService = new ItemService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        loadItemList(req);
        req.getRequestDispatcher("/items.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        String itemId = req.getParameter("itemId");
        String name = req.getParameter("name");
        double price = Double.parseDouble(req.getParameter("price"));

        try {
            if ("edit".equalsIgnoreCase(action)) {
                itemService.updateItem(new Item(itemId, name, price));
                req.setAttribute("success", "✅ Item updated successfully!");
            } else if ("delete".equalsIgnoreCase(action)) {
                itemService.deleteItem(itemId);
                req.setAttribute("success", "🗑️ Item deleted successfully!");
            } else {
                // Add new
                if (itemService.itemExists(itemId)) {
                    req.setAttribute("error", "⚠️ Item ID already exists!");
                } else {
                    itemService.addItem(new Item(itemId, name, price));
                    req.setAttribute("success", "✅ Item added successfully!");
                }
            }
        } catch (Exception e) {
            req.setAttribute("error", "❌ Error: " + e.getMessage());
            e.printStackTrace();
        }

        loadItemList(req);
        req.getRequestDispatcher("/items.jsp").forward(req, resp);
    }

    private void loadItemList(HttpServletRequest req) {
        try {
            List<Item> items = itemService.listItems();
            req.setAttribute("items", items);
        } catch (Exception e) {
            req.setAttribute("error", "Error loading items: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
