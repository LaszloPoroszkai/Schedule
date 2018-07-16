<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <c:url value="/style.css" var="styleUrl"/>
        <c:url value="/protected/main.js" var="mainUrl"/>
        <c:url value="/protected/logout.js" var="logoutUrl"/>
        <c:url value="/protected/schedules_view.js" var="schedulesUrl"></c:url>
        <c:url value="/protected/schedule_view.js" var="scheduleUrl"></c:url>
        <c:url value="/protected/schedule_create.js" var="addScheduleUrl"></c:url>
        <c:url value="/protected/admin.js" var="adminUrl"></c:url>
        <c:url value="/protected/tasks_view.js" var="tasksUrl"></c:url>
        <c:url value="/protected/tasks_create.js" var="addTasksUrl"></c:url>
        <c:url value="/protected/events.js" var="eventsUrl"></c:url>
        <link rel="stylesheet" type="text/css" href="${styleUrl}">
        <script src="${mainUrl}"></script>
        <script src="${eventsUrl}"></script>
        <script src="${logoutUrl}"></script>
        <script src="${schedulesUrl}"></script>
        <script src="${addScheduleUrl}"></script>
        <script src="${adminUrl}"></script>
        <script src="${tasksUrl}"></script>
        <script src="${addTasksUrl}"></script>
        <script src="${scheduleUrl}"></script>
    </head>
<body>
    <div id="main-menu">
        <div class="sidenav">
          <a href="#" id="view-task" onclick="return false;">View tasks</a>
          <a href="#" id="add-task" onclick="return false;">Add new task</a>
          <a href="#" id="view-schedules" onclick="return false;">View schedules</a>
          <a href="#" id="add-schedules" onclick="return false;">Add new schedules</a>
          <a href="main.jsp" id="back-to-main" >Back to main</a>
          <a href="#" id="logout" onclick="return false;">Logout</a>
        </div>

        <div class="content">
        <div id="admin-drop">
                     <div class="dropdown">
                     <a href="#" class="dropbtn" id = "dropbtn" onclick="return false;">User Profiles</a>
                     <div class="dropdown-content" id="dropdown-content">
                     </div>
                </div>
            </div>
            <div id="welcome-header"><h1>Welcome!</h1></div>
            <div id="see-all-content"></div>
            <div id="admin-button"></div>
            <div id="viewtask">
                <h2>Your Tasks</h2>
                <div id="tasks-content"></div>
            </div>
            <div id="newtask">
                <form id="new-task-form">
                    <h3>Create new task below:</h3>
                    <input type="text" name="title">
                    <input type="button" id="create" value="Create Task">
                </form>
            </div>
            <div id="schedules"></div>
            <div id="newschedule">
                <form id="new-schedule-form">
                    <h3>Create new schedule below:</h3>
                    <input type="text" name="name">
                    <input type="button" id="createSched" value="Create Schedule">
                </form>
            </div>
            <div id="showschedule">
                <form id="task-to-schedule">
                    <select id="task-select"></select>
                    <select id="column-select"></select>
                    <select id="start-select">
                        <c:forEach items="${hours1}" var="hour">
                            <option value=${hour} label=${hour}></option>
                        </c:forEach>
                    </select>
                    <select id="end-select">
                        <c:forEach items="${hours2}" var="hour">
                            <option value=${hour} label=${hour}></option>
                        </c:forEach>
                    </select>
                    <input id="desc">
                    <input type="submit" id="sendtask" value="Add Task" onclick="return false">
                </form>
                <form id="new-column">
                    <input type="text" id="column-name">
                    <input type="button" id="newColumn" value="Add Column" onclick="return false">
                </form><br>
                <div id="schedule">
                </div>
            </div>
            <div id="event-update" class="modal">
                <div class="event-update-content">
                    <span class="close" id="close-sch" onclick="return false">&times;</span>
                    <select id="upd-task-select"></select>
                    <select id="upd-column-select"></select>
                    <select id="upd-start-select">
                        <c:forEach items="${hours1}" var="hour">
                            <option value=${hour} label=${hour}></option>
                        </c:forEach>
                    </select>
                    <select id="upd-end-select">
                        <c:forEach items="${hours2}" var="hour">
                            <option value=${hour} label=${hour}></option>
                        </c:forEach>
                    </select>
                    <br><br>
                    <textarea id="description" rows="4" cols="50"></textarea>
                    <br><br>
                    <input type="submit" id="updEvent" value="Update Event" onclick="return false">
                    <br><br>
                    <input type="submit" id="deleteEvent" value="Delete Event" onclick="return false">
                </div>

            </div>
    </div>
</body>
</html>