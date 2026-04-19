$(function () {

    // ---------------------- Materialize ----------------------

    $('.sidenav').sidenav();            // 侧边栏导航（移动设备可滑动展开/收起）
    $(".dropdown-trigger").dropdown();  // 下拉菜单

    // ---------------------- slideToTop ----------------------

    // slideToTop start
    var slideToTop = $("<div />");
    slideToTop.html('<i class="material-icons">keyboard_arrow_up</i>');
    slideToTop.css({
        'position': 'fixed',
        'bottom': '20px',
        'right': '20px',
        'width': '40px',
        'height': '40px',
        'color': '#eee',
        'background-color': '#222d32',
        /*'font-size': '24px',*/
        'line-height': '50px',
        'text-align': 'center',
        /*'justify-content': 'center',*/
        'cursor': 'pointer',
        'border-radius': '5px',
        'z-index': '99999',
        'opacity': '.7',
        'display': 'none'
    });
    slideToTop.on('mouseenter', function () {
        $(this).css('opacity', '1');
    });
    slideToTop.on('mouseout', function () {
        $(this).css('opacity', '.7');
    });
    $('body').append(slideToTop);
    $(window).scroll(function () {
        if ($(window).scrollTop() >= 150) {
            if (!$(slideToTop).is(':visible')) {
                $(slideToTop).fadeIn(500);
            }
        } else {
            $(slideToTop).fadeOut(500);
        }
    });
    $(slideToTop).click(function () {
        $("html,body").animate({		// firefox ie not support body, chrome support body. but found that new version chrome not support body too.
            scrollTop: 0
        }, 100);
    });
    // slideToTop end

});