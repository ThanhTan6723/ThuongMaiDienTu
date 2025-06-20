package controller.admin.product;
import dao.client.ProductDAO;
import model.*;
import java.io.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import static dao.client.ProductDAO.updateProductAndBatches;

@MultipartConfig
@WebServlet(name = "EditProductControll", value = "/EditProductControll")
public class EditProductControll extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pid = request.getParameter("id");
		int product_id = Integer.parseInt(pid);
		List<Category> cateList = ProductDAO.getListCategory();
		request.setAttribute("catelist", cateList);
		List<Provider> providerList = ProductDAO.getListProvider();
		List<Batch> batchList = ProductDAO.getListBatchById(product_id);

		request.setAttribute("providerList", providerList);
		Product product = ProductDAO.getProductById(product_id);
		request.setAttribute("product", product);
		request.setAttribute("batchList", batchList);
		request.getRequestDispatcher("WEB-INF/admin/edit-product.jsp").forward(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");

		if (!ServletFileUpload.isMultipartContent(request)) {
			throw new ServletException("Content type is not multipart/form-data");
		}

		Map<String, String> formFields = new HashMap<>();
		String productImageFileName = null;
		boolean compressionSuccess = false;

		try {
			List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);

			for (FileItem item : items) {
				if (item.isFormField()) {
					formFields.put(item.getFieldName(), item.getString("UTF-8"));
				} else if (item.getFieldName().equals("product-image")) {
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
					productImageFileName = "images/" + fileName;

					// Kiểm tra kích thước nén và gốc để xác định nén thành công hay không
					long originalSize = item.getSize();
					long compressedSize = compressedBaos.size();
					compressionSuccess = compressedSize < originalSize;
				}
			}
		} catch (Exception ex) {
			throw new ServletException("File upload failed", ex);
		}

		// Kiểm tra xem nén thành công hay không
		if (compressionSuccess) {
			System.out.println("Image compressed successfully.");
		} else {
			System.out.println("Compression did not reduce the image size.");
		}
		String product_id = formFields.get("product-id");
		String product_name = formFields.get("product-name");
		String product_price = formFields.get("product-price");
		String product_desc = formFields.get("product-desc");
		String product_cate = formFields.get("product-cate");


			Category category = new Category(Integer.parseInt(product_cate));
			Product p = new Product(Integer.parseInt(product_id), product_name, Double.parseDouble(product_price),
					productImageFileName, product_desc, category);
        try {
            updateProductAndBatches(p,Integer.parseInt(product_id));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        response.sendRedirect("/LoadProductsPage");


	}
}
