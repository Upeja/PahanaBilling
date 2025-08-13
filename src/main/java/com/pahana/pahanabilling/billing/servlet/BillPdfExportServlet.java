package com.pahana.pahanabilling.billing.servlet;

import com.pahana.pahanabilling.billing.dao.BillDAO;
import com.pahana.pahanabilling.billing.entity.Bill;
import com.pahana.pahanabilling.util.PdfExportUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

@WebServlet("/bills/pdf")
public class BillPdfExportServlet extends HttpServlet {

    private final BillDAO billDAO = new BillDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String billIdParam = req.getParameter("billId");
        if (billIdParam == null || billIdParam.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("❌ Missing 'billId' parameter.");
            return;
        }

        int billId;
        try {
            billId = Integer.parseInt(billIdParam);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("❌ Invalid billId format.");
            return;
        }

        Bill bill;
        try {
            bill = billDAO.findById(billId);
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("❌ Database error while retrieving bill.");
            return;
        }

        if (bill == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("❌ Bill not found for ID: " + billId);
            return;
        }

        // Set PDF response headers
        resp.setContentType("application/pdf");
        resp.setHeader("Content-Disposition", "attachment; filename=bill_" + billId + ".pdf");
        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

        // Generate PDF
        try (OutputStream out = resp.getOutputStream()) {
            try {
                PdfExportUtil.generateBillPDF(bill, String.valueOf(out)); // ✅ catch Exception here
            } catch (Exception e) {
                e.printStackTrace();
                if (!resp.isCommitted()) {
                    resp.reset();
                    resp.setContentType("text/plain");
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    resp.getWriter().write("❌ Error generating PDF: " + e.getMessage());
                }
                return;
            }
            out.flush();
        }
    }
}
