
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html; charset=UTF-8" %><!DOCTYPE html>
<html lang="en">
<head>
<!-- Required meta tags -->
	<%@ page isELIgnored="false" %>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<title>Admin-Dashboard</title>
<!-- plugins:css -->
<jsp:include page="link/link.jsp"></jsp:include>
<style>
</style>
</head>
<body>
	<div class="container-scroller">
		<!-- partial:partials/_sidebar.html -->
		<jsp:include page="header/sidebar.jsp"></jsp:include>

		<!-- partial -->
		<div class="container-fluid page-body-wrapper">
			<!-- partial:partials/_navbar.html -->
			<jsp:include page="header/navbar.jsp"></jsp:include>

			<!-- partial -->
			<div class="main-panel">
				<div class="content-wrapper">

					<div class="row">
						<div class="col-xl-3 col-sm-6 grid-margin stretch-card">
							<div class="card">
								<div class="card-body">
									<div class="row">
										<div class="col-9">
											<div class="d-flex align-items-center align-self-start">
												<h3 class="mb-0">${map.get("sumOrder")}</h3>
												<p class="text-success ml-2 mb-0 font-weight-medium">+3.5%</p>
											</div>
										</div>
										<div class="col-3">
											<div class="icon icon-box-success ">
												<span class="mdi mdi-arrow-top-right icon-item"></span>
											</div>
										</div>
									</div>
									<h4 class="text-muted font-weight-normal">Số đơn hàng </h4>

								</div>
							</div>
						</div>
						<div class="col-xl-3 col-sm-6 grid-margin stretch-card">
							<div class="card">
								<div class="card-body">
									<div class="row">
										<div class="col-9">
											<div class="d-flex align-items-center align-self-start">
												<h3 class="mb-0">${map.get("sumPrice")}</h3>
												<p class="text-success ml-2 mb-0 font-weight-medium">+11%</p>
											</div>
										</div>
										<div class="col-3">
											<div class="icon icon-box-success">
												<span class="mdi mdi-arrow-top-right icon-item"></span>
											</div>
										</div>
									</div>
									<h6 class="text-muted font-weight-normal">Doanh thu</h6>
								</div>
							</div>
						</div>
						<div class="col-xl-3 col-sm-6 grid-margin stretch-card">
							<div class="card">
								<div class="card-body">
									<div class="row">
										<div class="col-9">
											<div class="d-flex align-items-center align-self-start">
												<h3 class="mb-0">$12.34</h3>
												<p class="text-danger ml-2 mb-0 font-weight-medium">-2.4%</p>
											</div>
										</div>
										<div class="col-3">
											<div class="icon icon-box-danger">
												<span class="mdi mdi-arrow-bottom-left icon-item"></span>
											</div>
										</div>
									</div>
									<h6 class="text-muted font-weight-normal">Daily Income</h6>
								</div>
							</div>
						</div>
						<div class="col-xl-3 col-sm-6 grid-margin stretch-card">
							<div class="card">
								<div class="card-body">
									<div class="row">
										<div class="col-9">
											<div class="d-flex align-items-center align-self-start">
												<h3 class="mb-0">$31.53</h3>
												<p class="text-success ml-2 mb-0 font-weight-medium">+3.5%</p>
											</div>
										</div>
										<div class="col-3">
											<div class="icon icon-box-success ">
												<span class="mdi mdi-arrow-top-right icon-item"></span>
											</div>
										</div>
									</div>
									<h6 class="text-muted font-weight-normal">Expense current</h6>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<!-- Chart Section -->
						<div class="col-md-4 grid-margin stretch-card">
							<div class="card">
								<div class="card-body">
									<canvas id="transaction-history" class="transaction-chart"></canvas>
									<h4 class="card-title">Danh mục bán chạy</h4>
									<div class="bg-gray-dark d-flex d-md-block d-xl-flex flex-row py-3 px-4 px-md-3 px-xl-4 rounded mt-3">
										<div class="text-md-center text-xl-left">
											<h6 class="mb-1">Noodle</h6>
											<p class="text-muted mb-0">${map.get("ratio1")}%</p>
										</div>
										<div class="align-self-center flex-grow text-right text-md-center text-xl-right py-md-2 py-xl-0">
											<h6 class="font-weight-bold mb-0">${map.get("sumPriceType1")} VND</h6>
										</div>
									</div>
									<div class="bg-gray-dark d-flex d-md-block d-xl-flex flex-row py-3 px-4 px-md-3 px-xl-4 rounded mt-3">
										<div class="text-md-center text-xl-left">
											<h6 class="mb-1">Rice</h6>
											<p class="text-muted mb-0">${map.get("ratio2")}%</p>
										</div>
										<div class="align-self-center flex-grow text-right text-md-center text-xl-right py-md-2 py-xl-0">
											<h6 class="font-weight-bold mb-0">${map.get("sumPriceType2")} VND</h6>
										</div>
									</div>
									<div class="bg-gray-dark d-flex d-md-block d-xl-flex flex-row py-3 px-4 px-md-3 px-xl-4 rounded mt-3">
										<div class="text-md-center text-xl-left">
											<h6 class="mb-1">Chicken</h6>
											<p class="text-muted mb-0">${map.get("ratio3")}%</p>
										</div>
										<div class="align-self-center flex-grow text-right text-md-center text-xl-right py-md-2 py-xl-0">
											<h6 class="font-weight-bold mb-0">${map.get("sumPriceType3")} VND</h6>
										</div>
									</div>
								</div>
							</div>
						</div>

						<!-- Table Section -->
						<div class="col-md-8 grid-margin stretch-card">
							<div class="card">
								<div class="card-body">
									<h4 class="card-title">Top 10 danh mục bán chạy</h4>
									<div class="table-responsive">
										<table class="table table-striped" id="example">
											<thead>
											<tr>
												<th>Mã</th>
												<th>Tên</th>
												<th>Hình ảnh</th>
												<th>Số lượng bán</th>
											</tr>
											</thead>
											<tbody>
											<c:forEach var="p" items="${list}">
											<tr>
												<td>${p.id}</td>
												<td>${p.name}</td>
												<td><img src="${p.image}" style="width: 30px;height: 30px"></td>
												<td>${conlai[p.id]}</td>
											</tr>
											</c:forEach>
											</tbody>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>

				<!-- content-wrapper ends -->
				<!-- partial:partials/_footer.html -->
				<jsp:include page="./footer/footer.jsp"></jsp:include>

				<!-- partial -->
			</div>
			<!-- main-panel ends -->
		</div>
		<!-- page-body-wrapper ends -->
	</div>
