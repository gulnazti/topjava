var filter = $("#filter");

function updateTable() {
    $.get(ctx.ajaxUrl + "filter", filter.serialize())
        .done(updateTableWithData);
}

function clearFilter() {
    filter.find(":input").val("");
    updateTable();
}

$(function () {
    ctx = {
        ajaxUrl: "profile/meals/",
        datatableApi: $("#datatable").DataTable({
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
        })
    };
    makeEditable();
});