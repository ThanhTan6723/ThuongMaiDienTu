package controller.client.product;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.admin.ProductAdminDAO;
import dao.client.ProductDAO;
import model.*;

@WebServlet("/DetailControl")
public class DetailControl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Set character encoding and content type
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String productId = request.getParameter("pid");
		System.out.println(productId);
		int pid = Integer.parseInt(productId);
		Product product = ProductDAO.getProductById(pid);
		// Ensure product is not null
		assert product != null;
		ProductDAO.incrementViewCount(pid);
		int category_id = product.getCategory().getId();
		List<Product> relativeProduct = ProductDAO.relativeProduct(category_id);
		request.setAttribute("relativeProduct", relativeProduct);
		int daban= 0;
        for(Batch b : product.getBatches()){
			daban += (b.getQuantity()-b.getCurrentQuantity());
		}
		request.setAttribute("daban",daban);
        // Tạo một bản sao của đối tượng Product
        product.setId(product.getId());
        product.setName(product.getName());
		product.setPrice(product.getPrice());
		product.setImage(product.getImage());
		product.setDescription(product.getDescription());
		product.setCategory(product.getCategory());
		product.setImages(product.getImages());

		// Get additional product details
		List<Image> listImageProduct = ProductDAO.listImageProduct(product.getId());
		String nameCategory = ProductDAO.getCategoryById(product.getCategory().getId());
		List<Batch> listBatch = ProductDAO.getListBatchById(product.getId());
		List<Provider> listProvider = ProductDAO.getListProviderByIdP(pid);
		List<Product> listInventory = ProductAdminDAO.getListProducts();


		// Get reviews and calculate statistics
		List<Review> reviewList = ProductDAO.getListReviewsByProductId(pid);
		Map<Integer, Integer> ratingCount = new HashMap<>();
		int totalReviews = reviewList.size();
		int totalRating = 0;

		for (Review review : reviewList) {
			int rating = review.getRating();
			ratingCount.put(rating, ratingCount.getOrDefault(rating, 0) + 1);
			totalRating += rating;
		}

		// Calculate average rating and rating percentages
		double averageRating = totalReviews > 0 ? (double) totalRating / totalReviews : 0;

		// Format the average rating to one decimal place
		DecimalFormat averageRatingFormat = new DecimalFormat("#.#");
		averageRatingFormat.setRoundingMode(RoundingMode.HALF_UP);
		String formattedAverageRating = averageRatingFormat.format(averageRating);

		// Calculate and format rating percentages
		DecimalFormat percentageFormat = new DecimalFormat("#");
		percentageFormat.setRoundingMode(RoundingMode.HALF_UP);
		Map<Integer, String> ratingPercentage = new HashMap<>();
		for (int i = 1; i <= 5; i++) {
			double percentage = totalReviews > 0 ? (double) ratingCount.getOrDefault(i, 0) * 100 / totalReviews : 0;
			ratingPercentage.put(i, percentageFormat.format(percentage));
		}

		// Print statistics for debugging
		System.out.println("Average Rating: " + formattedAverageRating);
		System.out.println("Rating Percentage: " + ratingPercentage);

		// Set attributes for JSP
		request.setAttribute("averageRating", formattedAverageRating);
		request.setAttribute("ratingPercentage", ratingPercentage);
		request.setAttribute("allreviews", totalReviews);
		request.setAttribute("productCurrentQuantities", ProductDAO.getTotalQuantityP(pid));
		request.setAttribute("reviews", reviewList);
		request.setAttribute("listImageProduct", listImageProduct);
		request.setAttribute("nameCategory", nameCategory);
		request.setAttribute("detail", product);
		// Format price for display
		DecimalFormat decimalFormat = new DecimalFormat("#,##0");
		decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
		String formattedPrice = decimalFormat.format(product.getPrice());
		request.setAttribute("formattedPrice", formattedPrice);
		request.setAttribute("listBatch", listBatch);
		request.setAttribute("listProvider",listProvider);
		request.setAttribute("listInventory", listInventory);
		List<Product> listProductSame = ProductDAO.getListProductByIdCategory(product.getCategory().getId());
		request.setAttribute("listProductSame",listProductSame);

		request.getRequestDispatcher("/WEB-INF/client/product-detail.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Call doGet method
		doGet(request, response);
	}
}
