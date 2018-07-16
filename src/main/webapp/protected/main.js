const BASE_URL = 'http://localhost:8080/ScheduleMaster3000/protected/';
let tempUser;

let prevViewDivEl;
let currViewDivEl;

function onMouseAction(pEl, callback) {
    pEl.addEventListener("click", callback);
    pEl.addEventListener("mouseover", function(){pEl.style.color="white"}, false);
    pEl.addEventListener("mouseout", function(){pEl.style.color="black"}, false);
}

function getAuthorization() {
    return JSON.parse(localStorage.getItem('user'));
}

function updateView(div) {
    if (currViewDivEl != null) {
        prevViewDivEl = currViewDivEl;
        prevViewDivEl.style.display='none';
    }
    currViewDivEl = document.getElementById(div);
    currViewDivEl.style.display='block';
}

function clearErrorMessages(div) {
    updateView(div)
}

function setUpOptionsContent(){
    const viewTasksLinkEl = document.getElementById('view-task');
    const newSchedDivEl = document.getElementById('add-schedules');
    const newTaskDivEl = document.getElementById('add-task');
    const viewSchedulesLinkEl = document.getElementById("view-schedules");
    const logoutLinkEl = document.getElementById("logout");
    logoutLinkEl.addEventListener('click', onLogoutButtonClicked);
    viewTasksLinkEl.addEventListener('click', function() {onLoadTasks(onTasksReceived);});
    viewSchedulesLinkEl.addEventListener('click', makeRequestUrl);
    newTaskDivEl.addEventListener('click', onCreateTasks);
    tempUser = getAuthorization();
    console.log("user id in normal case " + tempUser.id);
    newSchedDivEl.addEventListener('click', function(){onCreateScheds(tempUser.id);});
}


function onMainLoaded(){
    setUpOptionsContent();
}

document.addEventListener('DOMContentLoaded', function(){
    onMainLoaded(); setUpAdmin(); });