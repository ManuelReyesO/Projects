$(document).ready(function () {
    $('.buy').on('click', function () {
        $('.bottom').addClass("clicked");
    });

    $('.remove').on('click', function () {
        $('.bottom').removeClass("clicked");
    });
});