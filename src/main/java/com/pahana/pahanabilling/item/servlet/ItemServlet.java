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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();

        String itemId = req.getParameter("itemId");
        String name = req.getParameter("name");
        double price = Double.parseDouble(req.getParameter("price"));
        Item item = new Item(itemId, name, price);

        try {
            if (!item.isValid()) {
                req.setAttribute("error", "❌ Invalid item data.");
                forwardToList(req, resp);
                return;
            }

            if ("/items/edit".equals(path)) {
                itemService.updateItem(item);
            } else {
                if (itemService.itemExists(itemId)) {
                    req.setAttribute("error", "⚠️ Item ID already exists.");
                    forwardToList(req, resp);
                    return;
                }
                itemService.addItem(item);
            }

            resp.sendRedirect(req.getContextPath() + "/items");

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Error processing item: " + e.getMessage());
            forwardToList(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();

        try {
            if ("/items/edit".equals(path)) {
                String itemId = req.getParameter("itemId");
                Item item = itemService.getItemById(itemId);
                req.setAttribute("item", item);
                req.getRequestDispatcher("/WEB-INF/editItem.jsp").forward(req, resp);

            } else if ("/items/delete".equals(path)) {
                String itemId = req.getParameter("itemId");
                itemService.deleteItem(itemId);
                resp.sendRedirect(req.getContextPath() + "/items");

            } else { // "/items"
                String search = req.getParameter("search");
                List<Item> items;
                if (search != null && !search.trim().isEmpty()) {
                    items = itemService.searchItems(search.trim());
                    req.setAttribute("search", search);
                } else {
                    items = itemService.listItems();
                }
                req.setAttribute("items", items);
                req.getRequestDispatcher("/WEB-INF/items.jsp").forward(req, resp);
            }

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Error: " + e.getMessage());
            forwardToList(req, resp);
        }
    }

    private void forwardToList(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("items", itemService.listItems());
            req.getRequestDispatcher("/WEB-INF/views/items.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("Error loading item list: " + e.getMessage());
        }
    }
}
