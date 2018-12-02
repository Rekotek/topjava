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
    debugger;
    defaultCalories = data;
});

function addMeal() {
    // call that strange common function for showing modal form
    add();
    debugger;
    let iso_date = convertToISODateTime( new Date() );
    $("#dateTime").val(iso_date);
    $("#description").val("Прием пищи");
    $("#calories").val(defaultCalories);
}
