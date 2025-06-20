document.addEventListener('DOMContentLoaded', function () {
    const showAllReviewsBtn = document.getElementById('showAllReviewsBtn');
    const reviewItems = document.querySelectorAll('.review-item');
    const filterContainer = document.getElementById('filterContainer');
    const filterButtons = document.querySelectorAll('.filter-btn');
    const sortOptions = document.getElementById('sort-options');

    showAllReviewsBtn.addEventListener('click', function () {
        // Hiển thị tất cả các đánh giá
        reviewItems.forEach(function (item) {
            item.classList.remove('hidden');
        });
        // Hiển thị bộ lọc
        filterContainer.style.display = 'flex';
        // Ẩn nút "Xem tất cả đánh giá" sau khi đã hiển thị tất cả
        showAllReviewsBtn.style.display = 'none';
        // Đặt nút "Tất cả" thành active
        setActiveButton('all');
    });



    $('.filter-btn').on('click', function () {
        var rating = $(this).data('rating');
        var sort = $('#sort-options').val();

        // Đặt nút hiện tại thành active
        setActiveButton(rating);

        // Gọi hàm filterReviews với rating và sort mới
        filterReviews(rating, sort);
    });

    $('#sort-options').on('change', function () {
        var rating = $('.filter-btn.active').data('rating') || 'all';
        var sort = $(this).val();

        // Gọi hàm filterReviews với rating và sort mới
        filterReviews(rating, sort);
    });

    function setActiveButton(rating) {
        // Duyệt qua từng button filter và thêm/xóa class active tùy vào rating
        filterButtons.forEach(function (button) {
            if ($(button).data('rating') === rating.toString()) {
                $(button).addClass('active');
            } else {
                $(button).removeClass('active');
            }
        });
    }

    function filterReviews(rating, sort) {
        $.ajax({
            url: 'ReviewFilterControll',
            method: 'GET',
            data: {
                rating: rating,
                sort: sort
            },
            success: function (response) {
                var reviewsHtml = '';
                response.forEach(function (review) {
                    console.log(review);
                    reviewsHtml += `
                    <div class="review-item">
                        <h3><b>${review.nameCommenter}</b>`;
                    for (let i = 1; i <= review.rating; i++) {
                        reviewsHtml += `<i class="fa fa-star stars selected"></i>`;
                    }
                    for (let i = review.rating + 1; i <= 5; i++) {
                        reviewsHtml += `<i class="fa fa-star stars"></i>`;
                    }
                    reviewsHtml += `</h3>`;
                    if (review.image) {
                        reviewsHtml += `<br><img style="width: 100px;height: 100px;border-radius: 6px" src="${review.image}">`;
                    }
                    reviewsHtml += `
                    <div class="cmt" style="background-color: #fff5e3; margin-top: 15px;padding: 10px; width:60%; border-radius: 5px">
                        <h3>${review.comment ? review.comment : 'No comment provided'}</h3>
                    </div>
                    <p>${review.dateCreated}</p>`;
                    if (review.reply) {
                        reviewsHtml += `
                    <span><img src="/images/tick.png"></span><span style="font-size: 16px"><b>Golden Fields</b></span>
                    <span>Đã trả lời</span>
                    <div class="reply">
                        <p>${review.reply}</p>
                        <p class="reply-date">${review.dateReply}</p>
                    </div>`;
                    }
                    reviewsHtml += `</div>`;
                });
                $('#review-list').html(reviewsHtml);
            },
            error: function (err) {
                console.error(err);
            }
        });
    }
});