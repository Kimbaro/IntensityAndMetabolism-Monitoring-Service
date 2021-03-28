var createTable = function (carbonenRate_min, carbonenRate_max, heart, id, minimumexe, maximumexe, name) {
    //테이블 생성 START
    // var people = getParameterByName("people");

    // id에 i값 더하는거는 임시
    var container = window.document.getElementById("wrapper");

    var db_device_name = id;
    // for (var i = 0; i < people; i++) {
    //테이블 담을 div
    var container_item = document.createElement('div');
    //고객 데이터 id
    var db_item_id = document.createElement('span');
    db_item_id.appendChild(document.createTextNode(id));
    db_item_id.setAttribute("display", "none");

    var ca_table = document.createElement('table');
    ca_table.setAttribute("id", db_device_name + "_table");

    // 1행 START
    var ca_tr1 = document.createElement('tr');
    ca_tr1.setAttribute("id", db_device_name + "_head_tr");
    ca_tr1.style.height = "10%";

    var ca_td1 = document.createElement('td');
    ca_td1.setAttribute("id", db_device_name + "_head_td");
    ca_td1.setAttribute("colspan", "3");

    var ca_td1_span_name = document.createElement('span');
    ca_td1_span_name.setAttribute("id", db_device_name + "_head_name");
    var createName = document.createElement("span");
    createName.setAttribute("id", db_device_name + "name");
    createName.innerText = name;
    createName.style.font = "20px bold";
    ca_td1_span_name.appendChild(createName);

    // var ca_td1_span_age = document.createElement('span');
    // ca_td1_span_age.setAttribute("id", db_device_name + "_head_age");
    // var createAge = document.createElement("span");
    // createAge.setAttribute("id", db_device_name + "age");
    // createAge.innerText = "( " + age + "세 )";
    // ca_td1_span_age.appendChild(createAge);

    ca_td1.appendChild(ca_td1_span_name);
    // ca_td1.appendChild(ca_td1_span_age);
    ca_tr1.appendChild(ca_td1);
    // 1행 END

    //2행 START
    var ca_tr2 = document.createElement('tr');
    ca_tr1.setAttribute("id", db_device_name + "_body_tr");

    var ca_td2 = document.createElement('td');
    ca_td2.setAttribute("id", db_device_name + "_body_td");
    ca_td2.setAttribute("colspan", "3");

    var createHeart = document.createElement("span");
    createHeart.setAttribute("id", db_device_name + "heart");
    createHeart.appendChild(document.createTextNode(heart));
    createHeart.style.fontSize = "75px";
    createHeart.innerText = heart;

    ca_td2.appendChild(createHeart);
    ca_tr2.appendChild(ca_td2);
    //2행 END

    //3행 START
    var ca_tr3 = document.createElement('tr');
    ca_tr3.setAttribute("id", db_device_name + "_foot_tr");
    ca_tr3.style.height = "10%";

    var ca_td3_1 = document.createElement('td');
    ca_td3_1.setAttribute("id", db_device_name + "_foot_td_1");

    var ca_td3_span_max_rate = document.createElement('span');
    ca_td3_span_max_rate.setAttribute("id", db_device_name + "exercise");
    ca_td3_span_max_rate.innerText = minimumexe + " ~ " + maximumexe;
    var ca_td3_span_max_rate_icon = document.createElement('img');
    ca_td3_span_max_rate_icon.setAttribute("src", "icon/maximumHeart.svg");
    ca_td3_span_max_rate_icon.style.cssFloat = "left";
    ca_td3_1.appendChild(ca_td3_span_max_rate_icon);
    ca_td3_1.appendChild(ca_td3_span_max_rate);

    var ca_td3_2 = document.createElement('td');
    ca_td3_2.setAttribute("id", db_device_name + "_foot_td_2");
    var ca_td3_span_carbonen_rate = document.createElement('span');
    ca_td3_span_carbonen_rate.setAttribute("id", db_device_name + "carbonenRate");
    ca_td3_span_carbonen_rate.innerText = carbonenRate_min + " ~ " + carbonenRate_max;
    var ca_td3_span_carbonen_rate_icon = document.createElement('img');
    ca_td3_span_carbonen_rate_icon.setAttribute("src", "icon/carbonenRate.svg");
    ca_td3_span_carbonen_rate_icon.style.cssFloat = "left";
    ca_td3_2.appendChild(ca_td3_span_carbonen_rate_icon);
    ca_td3_2.appendChild(ca_td3_span_carbonen_rate);

    ca_td3_1.appendChild(ca_td3_span_max_rate);
    ca_td3_2.appendChild(ca_td3_span_carbonen_rate);

    ca_tr3.appendChild(ca_td3_2);
    ca_tr3.appendChild(ca_td3_1);
    //3행 END

    //주입 START
    ca_table.appendChild(ca_tr1);
    ca_table.appendChild(ca_tr2);
    ca_table.appendChild(ca_tr3);
    //주입 END
    container_item.appendChild(ca_table);
    container.appendChild(container_item);

//    }
}