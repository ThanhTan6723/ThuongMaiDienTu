package controller.admin.product;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.admin.ProductAdminDAO;
import dao.client.ProductDAO;
import model.*;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

@MultipartConfig
@WebServlet("/AddProductControll")
public class AddProductControll extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Category> cateList = ProductDAO.getListCategory();
		request.setAttribute("catelist", cateList);
		request.getRequestDispatcher("WEB-INF/admin/add-product.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		HttpSession session = request.getSession();
		Account loggedInUser = (Account) session.getAttribute("account");

		if (!ServletFileUpload.isMultipartContent(request)) {
			throw new ServletException("Content type is not multipart/form-data");
		}

		Map<String, List<String>> formFields = new HashMap<>();
		List<Image> productImages = new ArrayList<>();
		List<Image> productListImages = new ArrayList<>();

		try {
			List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);

			for (FileItem item : items) {
				if (item.isFormField()) {
					String fieldName = item.getFieldName();
					String fieldValue = item.getString("UTF-8");
					formFields.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(fieldValue);
				} else {
					String fileName = new File(item.getName()).getName();
					InputStream fileContent = item.getInputStream();

					// Nén ảnh với Thumbnails
					ByteArrayOutputStream compressedBaos = new ByteArrayOutputStream();
					Thumbnails.of(fileContent)
							.size(300, 300)
							.outputQuality(0.5)
							.toOutputStream(compressedBaos);

					String uploadPath = getServletContext().getRealPath("") + File.separator + "images";
					File uploadDir = new File(uploadPath);
					if (!uploadDir.exists()) {
						uploadDir.mkdir();
					}

					File file = new File(uploadPath + File.separator + fileName);
					try (FileOutputStream fos = new FileOutputStream(file)) {
						fos.write(compressedBaos.toByteArray());
					}
					String fileUrl = "images/" + fileName;

					if (item.getFieldName().equals("product-image[]")) {
						productImages.add(new Image(0, fileUrl));
					} else if (item.getFieldName().equals("product-listimage[]")) {
						productListImages.add(new Image(0, fileUrl));
					}
				}
			}

			// Lấy và xử lý các trường form khác
			String[] productNames = formFields.get("product-name[]").toArray(new String[0]);
			String[] productPrices = formFields.get("product-price[]").toArray(new String[0]);
			String[] productWeights = formFields.get("product-weight[]").toArray(new String[0]);
			String[] productDescriptions = formFields.get("product-desc[]").toArray(new String[0]);
			String[] productCategories = formFields.get("product-cate[]").toArray(new String[0]);
			String[] productBatchNames = formFields.get("product-Batch-name[]").toArray(new String[0]);
			String[] manufacturingDates = formFields.get("manufacturingDate[]").toArray(new String[0]);
			String[] expiryDates = formFields.get("expiryDate[]").toArray(new String[0]);
			String[] dateOfImportings = formFields.get("dateOfImporting[]").toArray(new String[0]);
			String[] productBatchQuantities = formFields.get("product-Batch-quantity[]").toArray(new String[0]);
			String[] productPriceImports = formFields.get("product-priceImport[]").toArray(new String[0]);
			String[] providerNames = formFields.get("providerName[]").toArray(new String[0]);
			String[] providerAddresses = formFields.get("providerAddress[]").toArray(new String[0]);
			String[] providerIds = formFields.get("providerId[]").toArray(new String[0]);

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			List<Product> list = new ArrayList<>();
			for (int i = 0; i < productNames.length; i++) {
				String productName = productNames[i];
				String productPrice = productPrices[i];
				String productWeight = productWeights[i];
				String productDescription = productDescriptions[i];
				String productCategory = productCategories[i];
				String productBatchName = productBatchNames[i];
				String manufacturingDate = manufacturingDates[i];
				String expiryDate = expiryDates[i];
				String dateOfImporting = dateOfImportings[i];
				String productBatchQuantity = productBatchQuantities[i];
				String productPriceImport = productPriceImports[i];
				String providerId = providerIds[i];
				String providerName = providerNames[i];
				String providerAddress = providerAddresses[i];

				// Chuyển đổi định dạng ngày
				Date manufacturingDateParsed = dateFormat.parse(manufacturingDate);
				Date expiryDateParsed = dateFormat.parse(expiryDate);
				Date dateOfImportingParsed = dateFormat.parse(dateOfImporting);
				Provider provider = ProductDAO.getProviderById(Integer.parseInt(providerId));
				if(provider ==null) {
					// Tạo đối tượng Provider và Batch
					 provider = new Provider(providerName, providerAddress);
				}
				Batch batch = new Batch(productBatchName, manufacturingDateParsed, expiryDateParsed, dateOfImportingParsed,
						Integer.parseInt(productBatchQuantity), Integer.parseInt(productBatchQuantity),
						Double.parseDouble(productPriceImport), provider, loggedInUser);

				List<Batch> batches = new ArrayList<>();
				batches.add(batch);

				// Tạo đối tượng Product
				Product product = new Product(productName, Double.parseDouble(productPrice),Double.parseDouble(productWeight),
						productImages.get(i).getUrl(), productDescription,
						new Category(Integer.parseInt(productCategory)), productListImages, batches);
				list.add(product);
			}
			ProductAdminDAO.insertProduct(list);
		} catch (FileUploadException | ParseException e) {
			throw new RuntimeException(e);
		}
		response.sendRedirect("LoadProductsPage");
	}
}