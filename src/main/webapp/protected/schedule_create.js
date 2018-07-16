let hasCreateSchedEventListener = false;

function onSchedulesReceived() {
    showschedDivEl = document.getElementById('schedules');
    clearErrorMessages('schedules');

    while (showschedDivEl.firstChild) {
            showschedDivEl.removeChild(showschedDivEl.firstChild);
    }

    if(this.status == 200){
        const text = this.responseText;
        const scheds = JSON.parse(text);
        makeResponseContent(scheds);
    }
    else {
        alert("internal server error");
    }
}


function onNewSched(toPostUserId) {
    console.log("user id in the onNEwSched " + toPostUserId);
    const name = document.forms['new-schedule-form'].elements['name'].value;

    const params = new URLSearchParams();

    params.append('id', toPostUserId);
    params.append('name', name);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onSchedulesReceived);
    xhr.open('POST', BASE_URL + 'schedule');
    xhr.send(params);
}


function onCreateScheds(tempUserId) {
    console.log("user id in the createScheds " + tempUserId);
    const createSchedDivEl = document.getElementById('newschedule');
    clearErrorMessages('newschedule');

    const buttonEl = document.getElementById('createSched');
    if(!hasCreateSchedEventListener){
        buttonEl.addEventListener('click', function(){onNewSched(tempUserId);});
        hasCreateSchedEventListener = true;
    }
}