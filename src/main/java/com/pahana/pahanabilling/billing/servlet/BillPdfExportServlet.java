package com.pahana.pahanabilling.billing.servlet;

import com.pahana.pahanabilling.billing.dao.BillDAO;
import com.pahana.pahanabilling.billing.entity.Bill;
import com.pahana.pahanabilling.util.PdfExportUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/bills/pdf")
public class BillPdfExportServlet extends HttpServlet {

    private final BillDAO billDAO = new BillDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Ensure UTF-8 support
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String billIdParam = req.getParameter("billId");

        if (billIdParam == null || billIdParam.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("❌ Missing 'billId' parameter.");
            return;
        }

        try {
            int billId = Integer.parseInt(billIdParam);

            Bill bill = billDAO.findById(billId);
            if (bill == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("❌ Bill not found for ID: " + billId);
                return;
            }

            // Set headers to trigger file download
            resp.setContentType("application/pdf");
            resp.setHeader("Content-Disposition", "attachment; filename=bill_" + billId + ".pdf");
            resp.setHeader("Cache-Control", "no-cache");

            // Generate and stream PDF
            try (OutputStream out = resp.getOutputStream()) {
                PdfExportUtil.generateBillPDF(bill, out);
                out.flush();
            }

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("❌ Invalid billId format.");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("❌ Error generating PDF: " + e.getMessage());
        }
    }
}
