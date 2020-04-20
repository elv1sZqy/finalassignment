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
                        "                                <div class=\"pricing_button ml-auto mr-auto\">" +
                        "<a href=\"#popup\">详情</a>" +
                        "</div>\n" +
                        "                            </div>\n" +
                        "                        </div>" +
                        " <div class=\"popup\" id=\"popup\">\n" +
                        "                        <div class=\"popup-inner\">\n" +
                        "                        <div class=\"popup__photo\">\n" +
                        "                        <!--                <img src=\"photo-1515224526905-51c7d77c7bb8.jpg\" alt=\"\">-->\n" +
                        "                        </div>\n" +
                        "                        <div class=\"popup__text\">\n" +
                        "                        <h1>Lorem ipsum dolor sit amet</h1>\n" +
                        "                    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer ex velit, viverra non vulputate\n" +
                        "                    vitae, blandit vitae nisl. Nullam fermentum orci et erat viverra bibendum. Aliquam sed varius nibh,\n" +
                        "                        vitae mattis purus. Mauris elementum sapien non ullamcorper vulputate. Orci varius natoque penatibus\n" +
                        "                    et magnis dis parturient montes, nascetur ridiculus mus. Sed eget felis sit amet eros viverra\n" +
                        "                    pulvinar.</p>\n" +
                        "                    </div>\n" +
                        "                    <a class=\"popup__close\" href=\"#popup1\">X</a>\n" +
                        "                        </div>\n" +
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



