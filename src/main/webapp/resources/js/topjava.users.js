function updateTable() {
    $.get(ctx.ajaxUrl, updateTableWithData);
}

function setEnabled(checkbox, id) {
    var enabled = checkbox.is(":checked");
    $.post(ctx.ajaxUrl + id, "enabled=" + enabled)
        .done(function () {
            checkbox.closest('tr').attr("data-enabled", enabled);
            successNoty("User is " + (enabled ? "enabled" : "disabled"));
        })
        .fail(function () {
            checkbox.prop("checked", !enabled);
        });
}

// $(document).ready(function () {
$(function () {
    // https://stackoverflow.com/a/5064235/548473
    ctx = {
        ajaxUrl: "admin/users/",
        datatableApi: $("#datatable").DataTable({
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
        })
    };
    makeEditable();
});