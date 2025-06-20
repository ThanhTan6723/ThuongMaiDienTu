package controller.client.product;

import com.google.gson.JsonObject;
import dao.client.ProductDAO;
import model.Review;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(name = "ReviewControll", value = "/ReviewControll")
public class ReviewControll extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Tránh vòng lặp vô hạn
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "GET method is not supported");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        // Đối tượng JSON phản hồi
        JsonObject jsonResponse = new JsonObject();

        String id = null;
        String ratingStr = null;
        String comment = null;
        String name = null;
        String phone = null;
        Review review = new Review();

        if (ServletFileUpload.isMultipartContent(request)) {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);

            try {
                List<FileItem> formItems = upload.parseRequest(request);
                for (FileItem item : formItems) {
                    if (item.isFormField()) {
                        // Xử lý các trường form thông thường
                        String fieldName = item.getFieldName();
                        String fieldValue = item.getString("UTF-8");
                        switch (fieldName) {
                            case "id":
                                id = fieldValue;
                                break;
                            case "rating":
                                ratingStr = fieldValue;
                                break;
                            case "comments":
                                comment = fieldValue;
                                break;
                            case "name":
                                name = fieldValue;
                                break;
                            case "phone":
                                phone = fieldValue;
                                break;
                        }
                    } else {
                        // Kiểm tra MIME type của tệp
                        if (item.getName() != null && !item.getName().isEmpty()) {
                            String mimeType = getServletContext().getMimeType(item.getName());
                            if (mimeType != null && mimeType.startsWith("image")) {
                                String fileName = new File(item.getName()).getName();
                                String uploadPath = getServletContext().getRealPath("") + File.separator + "images";
                                File uploadDir = new File(uploadPath);
                                if (!uploadDir.exists()) {
                                    uploadDir.mkdir();
                                }
                                String filePath = uploadPath + File.separator + fileName;
                                File storeFile = new File(filePath);

                                try (InputStream fileContent = item.getInputStream()) {
                                    Thumbnails.of(fileContent)
                                            .size(300, 300)
                                            .outputQuality(0.5)
                                            .toFile(storeFile);
                                }
                                String imageUrls = "images/" + fileName;
                                // Gán đường dẫn ảnh vào đối tượng review
                                review.setImage(imageUrls);
                            } else {
                                // Xử lý trường hợp tệp không phải là hình ảnh
                                jsonResponse.addProperty("error", "Tệp không phải là hình ảnh hợp lệ.");
                                response.getWriter().write(jsonResponse.toString());
                                return;
                            }
                        }
                    }
                }
            } catch (FileUploadException e) {
                e.printStackTrace();
            }
        }

        if (id != null && ratingStr != null && comment != null && name != null && phone != null) {
            int rating = Integer.parseInt(ratingStr);
            review.setNameCommenter(name);
            review.setPhoneNumberCommenter(phone);
            review.setRating(rating);
            review.setComment(comment);
            review.setDateCreated(String.valueOf(LocalDateTime.now()));
            review.setProductEvaluated(ProductDAO.getProductById(Integer.parseInt(id)));
            review.setAccept(false);

            // Lưu đánh giá vào cơ sở dữ liệu
            ProductDAO.saveReview(review);

            // Chuẩn bị phản hồi JSON thành công
            jsonResponse.addProperty("success", true);
            jsonResponse.addProperty("message", "Đánh giá đã được gửi thành công.");

            // Gửi phản hồi JSON về client
            response.getWriter().write(jsonResponse.toString());
        } else {
            // Chuẩn bị phản hồi JSON lỗi nếu thiếu thông tin cần thiết
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Thiếu thông tin cần thiết để gửi đánh giá.");
            response.getWriter().write(jsonResponse.toString());
        }
    }
}
