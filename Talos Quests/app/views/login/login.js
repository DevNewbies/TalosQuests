var createViewModel = require("./login-view-model").createViewModel;
var CurrentPage;
var ArgUsername;
var ArgPassword;

function onNavigatingTo(args) {
    CurrentPage = args.object;
    CurrentPage.bindingContext = createViewModel();
}

exports.onNavigatingTo = onNavigatingTo;

exports.LoginProcess = function() {    
    ArgUsername = CurrentPage.getViewById("ArgUsername").text;
    ArgPassword = CurrentPage.getViewById("ArgPassword").text;

    if(ArgUsername == "" && ArgPassword == "") {
        alert("You must enter your Username and your Password","OK");
    } else if(ArgUsername == "") {
        alert("You must enter your Username", "OK");
    } else if(ArgPassword == "") {
        alert("You must enter your Password", "OK");
    } else {
        alert("Username = " + ArgUsername + "\n" + "Password = " + ArgPassword, "OK");
        // To-Do : Navigate To Main Menu
    }
}

exports.RegisterProcess = function() {
    // To-Do : Navigate To Register Page
    alert("Register Triggered", "OK");
}