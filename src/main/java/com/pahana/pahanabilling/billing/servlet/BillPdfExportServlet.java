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

        String billIdParam = req.getParameter("billId");

        if (billIdParam == null) {
            resp.getWriter().write("Missing billId parameter.");
            return;
        }

        try {
            int billId = Integer.parseInt(billIdParam);
            Bill bill = billDAO.findById(billId);

            if (bill == null) {
                resp.getWriter().write("Bill not found.");
                return;
            }

            resp.setContentType("application/pdf");
            resp.setHeader("Content-Disposition", "attachment; filename=bill_" + billId + ".pdf");

            OutputStream out = resp.getOutputStream();
            PdfExportUtil.generateBillPDF(bill, out);
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("Error generating PDF: " + e.getMessage());
        }
    }
}
