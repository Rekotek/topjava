const ajaxUrl = "ajax/admin/users/";
let datatableApi;

// $(document).ready(function () {
$(function () {
    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "name"
            },
            {
                "data": "email"
            },
            {
                "data": "roles"
            },
            {
                "data": "enabled"
            },
            {
                "data": "registered"
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
                "asc"
            ]
        ]
    });
    makeEditable();
});

function changeEnableFlag(id, checkBox) {
    const checked = checkBox.is(':checked');
    $.ajax({
        type: "POST",
        url: ajaxUrl + id + '/enabled',
        data: {"flag": checked}
    }).done(function () {
        updateTable();
        const notyStr = (checked) ? "Enabled" : "Disabled";
        successNoty(notyStr);
    }).fail(function () {
        checkBox.prop("checked", !checked);
    });
}