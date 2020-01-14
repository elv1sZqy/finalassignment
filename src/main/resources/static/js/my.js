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
            if (data != null) {
                // 先将之前的内容清空
                $("#poems").empty();
                for (poem of data) {
                    // 将内容按照","切割
                    let content = poem.content.split(/[。！？)]/);
                    // 拼接
                    var str = "";
                    for (item of content) {
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
                        "                                <div class=\"pricing_button ml-auto mr-auto\"><a href=\"#\">Buy Now</a></div>\n" +
                        "                            </div>\n" +
                        "                        </div>")
                }

                // 展示搜索结果
                $("#result").attr("style", "display:block;")
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
                            "    <summary>" + poem.poemName + "         /"+poem.dynasty+"-"+poem.poetName+"</summary>\n" +
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



