function onRegisterPageLoad() {
    const registerBtnEl = document.getElementById('register-btn');
    registerBtnEl.addEventListener('click', postRegisterRequest);
}


function postRegisterRequest(){
    const registerFormEl = document.getElementById('registration-form');

    const emailInput = registerFormEl.querySelector('input[name=email]');
    const nameInput = registerFormEl.querySelector('input[name=user-name]');
    const passwordInput = registerFormEl.querySelector('input[name=password]');

    const email = emailInput.value;
    const name = nameInput.value;
    const password = passwordInput.value;

    const params = new URLSearchParams();
    params.append('email', email);
    params.append('name', name);
    params.append('password', password);


    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onRegisterResponse);
    xhr.open('POST', 'register');
    xhr.send(params);
}

function onRegisterResponse(){
    let registerResponseStatus = this.status;
    const registerErrorDivEl = document.getElementById('registration-error');
    const registerOkDivEl = document.getElementById("reg-ok");

    const response = JSON.parse(this.responseText);

    clearMessages();

    const pEl = document.createElement('p');
    pEl.classList.add('message');

    if(registerResponseStatus == 200){
        registerOkDivEl.style.display = 'block';

        pEl.classList.add('reg-ok');
        pEl.textContent = response.message;

        registerOkDivEl.appendChild(pEl);
    }
    else {
        registerErrorDivEl.style.display = 'block';

        pEl.classList.add('registration-error');
        pEl.textContent = response.message;

        registerErrorDivEl.appendChild(pEl);
    }
}


