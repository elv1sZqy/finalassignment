function getDynasty() {
    var dynasty = $("#dynasty").html();
    if (dynasty.length == 0 || dynasty == "全部") {
        dynasty = null;
    }
    return dynasty;
}

// 查询
function goSearch() {
    var searchInput = $("#searchInput").val();
    var dynasty = getDynasty();
    $.ajax({
        url: "/search",
        type: "GET",
        data: {"searchInput": searchInput, "dynasty": dynasty},
        success: function (data) {
            if (data.length == 0) {
                alert("查询结果为空!换个条件试试☺");
                return 0;
            }
            if (data != null) {
                // 先将之前的内容清空
                $("#poems").empty();
                for (poem of data) {
                    // 将内容按照","切割
                    let content = poem.content.split(/[。！？)]/);
                    // 拼接
                    var str = "";
                    for (let i = 0; i <content.length; i++) {
                        item = content[i];
                        console.log(i);
                        if (i > 4){
                            str += "<li class=\"d-flex flex-row align-items-center justify-content-center\">\n" +
                                "                                            <span>............</span></li>"
                            break;
                        }
                        str += "<li class=\"d-flex flex-row align-items-center justify-content-center\">\n" +
                            "                                            <span>" + item + "</span></li>"
                    }
                    $("#poems").append("<div class=\"pricing_item text-center magic_fade_in\">\n" +
                        "                            <div class=\"pricing_title\">" + poem.poemName + "</div>\n" +
                        "                            <div class=\"pricing_content\">\n" +
                        "                                <div class=\"package_price\">" + poem.poetName + "<span>/" + poem.dynasty + "</span></div>\n" +
                        "                                <div class=\"pricing_list\">\n" +
                        "                                    <ul>\n" +
                        str +
                        "                                    </ul>\n" +
                        "                                </div>\n" +
                        "                                <div class=\"pricing_button ml-auto mr-auto\"><a href=\"info\\" + poem.id+  "\">查看详情</a></div>\n" +
                        "                            </div>\n" +
                        "                        </div>")
                }

                // 展示搜索结果
                $("#result").attr("style", "display:block;");
                // 滚动到搜索结果的部分
                $('html,body').animate({scrollTop: 740}, 'slow');
            }
        }
    });
}


function reminder() {
    var searchInput = $("#searchInput").val();
    var dynasty = getDynasty();
    $("#reminder").empty();
    if (searchInput != null && searchInput.trim() != "") {
        $.ajax({
            url: "/reminder",
            type: "GET",
            data: {"searchInput": searchInput, "dynasty": dynasty},
            success: function (data) {
                if (data != null) {
                    for (poem of data) {
                        $("#reminder").append("<details>\n" +
                            "    <summary>" + poem.poemName + "         /" + poem.dynasty + "-" + poem.poetName + "</summary>\n" +
                            "    <div class=\"details-wrapper\">\n" +
                            "        <div class=\"details-styling\">\n" +
                            poem.content +
                            "    </div>\n" +
                            "    </div>\n" +
                            "    </details>")
                    }
                }
            }
        });
    }
}


function addHistory(poem) {
    var history = document.getElementById("createHistory1");
    if (history.childElementCount < 3) {
        return addPoem(poem, history);
    }
    var history2 = document.getElementById("createHistory2");
    if (history2.childElementCount < 3) {
        return addPoem(poem, history2);
    }
    // 两个都重置
    reset(history, history2);
    return addPoem(poem, history);
}

function createPoem() {
    $.ajax({
        //几个参数需要注意一下
        type: "GET",//方法类型
        dataType: "json",//预期服务器返回的数据类型
        url: "/newPoem",//url
        data: $('#form1').serialize(),
        complete: function (result) {
            if (result.status == 200) {
                let poem = result.responseText;
                let isLike = confirm(poem+"\n点击 \"取消\" 再生成一首");
                if (!isLike) {
                    createPoem();
                    return;
                }
                addHistory(poem);
            }
            ;
        }

    });
}
function addPoem(poem, history) {
    let split = poem.split(/[。！？)]/);
    var div = document.createElement("div");
    div.setAttribute("class", "footer_list_container");

    div.innerHTML = "  <ul class=\"footer_list\">\n" +
        "                                    <li>" + split[0] + "</li>\n" +
        "                                    <li>" + split[1] + "</li>\n" +
        "                                    <li>" + split[2] + "</li>\n" +
        "                                    <li>" + split[3] + "</li>\n" +
        "                                </ul>";
    history.appendChild(div);
    return;
}

function reset(history, history2) {
    history.innerHTML = "<div class=\"footer_list_title\">生成历史</div>";
    history2.innerHTML = "<div class=\"footer_list_title\">&nbsp;&nbsp;&nbsp;</div>";
}




