$(function () {
    $.datetimepicker.setDateFormatter('moment');
    // $.datetimepicker.setDateFormatter({
    //     parseDate: function (date, format) {
    //         var d = moment(date, format);
    //         return d.isValid() ? d.toDate() : false;
    //     },
    //     formatDate: function (date, format) {
    //         return moment(date).format(format);
    //     }
    // });

    $('#startDate').datetimepicker({
        format: "YYYY-MM-DD",
        timepicker: false
    });
    $('#endDate').datetimepicker({
        format: "YYYY-MM-DD",
        timepicker: false
    });
    $('#startTime').datetimepicker({
        format: "HH:m",
        datepicker: false,
        step: 30
    });
    $('#endTime').datetimepicker({
        format: "HH:mm",
        datepicker: false,
        step: 30
    });
    $('#dateTime').datetimepicker({
        format: "YYYY-MM-DD HH:mm",
        step: 30
    });
});