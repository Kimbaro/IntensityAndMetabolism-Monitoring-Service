var updateTable = function (carbonenRate_min, carbonenRate_max, heart, device, minimumexe, maximumexe) {
    console.log(heart);
    var container = window.document.getElementById(device + "_table");
    if (heart != 0) {
        if ((heart * 1) >= (carbonenRate_min * 1) && (heart * 1) <= (carbonenRate_max * 1)) { //안정권
            container.style.backgroundColor = '#B7F0B1';
        } else {
            container.style.backgroundColor = '#D8D8D8';
        }
    } else {
        container.style.backgroundColor = '#D8D8D8';
    }

    // document.getElementById(device + "age").innerText = "( " + age + "세 )";
    document.getElementById(device + "carbonenRate").innerText = carbonenRate_min + " ~ " + carbonenRate_max;
    document.getElementById(device + "exercise").innerText = minimumexe + " ~ " + maximumexe;
    document.getElementById(device + "heart").innerText = heart;
    // document.getElementById(device + "maximumHeart").innerText = maximumHeart;
    // document.getElementById(device + "name").innerText = name;
    // document.getElementById(device + "stableHeart").innerText=stableHeart;
    // document.getElementById(device + "id").innerText=id;

}