const ajaxUrl = "ajax/meals/";
let datatableApi;

$(function () {
    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "dateTime"
            },
            {
                "data": "description"
            },
            {
                "data": "calories"
            },
            {
                "defaultContent": "Edit",
                "orderable": false
            },
            {
                "defaultContent": "Delete",
                "orderable": false
            }
        ],
        "order": [
            [
                0,
                "desc"
            ]
        ]
    });
    makeEditable();
});

function resetFilter() {
    document.getElementById("filterForm").reset();
    updateTable();
}

function addMeal() {
    add();
    let iso_date = convertToISODateTime(new Date());
    $("#dateTime").val(iso_date);
    $("#description").val("Прием пищи");
    $("#calories").val(1000);
}

function updateTable() {
    let isEmpty = true;
    let form = $("#filterForm");
    form.find(":input").each(function () {
        if ($.trim($(this).val()) !== '') {
            isEmpty = false;
        }
    });

    const targetUrl = (isEmpty) ? ajaxUrl : ajaxUrl + "filter/";
    const dataBody = (isEmpty) ? {} : form.serialize();
    $.ajax({
        type: "GET",
        url: targetUrl,
        data: dataBody
    }).done(function (data) {
        applyDataToTable(data);
    });
}