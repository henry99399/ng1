$(document).ready(function () {
    var max_h = document.documentElement.clientHeight - 50 || document.body.clientHeight - 50;
    $(".content").scroll(function () {
        console.log("ww")
        if (this.scrollHeight - max_h - this.scrollTop < 200) {
            console.log("xx")
            getBookReview();
        }
    });
    if (total <= 10) {
        _ajax = false;
    }
})
var _ajax = true;
var param = {
    reviewId: review_id,
    bookId: book_id,
    pageNum: 1,
    pageSize: 10,
    total: 0,
    pages: 0
}
function getBookReview() {
    if (_ajax) {
        _ajax = false;
        param.pageNum += 1;
        var comments = $('.comments .main');
        $.jsonAjax("/v2/api/mobile/bookReview/getReview", param, function (res) {
            if (res.code == 0) {
                $.each(res.data.object.rows, function (index, item) {
                    var clone = $('.cloneRow').clone().removeClass("cloneRow").show();
                    clone.attr("href", ctxPath + "/mobile/commentDetail/"+ item.review_id);
                    if (item.icon) {
                        $('.touxiang img', clone).attr(ctxPath + item.icon);
                    }
                    $('.name', clone).html(item.nick_name);
                    $('.time', clone).html(formatDate(item.create_time));
                    $('i span', clone).html(item.review_nums);
                    $('p', clone).html(item.review_content);
                    comments.append(clone);
                })
                param.pages = res.data.object.pages;
                if(param.pageNum >= param.pages){
                    _ajax = false;
                }
                else{
                    _ajax = true;
                }
            }
        })
    }
}

function formatDate(str) {
    var date = new Date(Date.parse(str.replace(/-/g, "/")));
    return (date.getMonth() + 1) + "月" + date.getDate() + "日 " + date.getHours() + ":" + date.getMinutes();
}