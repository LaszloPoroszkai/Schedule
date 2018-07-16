function sendDeleteRequest(eventid, columnid) {
    let eventRequestUrl = baseUrl + "events?id=" + eventid + "&columnid=" + columnid;
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onUpdated);
    xhr.open('DELETE', eventRequestUrl);
    xhr.send();
}

function onUpdated() {
    const respStatus = this.status;
    const response = JSON.parse(this.responseText);
    if (respStatus == 302) {
        viewColumnsElement(response[0].id, document.getElementById('main-title').textContent);
        document.getElementById('event-update').style.display = 'none';
    }else{
        alert(response.message);
    }
}

function sendUpdateRequest(eventid) {
    const taskid = document.getElementById('upd-task-select').value;
    const columnid = document.getElementById('upd-column-select').value;
    const start = document.getElementById('upd-start-select').value;
    const end = document.getElementById('upd-end-select').value;
    const description = document.getElementById('description').value;

    let eventRequestUrl = baseUrl + "events?id=" + eventid + "&taskid=" + taskid + "&columnid=" + columnid + "&start=" +  start + "&end=" + end + "&desc=" + description;
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onUpdated);
    xhr.open('PUT', eventRequestUrl);
    xhr.send();
}

function setDefaultColumn(columnid) {
    const arr = JSON.parse(this.responseText);
    const dropEl = document.getElementById('upd-column-select');

    while (dropEl.firstChild) {
           dropEl.removeChild(dropEl.firstChild);
    }

    for (let i = 0;i<arr.length;i++) {
        var opt = document.createElement('option');
        if(arr[0].title){
            opt.label = arr[i].title;
            opt.value = arr[i].id;
            dropEl.appendChild(opt);
        }else{
            opt.label = "N/A"
            opt.value = "N/A";
        }
    }
    document.getElementById('upd-column-select').value = columnid;
}

function populateColumns(response) {
    let columnRequestUrl = baseUrl + "columns?columnId=" + response.columnid;
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', function(){setDefaultColumn.call(this, response.columnid);});
    xhr.open('GET', columnRequestUrl);
    xhr.send();
}

function setDefaultTask(taskid) {
    const arr = JSON.parse(this.responseText);
    const dropEl = document.getElementById('upd-task-select');

    while (dropEl.firstChild) {
           dropEl.removeChild(dropEl.firstChild);
    }

    for (let i = 0;i<arr.length;i++) {
        var opt = document.createElement('option');
        if(arr[0].title){
            opt.label = arr[i].title;
            opt.value = arr[i].id;
            dropEl.appendChild(opt);
        }else{
            opt.label = "N/A"
            opt.value = "N/A";
        }
    }

    document.getElementById('upd-task-select').value = taskid;
}

function populateTasks(response) {
    const xhr = new XMLHttpRequest();
    const user = getAuthorization();
    const userId = user.id;

    xhr.addEventListener('load', function(){setDefaultTask.call(this, response.taskid);});
    xhr.open('GET', BASE_URL + 'tasks?userid=' + userId);
    xhr.send();
}

function onEventReceived() {
    const response = JSON.parse(this.responseText);

    document.getElementById('upd-start-select').value = response.starttime;
    document.getElementById('upd-end-select').value = response.endtime;
    document.getElementById('description').value = response.description;
    document.getElementById('updEvent').onclick = function(){(sendUpdateRequest(response.id));};
    document.getElementById('deleteEvent').onclick = function(){(sendDeleteRequest(response.id, response.columnid));};

    populateTasks(response);
    populateColumns(response);
}

function getEventDetails(eventid) {
    let eventRequestUrl = baseUrl + "events?id=" + eventid;
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onEventReceived);
    xhr.open('GET', eventRequestUrl);
    xhr.send();
}

function updateEvent(eventid) {
    const updModal = document.getElementById('event-update');
    const close = document.getElementById('close-sch');

    updModal.style.display='block';

    close.addEventListener('click', function() {updModal.style.display = "none";}),

    getEventDetails(eventid);
}

function onAddSuccess() {
    const scheduleRespStatus = this.status;
    const response = JSON.parse(this.responseText);
    if(scheduleRespStatus == 302) {
        const schid = response[0].id;
        const title = response[0].name;
        viewColumnsElement(schid, title);
    }else{
        alert("An error has happened: " + response.message);
    }
}

function newEvent() {
    const taskid = document.getElementById('task-select').value;
    const columnid = document.getElementById('column-select').value;
    const starttime = document.getElementById('start-select').value;
    const endtime = document.getElementById('end-select').value;
    const desc = document.getElementById('desc').value

    let eventRequestUrl = baseUrl + "events?taskid=" + taskid + "&columnid=" + columnid + "&starttime=" + starttime + "&endtime=" + endtime + "&desc=" + desc;
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onAddSuccess);
    xhr.open('POST', eventRequestUrl);
    xhr.send();
}

function taskToSchedule(placeholder, eventid) {
    const response = JSON.parse(this.responseText);
    placeholder.textContent = placeholder.textContent + ": " + response[0].title;
    placeholder.addEventListener('click', function(){updateEvent(eventid);});
    placeholder.style.color = '#8B0000';
    placeholder.style.fontWeight = 'bold';
}

function addEvents(response) {
    for (let i = 0; i < response.length; i++) {
        const columnid = response[i].columnid;
        for (let j = response[i].starttime; j < response[i].endtime; j++) {
            const placeholder = document.getElementById(columnid + "-" + j);
            getTaskTitle(placeholder, response[i].taskid, response[i].id);
        }
    }
}

function onEventsReceived() {
    const scheduleRespStatus = this.status;
    if (scheduleRespStatus == 302){
        const response = JSON.parse(this.responseText);
        addEvents(response);
    }else{
        alert("An unknown error happened, http status code: " + scheduleRespStatus);
    }
}

function populateEvents(schid) {
    let eventRequestUrl = baseUrl + "events?schid=" + schid;
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onEventsReceived);
    xhr.open('GET', eventRequestUrl);
    xhr.send();
}