$(document).ready(function() {
    $("#pass1").on("focusout", function () {
        if ($(this).val() !== $("#pass2").val()) {
            $("#pass2").removeClass("valid").addClass("invalid");
            $(':button[type="submit"]').prop('disabled', true);
        } else {
            $("#pass2").removeClass("invalid").addClass("valid");
            $(':button[type="submit"]').prop('disabled', false);
        }
    });

    $("#pass2").on("keyup", function () {
        if ($("#pass1").val() !== $(this).val()) {
            $(this).removeClass("valid").addClass("invalid");
            $(':button[type="submit"]').prop('disabled', true);
        } else {
            $(this).removeClass("invalid").addClass("valid");
            $(':button[type="submit"]').prop('disabled', false);
        }
    });

});