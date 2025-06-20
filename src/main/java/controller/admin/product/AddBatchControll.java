package controller.admin.product;

import dao.admin.ProductAdminDAO;
import dao.client.ProductDAO;
import model.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet(name = "AddBatchControll", value = "/AddBatchControll")
public class AddBatchControll extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            request.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=UTF-8");
            request.getRequestDispatcher("WEB-INF/admin/add-existProduct.jsp").forward(request, response);
        }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            request.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=UTF-8");
            HttpSession session = request.getSession();
            Account loggedInUser = (Account) session.getAttribute("account");
            System.out.println("Người dùng: "+loggedInUser);
            String[] productIds = request.getParameterValues("product-ID[]");
            String[] productBatchNames = request.getParameterValues("product-Batch-name[]");
            String[] manufacturingDates = request.getParameterValues("manufacturingDate[]");
            String[] expiryDates = request.getParameterValues("expiryDate[]");
            String[] dateOfImportings = request.getParameterValues("dateOfImporting[]");
            String[] productBatchQuantities = request.getParameterValues("product-Batch-quantity[]");
            String[] productPriceImports = request.getParameterValues("product-priceImport[]");
            String[] providerNames = request.getParameterValues("providerName[]");
            String[] providerAddresses = request.getParameterValues("providerAddress[]");
            String[] providerIds = request.getParameterValues("providerId[]");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            for (int i = 0; i < productBatchNames.length; i++) {
                String productId = productIds[i];
                String productBatchName = productBatchNames[i];
                String manufacturingDate = manufacturingDates[i];
                String expiryDate = expiryDates[i];
                String dateOfImporting = dateOfImportings[i];
                String productBatchQuantity = productBatchQuantities[i];
                String productPriceImport = productPriceImports[i];
                String providerId = providerIds[i];
                String providerName = providerNames[i];
                String providerAddress = providerAddresses[i];
                try {
                    Date manufacturingDateParsed = dateFormat.parse(manufacturingDate);
                    Date expiryDateParsed = dateFormat.parse(expiryDate);
                    Date dateOfImportingParsed = dateFormat.parse(dateOfImporting);
                    List<Batch> batchList = new ArrayList<>();
                    Provider provider = ProductDAO.getProviderById(Integer.parseInt(providerId));
                    if(provider ==null) {
                        // Tạo đối tượng Provider và Batch
                        provider = new Provider(providerName, providerAddress);
                    }
                    Batch batch = new Batch(productBatchName,manufacturingDateParsed,expiryDateParsed,dateOfImportingParsed,Integer.parseInt(productBatchQuantity),
                            Integer.parseInt(productBatchQuantity),Double.parseDouble(productPriceImport),
                            provider,loggedInUser);
                    batchList.add(batch);
                    ProductAdminDAO.addBatchesToProduct(Integer.parseInt(productId),batchList);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
            response.sendRedirect("LoadProductsPage");
        }

}