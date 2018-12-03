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
    $("#filterForm").find(":input").val("");
    updateTable();
}

let defaultCalories;
$.get(ajaxUrl + "defaultcalories", function (data) {
    defaultCalories = data;
});

function addMeal() {
    add();
    let iso_date = convertToISODateTime(new Date());
    $("#dateTime").val(iso_date);
    $("#description").val("Прием пищи");
    $("#calories").val(defaultCalories);
}

function updateTable() {
    let form = $("#filterForm");
    $.ajax({
        type: "GET",
        url: ajaxUrl + "filter/",
        data: form.serialize()
    }).done(function (data) {
        datatableApi.clear().rows.add(data).draw();
    });
}

function applyFilter() {
    let form = $("#filterForm");
    $.ajax({
        type: "GET",
        url: ajaxUrl + "filter/",
        data: form.serialize()
    }).done(function (data) {
        datatableApi.clear().rows.add(data).draw();
    });
}