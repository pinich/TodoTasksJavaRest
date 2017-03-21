var user_d = null; //detailes of logged in user
var postid; //stores id of current open post
var user_post = null;

//this function is take care when the browser is load at the beginning
$(document).ready(function () {
    fillTodoList();
});



// Gets the homePage template, puts it in document, then fills it with add post template and posts list
function fillTodoList() {
    $.ajax({
        url: "templates/listELM.html",
        type: "GET"
    })
            .done(function (html) {
                getTodosData(html);
            })
            .fail(function (xhr, status, errorThrown) {
                console.log("Error: Unable to load postsList");
            });
}

function getTodosData(template) {
    $.ajax({
        url: "api/todo",
        type: "GET",
        dataType: 'json',
        contentType: 'application/json',
    })
            .done(function (data) {
                data = data["data"];
                var todoList = $(".list-group");
                todoList.html('');
                for (var i = 0; i < data.length; i++) {
                    let todo = data[i];
                    let tmp = $(template);
                    tmp.find(".todoMessage").text(todo["message"]);
                    tmp.find(".todoEditBtn").attr("onclick", "editTodoStart('" + todo.id + "');return false;");
                    tmp.find(".todoDeleteBtn").attr("onclick", "deleteTodo('" + todo.id + "');return false;");
                    tmp.attr("id", "todo_" + todo.id);
                    tmp.attr("href", "javascript:todoDone('" + todo.id + "');")
                    if (todo["status"] == 1) {
                        tmp.addClass("list-group-item-success");
                    }
                    todoList.append(tmp); //append filled post to the postslist element
                }
            })
            .fail(function (xhr, status, errorThrown) {
                console.log("Error: Unable to load postsList");
            });
}

function editTodoStart(id) {
    var todoELM = $("#todo_" + id);
    console.log("Editing:" + id);
    //EDIT
    var switchToInput = function () {
        var $input = $("<input>", {
            val: $(this).text(),
            type: "text",
            rel: jQuery(this).text(),
        });
        $input.addClass("loadNum");
        $(this).replaceWith($input);
        $input.on("blur", switchToSpan);
        $input.select();
    };
    var switchToSpan = function () {
        if (jQuery(this).val()) {
            var $text = jQuery(this).val();
        } else {
            var $text = jQuery(this).attr('rel');
        }
        var $span = $("<span>", {
            text: $text,
        });
        $span.addClass("loadNum");
        $(this).replaceWith($span);
        $span.on("click", switchToInput);
    }


}

function deleteTodo(id) {
    console.log("Delete:" + id);
    var todoELM = $("#todo_" + id);
    $.ajax({
        url: "api/todo",
        type: "DELETE",
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({"id": id})
    })
            .done(function (html) {
                todoELM.remove();
            })
            .fail(function (xhr, status, errorThrown) {
                console.log("Error: Unable to load postsList");
            });
}

function addTodo() {
    let dataToSend = {
        "message": $("#txtInput").val(),
        "status": 0,
        "other": ""
    }
    $.ajax({
        url: "api/todo",
        type: "POST",
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify(dataToSend)
    })
            .done(function (html) {
                fillTodoList(html);
            })
            .fail(function (xhr, status, errorThrown) {
                console.log("Error: Unable to load postsList");
            });
}

function todoDone(id) {
    console.log("Todo Done" + id);
    var todoELM = $("#todo_" + id);

    var taskStatus = -1;
    if (todoELM.hasClass("list-group-item-success")) {
        taskStatus = 0;
    } else {
        taskStatus = 1;
    }

    var dataToSend = {
        "id": id,
        "message": todoELM.find(".todoMessage").text(),
        "status": taskStatus,
        "other": ""
    }
    $.ajax({
        url: "api/todo",
        type: "PUT",
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify(dataToSend)
    })
            .done(function (html) {
                todoELM.toggleClass("list-group-item-success");
            })
            .fail(function (xhr, status, errorThrown) {
                console.log("Error: Unable to load postsList");
            });
}