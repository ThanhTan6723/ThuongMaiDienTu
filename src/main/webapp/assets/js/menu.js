function scrollCarousel(direction) {
    const carousel = document.querySelector('.product-carousel');
    const scrollAmount = carousel.clientWidth / 4; // Cuộn một lượng bằng với chiều rộng của một sản phẩm

    if (direction === 1) {
        // Cuộn sang phải
        carousel.scrollBy({left: scrollAmount, behavior: 'smooth'});
    } else {
        // Cuộn sang trái
        carousel.scrollBy({left: -scrollAmount, behavior: 'smooth'});
    }
}

document.addEventListener('DOMContentLoaded', function() {
// Lấy tất cả các fieldset
    const fieldsets = document.querySelectorAll('fieldset');

// Lấy nút Lọc
    const filterButton = document.querySelector('input[type="submit"]');
});