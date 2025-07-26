package com.pahana.pahanabilling.item.servlet;

import com.pahana.pahanabilling.item.entity.Item;
import com.pahana.pahanabilling.item.service.ItemService;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/items", "/items/edit", "/items/delete"})
public class ItemServlet extends HttpServlet {
    private final ItemService itemService = new ItemService();

    // Handle Add and Update
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();

        String itemId = req.getParameter("itemId");
        String name = req.getParameter("name");
        double price = Double.parseDouble(req.getParameter("price"));

        try {
            Item item = new Item(itemId, name, price);

            if ("/items/edit".equals(path)) {
                itemService.updateItem(item);
            } else {
                itemService.addItem(item); // default to add
            }

            resp.sendRedirect(req.getContextPath() + "/items");
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("Error processing item: " + e.getMessage());
        }
    }

    // Handle View, Edit Form and Delete
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();

        try {
            if ("/items/edit".equals(path)) {
                String itemId = req.getParameter("itemId");
                Item item = itemService.getItemById(itemId);
                req.setAttribute("item", item);
                req.getRequestDispatcher("/WEB-INF/views/editItem.jsp").forward(req, resp);

            } else if ("/items/delete".equals(path)) {
                String itemId = req.getParameter("itemId");
                itemService.deleteItem(itemId);
                resp.sendRedirect(req.getContextPath() + "/items");

            } else { // "/items"
                List<Item> items = itemService.listItems();
                req.setAttribute("items", items);
                req.getRequestDispatcher("/WEB-INF/views/items.jsp").forward(req, resp);
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("Error: " + e.getMessage());
        }
    }
}
