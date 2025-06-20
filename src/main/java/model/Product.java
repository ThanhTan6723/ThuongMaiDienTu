package model;


import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//import dao.client.ProductDAO;

public class Product {
	private int id;
	private String name;
	private double price;
	private double weight;
	private String image;
	private String description;
	private Category category;
	private List<Image> images;
	private List<Batch> batches;
	private int viewCount;
	public Product() {

		super();
	}


	public Product(String name, double price, String image, String description, Category category) {
		this.name = name;
		this.price = price;
		this.image = image;
		this.description = description;
		this.category = category;
	}

	public void addBatch(Batch batch) {
		if (batches == null) {
			batches = new ArrayList<>();
		}
		batches.add(batch);
	}

	public Product(int id, String name, double price,double weight, String image, String description, Category category, List<Image> images, List<Batch> batches, int viewCount) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.weight = weight;
		this.image = image;
		this.description = description;
		this.category = category;
		this.images = images;
		this.batches = batches;
		this.viewCount = viewCount;
	}

	public Product(int id, String name, double price, double weight, String image, String description, Category category, int viewCount) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.weight = weight;
		this.image = image;
		this.description = description;
		this.category = category;
		this.viewCount = viewCount;
	}

	public Product(String name, double price,double weight, String image, String description, Category category, List<Image> images, List<Batch> batches) {
		this.name = name;
		this.price = price;
		this.weight = weight;
		this.image = image;
		this.description = description;
		this.category = category;
		this.images = images;
		this.batches = batches;
	}

	public Product(int id, String name, double price, String image,String description, Category category,int viewCount) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.image = image;
		this.description = description;
		this.category = category;
		this.viewCount = viewCount;
	}

	public Product(int id, String name, double price, String image, String description, Category category) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.image = image;
		this.description = description;
		this.category = category;
	}

	public Product(int id, String name, double price, String description, Category category, List<Batch> batches) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.description = description;
		this.category = category;
		this.batches = batches;
	}

	public Product(int id, String name, double price, String image, String description, Category category, List<Image> images, List<Batch> batches) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.image = image;
		this.description = description;
		this.category = category;
		this.images = images;
		this.batches = batches;
	}

	public Product(int id, String name, double price, String image, String description, Category category,  List<Batch> batches) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.image = image;
		this.description = description;
		this.category = category;
		this.batches = batches;
	}

	public Product(int id, String name, double price, String image, List<Batch> batches) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.image = image;
		this.batches = batches;
	}

	public Product(int id, String name, double price, double weight, String image, String description, Category category, List<Batch> batches, int viewCount) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.weight = weight;
		this.image = image;
		this.description = description;
		this.category = category;
		this.batches = batches;
		this.viewCount = viewCount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public List<Batch> getBatches() {
		return batches;
	}

	public void setBatches(List<Batch> batches) {
		this.batches = batches;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		DecimalFormat decimalFormat = new DecimalFormat("#,##0");
		decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US)); // Sử dụng dấu chấm (.) làm dấu phân cách

		StringBuilder builder = new StringBuilder();
		builder.append("Product{")
				.append("id=").append(id)
				.append(", name='").append(name).append('\'')
				.append(", price=").append(decimalFormat.format(price))
				.append(", weight=").append(weight).append('\'')
				.append(", image='").append(image).append('\'')
				.append(", description='").append(description).append('\'')
				.append(", category=").append(category)
				.append(", images=").append(images)
				.append(", batches=[");

		if (batches != null && !batches.isEmpty()) {
			for (Batch batch : batches) {
				builder.append("Batch{id=").append(batch.getId())
						.append(", name='").append(batch.getName()).append('\'');

				if (batch.getManufacturingDate() != null) {
					builder.append(", manufacturingDate=").append(dateFormat.format(batch.getManufacturingDate()));
				} else {
					builder.append(", manufacturingDate=null");
				}

				if (batch.getExpiryDate() != null) {
					builder.append(", expiryDate=").append(dateFormat.format(batch.getExpiryDate()));
				} else {
					builder.append(", expiryDate=null");
				}

				if (batch.getDateOfImporting() != null) {
					builder.append(", dateOfImporting=").append(dateFormat.format(batch.getDateOfImporting()));
				} else {
					builder.append(", dateOfImporting=null");
				}

				builder.append(", quantity=").append(batch.getQuantity())
						.append(", currentQuantity=").append(batch.getCurrentQuantity())
						.append(", priceImport=").append(decimalFormat.format(batch.getPriceImport())) // Định dạng giá nhập
						.append(", adminCreate=").append(batch.getAdminCreate())
						.append(", provider=").append(batch.getProvider())
						.append('}');
			}
		}
		builder.append("]}")
				.append(", viewCount=").append(viewCount);

		return builder.toString();
	}

}