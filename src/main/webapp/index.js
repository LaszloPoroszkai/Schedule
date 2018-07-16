let errorDivEl;


function onLoad() {
    errorDivEl = document.getElementById('login-error');

    const loginButtonEl = document.getElementById('login-btn');
    loginButtonEl.addEventListener('click', onLoginButtonClicked);
    window.onbeforeunload = function(e){
      gapi.auth2.getAuthInstance().signOut();
    };
}


function onLoginButtonClicked(){

    const loginFormEl = document.getElementById('login-form');
    const emailInput = loginFormEl.querySelector('input[name=email]');
    const password = loginFormEl.querySelector('input[name=password]');

    const emailValue = emailInput.value;
    const passwordValue = password.value;

    if(validateLoginInput(emailValue, passwordValue)){
        makeRequest(emailValue, passwordValue);
    }
}


function setAuthorization(user) {
    return localStorage.setItem('user', JSON.stringify(user));
}


function onLoginResponse() {
let responseStatus = this.status;
    if(responseStatus == 302){
        const user = JSON.parse(this.responseText);
        setAuthorization(user);
        window.location.href = "protected/main.jsp";
    }
    else {
        clearMessages();
        const loginErrDivEl = document.getElementById("login-error");
        const response = JSON.parse(this.responseText);

        const pEl = document.createElement('p');
        pEl.classList.add('message');
        pEl.classList.add('login-error');
        pEl.textContent = response.message;

        loginErrDivEl.appendChild(pEl);
    }
}


function clearMessages() {
    const messageEls = document.getElementsByClassName('message');
    for (let i = 0; i < messageEls.length; i++) {
        const messageEl = messageEls[i];
        messageEl.remove();
    }
}


function onSignIn(googleUser) {
  var id_token = googleUser.getAuthResponse().id_token;

  var xhr = new XMLHttpRequest();
  xhr.open('POST', 'glogin');
  xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
  xhr.addEventListener('load', onLoginResponse);
  xhr.send('idtoken=' + id_token);
}


function makeRequest(emailValue, passwordValue){
    const params = new URLSearchParams();
    params.append('email', emailValue);
    params.append('password', passwordValue);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onLoginResponse)
    xhr.open('POST', 'login');
    xhr.send(params);
}

function validateLoginInput(emailValue, passwordValue) {
    errorDivEl.style.display = 'block';

    while(errorDivEl.firstChild){
        errorDivEl.removeChild(errorDivEl.firstChild)
    }

    if (emailValue == "" || passwordValue == "") {
        createLoginErrorMessage(errorDivEl);
        return false;    }
    else {
        return true;
    }
}

function createLoginErrorMessage(errorDivEl){
    const pEl = document.createElement('p');
    const errorMessage = "Please fill both the email and password fields!";
    pEl.appendChild(document.createTextNode(errorMessage));
    errorDivEl.appendChild(pEl);
}


function onSuccess(googleUser) {
         console.log('Logged in as: ' + googleUser.getBasicProfile().getName());
         onSignIn(googleUser);
    }
    function onFailure(error) {
          console.log(error);
    }
    function renderButton() {
          gapi.signin2.render('my-signin2', {
            'scope': 'profile email',
            'width': 240,
            'height': 50,
            'longtitle': true,
            'theme': 'dark',
            'onsuccess': onSuccess,
            'onfailure': onFailure
          });
}




document.addEventListener('DOMContentLoaded', function(){ onLoad(); onRegisterPageLoad(); });