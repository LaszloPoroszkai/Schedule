function newColumn() {
    const schid = document.getElementById('newColumn').name;
    const name = document.getElementById('column-name').value;
    const title = document.getElementById('main-title').textContent;
    let eventRequestUrl = baseUrl + "columns?schid=" + schid + "&title=" + name;
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', function(){onColumnsReceived.call(this, title, schid);});
    xhr.open('POST', eventRequestUrl);
    xhr.send();
}

function showColumns(response, title) {
    updateView('showschedule');
    const scheduleDivEl = document.getElementById('schedule');

    const tableEl = document.createElement('table');
    const mainTheadEl = document.createElement('thead');
    const columnTheadEl = document.createElement('thead');
    const trEl = document.createElement('tr');
    const columnTitleEl = document.createElement('tr');
    const titleTdEl = document.createElement('td');
    titleTdEl.setAttribute('id', 'main-title');
    titleTdEl.colSpan=response.length;
    titleTdEl.textContent = title;
    titleTdEl.contentEditable="true";
    titleTdEl.addEventListener("keydown", function(event){if (event.keyCode == 13){event.preventDefault(); updateTitle(titleTdEl.textContent, title); return false}})
    tableEl.appendChild(mainTheadEl.appendChild(trEl.appendChild(titleTdEl)));
    tableEl.setAttribute('class', 'column');
    if (response.length >= 7) {
        document.getElementById('new-column').style.display='none';
    }else {
        document.getElementById('new-column').style.display='block';
    }

    if(response[0].title!=null) {
        for (let i = 0; i < response.length; i++){
            const nameTdEl = document.createElement('td');
            nameTdEl.textContent=response[i].title;
            nameTdEl.contentEditable="true";
            columnTitleEl.appendChild(nameTdEl);
        }

        tableEl.appendChild(columnTitleEl);
        const tbodyEl = document.createElement('tbody');

        for (let j = 0; j < 24; j++) {
            const trEl = document.createElement('tr');
            for (let i = 0; i < response.length; i++) {
                const placeholderEl = document.createElement('td');
                m = j+1;
                placeholderEl.textContent = j + " - " + m;
                placeholderEl.setAttribute('id', response[i].id + "-" + j);
                placeholderEl.setAttribute('class', 'placeholder');
                trEl.appendChild(placeholderEl);
            }
            tbodyEl.appendChild(trEl);
        }

        tableEl.appendChild(tbodyEl);
        if (response.length > 0) {
            populateEvents(response[0].schid);
        }
    }
    scheduleDivEl.appendChild(tableEl);
}

function populateDrop(arr, selectName) {
    const dropEl = document.getElementById(selectName);

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
}

function populateDropDowns(response) {
    populateDrop(response, 'column-select');
    onLoadTasks(sendResults);
}

function onColumnsReceived(title, schid) {
    const scheduleRespStatus = this.status;
    scheduleDivEl = document.getElementById('schedule');
    const addEvent = document.getElementById('sendtask');
    const addColumn = document.getElementById('newColumn');
    while (scheduleDivEl.firstChild) {
           scheduleDivEl.removeChild(scheduleDivEl.firstChild);
    }
    const response = JSON.parse(this.responseText);
    if (scheduleRespStatus == 302){
        addEvent.addEventListener('click',newEvent);
        addColumn.addEventListener('click',newColumn);
        addColumn.name = schid;
        populateDropDowns(response);
        showColumns(response, title);
    }else {
        alert("An unknown error happened, http status code: " + response.message);
    }
}

function viewColumnsElement(scheduleId, title) {
    const userId = getAuthorization();

    let columnRequestUrl = baseUrl + "columns?schId=" + scheduleId;
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', function(){onColumnsReceived.call(this, title, scheduleId);});
    xhr.open('GET', columnRequestUrl);
    xhr.send();
}