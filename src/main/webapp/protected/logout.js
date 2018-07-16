function onLogoutButtonClicked() {
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onLogoutResponse);
    xhr.open('GET', 'logout');
    xhr.send();
}

function onLogoutResponse(){
    signOut();
    let logoutStatus = this.status;
    if(logoutStatus == 205){
        window.location.href = "../index.jsp";
    }
    else {
        alert("An unknown error has occured, please contact with the administrator!");
    }

}

function signOut() {
    window.onLoadCallback = function(){
        var auth2 = gapi.auth2.getAuthInstance();
                auth2.signOut().then(function () {
                console.log('User signed out.');
        });
    };
  }
