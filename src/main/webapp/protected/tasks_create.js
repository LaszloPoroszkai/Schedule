function onNewTask() {
    const title = document.forms['new-task-form'].elements['title'].value;
    const user = getAuthorization();
    const userId = user.id;

    const params = new URLSearchParams();

    params.append('userid', userId);
    params.append('title', title);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onTasksReceived);
    xhr.open('POST', BASE_URL + 'tasks');
    xhr.send(params);
}


function onCreateTasks() {
    const createTaskDivEl = document.getElementById('newtask');
    clearErrorMessages('newtask');

    const buttonEl = document.getElementById('create');
    buttonEl.addEventListener('click', onNewTask);
}