package com.pahana.pahanabilling.item.servlet;

import com.pahana.pahanabilling.item.entity.Item;
import com.pahana.pahanabilling.item.service.ItemService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
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
        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");
        String itemId = req.getParameter("itemId");
        String name = req.getParameter("name");
        String priceStr = req.getParameter("price");

        try {
            if ("edit".equalsIgnoreCase(action)) {
                // Edit: requires price
                double price = Double.parseDouble(priceStr);
                itemService.updateItem(new Item(itemId, name, price));
                req.setAttribute("success", " Item updated successfully!");

            } else if ("delete".equalsIgnoreCase(action)) {
                // Delete: do NOT parse price
                try {
                    itemService.deleteItem(itemId);
                    req.setAttribute("success", "️ Item deleted successfully!");
                } catch (Exception ex) {
                    if (isForeignKeyViolation(ex)) {
                        req.setAttribute("error", " Cannot delete this item because it is used in one or more bills.");
                    } else {
                        throw ex;
                    }
                }

            } else {
                // Add new: requires price
                double price = Double.parseDouble(priceStr);
                if (itemService.itemExists(itemId)) {
                    req.setAttribute("error", " Item ID already exists!");
                } else {
                    itemService.addItem(new Item(itemId, name, price));
                    req.setAttribute("success", " Item added successfully!");
                }
            }
        } catch (NumberFormatException nfe) {
            req.setAttribute("error", " Invalid price.");
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

    // Detect FK violations from MySQL (SQLState 23000, error codes 1451/1452 or message contains "foreign key")
    private boolean isForeignKeyViolation(Throwable t) {
        while (t != null) {
            if (t instanceof SQLException se) {
                String state = se.getSQLState();
                int code = se.getErrorCode();
                String msg = se.getMessage() != null ? se.getMessage().toLowerCase() : "";
                if ("23000".equals(state) || code == 1451 || code == 1452 || msg.contains("foreign key")) {
                    return true;
                }
            } else {
                String msg = t.getMessage();
                if (msg != null && msg.toLowerCase().contains("foreign key")) {
                    return true;
                }
            }
            t = t.getCause();
        }
        return false;
    }
}