const baseUrl = "http://localhost:8080/ScheduleMaster3000/protected/";
let scheduleDivEl;

function onUpdateResponse(title) {
    const respStatus = this.status;
    const response = JSON.parse(this.responseText);
}

function updateTitle(newTitle, oldTitle) {
    const userId = getAuthorization().id;
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', function(){onUpdateResponse.call(this, newTitle);});
    xhr.open('PUT', baseUrl + "schedule?title=" + oldTitle + "&newTitle=" + newTitle + "&userId=" + userId);
    xhr.send();
}

function makeRequestUrl(){
    const thisUser = getAuthorization();
    let requestUrl = baseUrl + "schedule?id=" + thisUser.id;
    onViewScheds(requestUrl);
}


function onViewScheds(requestUrl) {
    const viewScheduleDivEl = document.getElementById("schedules");
    viewScheduleDivEl.style.display = 'block';

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onScheduleResponse);
    xhr.open('GET', requestUrl);
    xhr.send();
}

function getAuthorization() {
    return JSON.parse(localStorage.getItem('user'));
}

function onScheduleResponse() {
    const scheduleRespStatus = this.status;
    scheduleDivEl = document.getElementById('schedules');
    while (scheduleDivEl.firstChild) {
           scheduleDivEl.removeChild(scheduleDivEl.firstChild);
    }

    clearErrorMessages('schedules');

    if (scheduleRespStatus == 200){
        const response = JSON.parse(this.responseText);
        makeResponseContent(response);
    }
    else if(scheduleRespStatus == 204){
        onEmptyResponse();
    }
    else {
        alert("An unknown error happened, http status code: " + scheduleRespStatus);
    }
}

function makeResponseContent(response){
    const ulEl = document.createElement('ul');
    scheduleDivEl = document.getElementById('schedules');

    for (let i = 0; i < response.length; i++){
        let pEl = document.createElement('p');
        pEl.classList.add('schedule');

        const liEl = document.createElement('li');

        pEl.textContent = response[i].name;
        onMouseAction(pEl, function(){viewColumnsElement(response[i].id, response[i].name);});

        liEl.appendChild(pEl);

        const deleteButton = document.createElement("button");
        const t = document.createTextNode("Delete");
        deleteButton.appendChild(t);
        deleteButton.id = response[i].id;
        deleteButton.addEventListener("click", function() {deleteSchedule(response[i].id);});
        liEl.appendChild(deleteButton);

        ulEl.appendChild(liEl);
        ulEl.classList.add('link');

    }
    scheduleDivEl.appendChild(ulEl);
}

function onEmptyResponse(){
    let pEl = document.createElement('p');

    scheduleDivEl.style.display = 'block';

    pEl.classList.add('error-messages');
    pEl.textContent = "No schedule added yet";

    scheduleDivEl.appendChild(pEl);
}

function deleteSchedule(scheduleId){
    const xhr = new XMLHttpRequest();
    const user = getAuthorization();
    const userId = user.id;
    xhr.addEventListener('load', onScheduleResponse);
    xhr.open('DELETE', baseUrl + "schedule?scheduleId=" + scheduleId + '&userid=' + userId );
    xhr.send();
}