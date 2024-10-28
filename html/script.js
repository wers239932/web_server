function checkY(y) {
    if(y!=NaN && Number.isInteger(y) && y>=-5 && y<=3)
        return true;
    return false;
}
function checkX(x) {
    return !Number.isNaN(x);
}

function sendAll() {
    const checkedValuesX = [...document.querySelectorAll('input[type="checkbox"]:checked')].map(checkbox => checkbox.value);
    if(checkedValuesX.length==0) document.getElementById("status").textContent = "вы не выбрали первую координату";
    checkedValuesX.forEach(a=>
        {
            let x=Number.parseInt(a);
            send(x);
            
        }
    )
}

async function send(curx) {
    let x = Number.parseInt(curx);
    let y = Number.parseInt(document.getElementById("y").value);
    let R = Number.parseInt(document.getElementById("R").value);
    let x1=x.toString();
    let y1=y.toString();
    let r1=R.toString();
    
    const formData = {
        X: x1,
        Y: y1,
        R: r1
    };  
    var errorMessage = "";
    if(!checkX(x)) {
        errorMessage += "вы не выбрали первую координату";
    } if(!checkY(y)) {
        if(errorMessage!="") errorMessage+=", и ";
        errorMessage += "вторая координата введена неверно"; 
    }
    document.getElementById("status").textContent = errorMessage;
    if(errorMessage=="") {
        params = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        }
        let url = new URL("http://localhost:8080/fcgi-bin/web_server.jar");
        //console.log(params);
        let timeStart = Date.now();
        let response = await fetch(url, params);
        if(response.ok) {
            write(response, timeStart);
        } else {
            ooops(response, timeStart);
        }

    }
    
}

async function ooops(response, timeStart) {
    document.getElementById("status").textContent = "сервер не смог расшифровать входные данные: "+response.statusText;
}

async function write(response, timeStart) {
    let array = await response.json();
    let timeStop = Date.now();
    let length = timeStop-timeStart;
    var newrow = document.createElement("tr");
    var tdX = document.createElement("td");
    var tdY = document.createElement("td");
    var tdR = document.createElement("td");
    var tdTime = document.createElement("td");
    var tdDuration = document.createElement("td");
    tdX.innerHTML = array.X;
    tdY.innerHTML = array.Y;
    tdR.innerHTML = array.R;
    tdTime.innerHTML = array.time;
    tdDuration.innerHTML = length;
    var val1 = document.getElementById("history");
    val1.appendChild(newrow);
    newrow.appendChild(tdX);
    newrow.appendChild(tdY);
    newrow.appendChild(tdR);
    newrow.appendChild(tdTime);
    newrow.appendChild(tdDuration);

    var pointer = document.getElementById("pointer");
    pointer.setAttribute('visibility', 'visible');
    var cx = 200+150*Number.parseFloat(array.X)/Number.parseFloat(array.R);
    var cy = 200-150*Number.parseFloat(array.Y)/Number.parseFloat(array.R);
    pointer.setAttribute('cx', ''+cx);
    pointer.setAttribute('cy', ''+cy);

    if(array.content=='true') {
        document.getElementById("status").textContent = "вы попали";
    }
    else if(array.content=='false') {
        document.getElementById("status").textContent = "вы промахнулись";
    }
}