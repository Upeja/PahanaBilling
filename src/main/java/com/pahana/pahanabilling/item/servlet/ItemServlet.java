package com.pahana.pahanabilling.item.servlet;

import com.pahana.pahanabilling.item.entity.Item;
import com.pahana.pahanabilling.item.service.ItemService;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/items")
public class ItemServlet extends HttpServlet {
    private final ItemService itemService = new ItemService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("itemId");
        String name = req.getParameter("name");
        double price = Double.parseDouble(req.getParameter("price"));

        try {
            Item item = new Item(id, name, price);
            itemService.addItem(item);
            resp.sendRedirect("items"); // reload view
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("Error: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Item> items = itemService.listItems();
            req.setAttribute("items", items);
            req.getRequestDispatcher("/WEB-INF/views/items.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("Error: " + e.getMessage());
        }
    }
}
