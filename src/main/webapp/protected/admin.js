function setUpAdmin(){
    checkUserRole();
}

function checkUserRole(){
    const user = getAuthorization();
    const userRole = user.role;

    if(userRole === "ADMIN"){
        getAvailableUsers();
    }
}

function getAvailableUsers(){
    const usersUrl = "http://localhost:8080/ScheduleMaster3000/protected/users";
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load',  fillAdminDropContent);
    xhr.open('GET', usersUrl);
    xhr.send();
}

function fillAdminDropContent(){
    const adminRespStatus = this.status;
    const usersResponse = JSON.parse(this.responseText);
    if(adminRespStatus == 200){
        makeUsersResponseContent(usersResponse);

        const dropdownEl = document.getElementById("admin-drop");
        dropdownEl.style.display = "block";
    }
}

function makeUsersResponseContent(usersResponse){
    const adminContentDivEl = document.getElementById("dropdown-content");

    for(let i = 0; i < usersResponse.length; i++){
        if (usersResponse[i].role != "ADMIN"){
            let a = document.createElement('a');
            let linkText = document.createTextNode(usersResponse[i].name);
            a.appendChild(linkText);
            a.title = usersResponse[i].email;
            a.href = "#";
            a.id = usersResponse[i].id;
            a.onclick = "return false;";
            a.addEventListener('click', function() {showGivenUserContent(usersResponse[i].id, usersResponse[i].name, usersResponse[i].email); clearOldContent();});


            tempUserId = usersResponse[i].id;
            adminContentDivEl.appendChild(a);
        }
    }
}
function addEventListenerWhenNormalLinksCalled(){
    document.getElementById("view-task").addEventListener('click', clearAdminChilds);
    document.getElementById("add-task").addEventListener('click', clearAdminChilds);
    document.getElementById("view-schedules").addEventListener('click', clearAdminChilds);
    document.getElementById("add-schedules").addEventListener('click', clearAdminChilds);
    document.getElementById("add-schedules").addEventListener('click', clearAdminChilds);

}

function clearAdminChilds(){

    const user = getAuthorization();
    const userRole = user.role;

    if(userRole === "ADMIN"){
       const seeAllUserDivEl = document.getElementById('see-all-content');
       while (seeAllUserDivEl.firstChild) {
                  seeAllUserDivEl.removeChild(seeAllUserDivEl.firstChild);
       }
       let scheduleInAdminDivEl = document.getElementById('schedule');
       while (scheduleInAdminDivEl.firstChild) {
           scheduleInAdminDivEl.removeChild(scheduleInAdminDivEl.firstChild);
       }
       let adminBtnDivEl = document.getElementById('admin-button');
       while (adminBtnDivEl.firstChild) {
          adminBtnDivEl.removeChild(adminBtnDivEl.firstChild);
       }
       let showscheduleDivEl = document.getElementById('showschedule');
       let scheduleColumnName = document.getElementsByClassName('column');
       if(showscheduleDivEl != null){
            showscheduleDivEl.style.display = 'none';
       }
    }
}

function showGivenUserContent(id, currentUser, email){
    const seeAllUserDivEl = document.getElementById('see-all-content');
    clearAdminChilds();
    addEventListenerWhenNormalLinksCalled();
    addAdminButtons(id, currentUser);


    seeAllUserDivEl.style.display = 'block';
    let adminPEl = document.createElement('p');
    adminPEl.appendChild(document.createTextNode("Username: " + currentUser + " Email: " + email));
    seeAllUserDivEl.appendChild(adminPEl);

}

function addAdminButtons(uid, currentUser){
    clearAdminChilds();

    const adminButtonDivEl = document.getElementById('admin-button');
    var adminBtn1 = document.createElement("button");
    var adminBtn2 = document.createElement("button");
    var adminBtn3 = document.createElement("button");
    var adminBtn4 = document.createElement("button");

    adminBtn1.addEventListener('click',  function() {clearOldContent(); onUsersScheduleClicked(uid);});
    adminBtn2.addEventListener('click',  function() {clearOldContent(); onUsersAddScheduleClicked(uid);});

    var adminBtnText1 = document.createTextNode("View " + currentUser + "'s schedules");
    var adminBtnText2 = document.createTextNode("Add to " + currentUser + "'s schedules");
    var adminBtnText3 = document.createTextNode("View " + currentUser + "'s task");
    var adminBtnText4 = document.createTextNode("Add to " + currentUser + "'s tasks");

    adminBtn1.appendChild(adminBtnText1);
    adminBtn2.appendChild(adminBtnText2);
    adminBtn3.appendChild(adminBtnText3);
    adminBtn4.appendChild(adminBtnText4);

    adminButtonDivEl.appendChild(adminBtn1);
    adminButtonDivEl.appendChild(adminBtn2);
    adminButtonDivEl.appendChild(adminBtn3);
    adminButtonDivEl.appendChild(adminBtn4);
}

function onUsersScheduleClicked(uid){
    onViewScheds("http://localhost:8080/ScheduleMaster3000/protected/schedule?id=" + uid);

}

function onUsersAddScheduleClicked(uid){
    document.getElementById("newschedule").style.display = 'block';
    console.log("user id by admin: " + uid);
    onCreateScheds(uid);

}

function clearOldContent(){
    let showSchedDivEl = document.getElementById('schedules');
    let showSchDivEl = document.getElementById('schedule');
    while (showSchedDivEl.firstChild) {
        showSchedDivEl.removeChild(showSchedDivEl.firstChild);
    }
    while (showSchDivEl.firstChild) {
        showSchDivEl.removeChild(showSchDivEl.firstChild);
    }

}