<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <c:url value="/style.css" var="styleUrl"/>
        <c:url value="/index.js" var="indexUrl"/>
        <c:url value="register.js" var="registerUrl"></c:url>
        <link rel="stylesheet" type="text/css" href="${styleUrl}">
        <script src="${indexUrl}"></script>
        <script src='${registerUrl}'></script>
        <script src="https://apis.google.com/js/platform.js?onload=renderButton" async defer></script>
        <title>ScheduleMaster3000</title>
    </head>
<body>
<div id='login' class='login'>
    <div id='login-header-text'>Login</div>
    <form id='login-form' onsubmit="return false;">
        <p>
            <input type='text' name='email' value='Email'>
            <input type='password' name='password' value='Password'>
        </p>
        <p>
            <button id='login-btn'>Login</button>
        </p>
        <p>
            <div id='login-error' class='status-error'></div>
        </p>
    </form>
</div>
<br>

<div id="my-signin2" data-onsuccess="onSuccess" >
    <meta name="google-signin-client_id" content="34170676201-s6hhek7s226udra0di1cdnskq2p8b850.apps.googleusercontent.com">
</div>
<br>
<div id='registration'>
    <div class='registration-text'>Sing up</div>
    <form id='registration-form' onsubmit='return false;'>
        <p>
            Email:<br>
            <input type='text' name='email'><br>
            Username:<br>
            <input type='text' name='user-name'><br>
            Password:<br>
            <input type='text' name='password'>
        </p>
        <p>
            <button id='register-btn'>Submit</button>
        </p>
        <p>
            <div id='registration-error' class='status-error'></div>
            <div id='reg-ok'></div>
        </p>
    </form>
</div>




</div>



</body>
</html>
