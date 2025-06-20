<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ page isELIgnored="false" %>

<html>
<nav class="sidebar sidebar-offcanvas" id="sidebar">
    <div class="sidebar-brand-wrapper d-none d-lg-flex align-items-center justify-content-center fixed-top">

        <a class="sidebar-brand brand-logo-mini" href="IndexAdminControll"><img src="assetsAdmin/images/logo-mini.svg"
                                                                                alt="logo"/></a>
    </div>
    <c:set scope="session" var="acc" value="${sessionScope.account}"></c:set>
    <ul class="nav">
        <li class="nav-item nav-category">
            <span class="nav-link">MENU ADMIN</span>
        </li>
        <c:if test="${acc.role.id==1}">
        <li class="nav-item menu-items">
            <a class="nav-link" href="IndexAdminControll">
              <span class="menu-icon">
                <i class="mdi mdi-speedometer"></i>
              </span>
                <span class="menu-title">Dashboard</span>
            </a>
        </li>
        </c:if>
        <c:if test="${acc.role.id == 2 or acc.role.id == 1}">
        <li class="nav-item menu-items">
            <a class="nav-link" href="./LoadUserPage">
              <span class="menu-icon">
                <i class="mdi mdi-account"></i>
              </span>
                <span class="menu-title">Quản lý người dùng</span>
            </a>
        </li>
        </c:if>

        <c:if test="${acc.role.id == 4 or acc.role.id == 1}">
        <li class="nav-item menu-items">
            <a class="nav-link" data-toggle="collapse" href="#ui-basic" aria-expanded="false" aria-controls="ui-basic">
              <span class="menu-icon">
                <i class="mdi mdi-laptop"></i>
              </span>
                <span class="menu-title">Kho</span>
                <i class="menu-arrow"></i>
            </a>
            <div class="collapse" id="ui-basic">
                <ul class="nav flex-column sub-menu">
                    <li class="nav-item"><a class="nav-link" href="./LoadProductsPage">Danh sách sản phẩm</a></li>
                    <li class="nav-item"><a class="nav-link" href="./LoadInventoryProductPage">Tồn kho</a></li>
                    <li class="nav-item"><a class="nav-link" href="./LoadExpiredProductPage">Sản phẩm hết hạn</a></li>
                </ul>
            </div>
        </li>
        <li class="nav-item menu-items">
            <a class="nav-link" data-toggle="collapse" href="#ui-basic-2" aria-expanded="false"
               aria-controls="ui-basic-2">
            <span class="menu-icon">
                <i class="mdi mdi-folder"></i>
            </span>
                <span class="menu-title">Nhập kho</span>
                <i class="menu-arrow"></i>
            </a>
            <div class="collapse" id="ui-basic-2">
                <ul class="nav flex-column sub-menu">
                    <li class="nav-item">
                        <a class="nav-link" href="./AddProductControll">Sản phẩm mới</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="./AddBatchControll">Đã có trong kho</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="./LoadBatch">Thống kê nhập kho</a>
                    </li>
                </ul>
            </div>
        </li>
        </c:if>

        <c:if test="${acc.role.id == 3 or acc.role.id == 1}">
        <li class="nav-item menu-items">
            <a class="nav-link" href="./LoadBillControll">
              <span class="menu-icon">
                <i class="mdi mdi-book"></i>
              </span>
                <span class="menu-title">Quản lý đơn hàng</span>
            </a>
        </li>
        </c:if>

        <c:if test="${acc.role.id == 1}">
        <li class="nav-item menu-items">
            <a class="nav-link" href="./LoadLogPageControll">
              <span class="menu-icon">
                <i class="mdi mdi-security"></i>
              </span>
                <span class="menu-title">Quản lý log</span>
            </a>
        </li>


        <li class="nav-item menu-items">
            <a class="nav-link" href="./LoadMangeVoucherControll">
              <span class="menu-icon">
                <i class="mdi mdi-sale"></i>
              </span>
                <span class="menu-title">Quản lý vouncher</span>
            </a>
        </li>
        <li class="nav-item menu-items">
            <a class="nav-link" href="./LoadManageReviewControll">
              <span class="menu-icon">
                <i class="mdi mdi-comment"></i>
              </span>
                <span class="menu-title">Quản lý review</span>
            </a>
        </li>
        </c:if>
    </ul>
</nav>
</html>