</body>
<script type="text/javascript">
const canvas = document.getElementById('transaction-history');
const ctx = canvas.getContext('2d');

const centerX = canvas.width / 2;
const centerY = canvas.height / 2;
const radius = 80; // Độ rộng của hình tròn
const percentages = [10, 40, 50]; // Tỷ lệ phần trăm tương ứng

const colors = ['#ff6384', '#36a2eb', '#ffce56']; // Màu cho các phần trăm
let startAngle = -0.5 * Math.PI; // Bắt đầu từ góc 12h

percentages.forEach((percent, index) => {
  const endAngle = startAngle + 2 * Math.PI * (percent / 100);
  ctx.beginPath();
  ctx.moveTo(centerX, centerY);
  ctx.arc(centerX, centerY, radius, startAngle, endAngle);
  ctx.closePath();
  ctx.fillStyle = colors[index];
  ctx.fill();

  // Hiển thị số liệu ở giữa
  const angle = startAngle + (endAngle - startAngle) / 2;
  const x = centerX + Math.cos(angle) * radius * 0.7; // Điểm hiển thị số liệu cách bán kính 70%
  const y = centerY + Math.sin(angle) * radius * 0.7;
  ctx.font = 'bold 12px Arial';
  ctx.fillStyle = 'black';
  ctx.textBaseline = 'middle';
  ctx.textAlign = 'center';
  ctx.fillText(percent + '%', x, y);

  startAngle = endAngle;
});

</script>

<script src="assetsAdmin/vendors/js/vendor.bundle.base.js"></script>
<!-- endinject -->
<!-- Plugin js for this page -->
<script src="assetsAdmin/vendors/chart.js/Chart.min.js"></script>
<script src="assetsAdmin/vendors/progressbar.js/progressbar.min.js"></script>
<script src="assetsAdmin/vendors/jvectormap/jquery-jvectormap.min.js"></script>
<script src="assetsAdmin/vendors/jvectormap/jquery-jvectormap-world-mill-en.js"></script>
<script src="assetsAdmin/vendors/owl-carousel-2/owl.carousel.min.js"></script>
<!-- End plugin js for this page -->
<!-- inject:js -->
<script src="assetsAdmin/js/off-canvas.js"></script>
<script src="assetsAdmin/js/hoverable-collapse.js"></script>
<script src="assetsAdmin/js/misc.js"></script>
<script src="assetsAdmin/js/settings.js"></script>
<script src="assetsAdmin/js/todolist.js"></script>
<!-- endinject -->
<!-- Custom js for this page -->
<script src="assetsAdmin/js/dashboard.js"></script>
</html>