function getTaskTitle(placeholder, taskid, eventid) {
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', function(){taskToSchedule.call(this, placeholder, eventid);});
    xhr.open('GET', BASE_URL + 'tasks?taskid=' + taskid);
    xhr.send();
}

function createTasksTableHeader() {
    const idTdEl = document.createElement('td');
    idTdEl.textContent = 'Id';

    const titleTdEl = document.createElement('td');
    titleTdEl.textContent = 'Title';

    const deleteTdEl = document.createElement('td');
    deleteTdEl.textContent = 'Delete?';

    const trEl = document.createElement('tr');
    trEl.appendChild(idTdEl);
    trEl.appendChild(titleTdEl);
    trEl.appendChild(deleteTdEl);

    const theadEl = document.createElement('thead');
    theadEl.appendChild(trEl);
    return theadEl;
}


function createTasksTableBody(tasks) {
    const tbodyEl = document.createElement('tbody');

        for (let i = 0; i < tasks.length; i++) {
            const task = tasks[i];

            // creating id cell
            const idTdEl = document.createElement('td');
            idTdEl.textContent = task.id;

            const titleTdEl = document.createElement('td');
            titleTdEl.textContent = task.title;

            const deleteTdEl = document.createElement("BUTTON");
            const t = document.createTextNode("Delete");
            deleteTdEl.appendChild(t);
            deleteTdEl.id= task.id;
            deleteTdEl.addEventListener("click", function() {deleteTask(task.id);});


            // creating row
            const trEl = document.createElement('tr');
            trEl.appendChild(idTdEl);
            trEl.appendChild(titleTdEl);
            trEl.appendChild(deleteTdEl);

            tbodyEl.appendChild(trEl);
        }

        return tbodyEl;
}

function createTasksTable(tasks) {
    const tableEl = document.createElement('table');
    tableEl.appendChild(createTasksTableHeader());
    tableEl.appendChild(createTasksTableBody(tasks));
    tableEl.setAttribute('id', 'availableTasks');
    return tableEl;
}

function onTasksReceived() {
    const taskRespStatus = this.status;
    showtaskDivEl = document.getElementById('viewtask');

    while (showtaskDivEl.firstChild) {
        showtaskDivEl.removeChild(showtaskDivEl.firstChild);
    }
    clearErrorMessages('viewtask');

    if (taskRespStatus == 302){
        const text = this.responseText;
        const tasks = JSON.parse(text);
        showtaskDivEl.appendChild(createTasksTable(tasks));
        }
        else if(taskRespStatus == 204){
             alert("You cannot delete, please remove first from Schedule ");
        }
}

function sendResults() {
    const text = this.responseText;
    const tasks = JSON.parse(text);
    populateDrop(tasks, 'task-select');
}

function onLoadTasks(listener) {
    const xhr = new XMLHttpRequest();
    const user = getAuthorization();
    const userId = user.id;

    xhr.addEventListener('load', listener);
    xhr.open('GET', BASE_URL + 'tasks?userid=' + userId);
    xhr.send();
}

function deleteTask(taskId) {
    const xhr = new XMLHttpRequest();
    const user = getAuthorization();
    const userId = user.id;
    xhr.addEventListener('load', onTasksReceived);
    xhr.open('DELETE', BASE_URL + 'tasks?taskid=' + taskId + '&userid=' + userId );
    xhr.send();
}