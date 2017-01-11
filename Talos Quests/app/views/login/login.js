var dialogsModule = require("ui/dialogs");
var frameModule = require("ui/frame");
var viewModule = require("ui/core/view");
var config = require("../../shared/config");
var UserViewModel = require("../../shared/view-models/user-view-model");
var user = new UserViewModel();
var page;
var anime = require("../animation/animation");
var timer = require("timer");
user.username="Antonis";
user.password="Test1234@";
exports.loaded = function(args) {
    page = args.object;
    page.bindingContext = user;
    anime.multiFadeAnimation(page,"child","StackLayout",1000);
    signIn();
};
function signIn(){
    showIndicator(); 
    user.readCookie().catch(function(error){
       // console.log("error " + error);
        hideIndicator();
        anime.multiFadeAnimation(page,"child","StackLayout",1000);
        return Promise.reject();
        }).then(function(data){
            if(data.state==200){
                frameModule.topmost().navigate("views/main/main");
            }
        });
    timer.setTimeout(() => {
        hideIndicator();
    }, 2000);
}
function hideIndicator(){
    user.isLoading= false;
    user.isItemVisible= true;
};
function showIndicator(){
    user.isLoading=true;
    user.isItemVisible=false;
};
function errorHandling(error){
      if(error === "401")
            {
                hideIndicator();
                dialogsModule.alert({
                    message: "Username or password is incorrect!",
                    okButtonText: "OK"
                });
            }
            else if(error === "400")
            {
                hideIndicator();
                dialogsModule.alert({
                    message: "You did not filled the fields!",
                    okButtonText: "OK"
                });
            }
            else if(error === "500")
            {
                hideIndicator();
                    dialogsModule.alert({
                    message: "Server is busy right now, please try again later!",
                    okButtonText: "OK"
                });
            }else{
                hideIndicator();
                    dialogsModule.alert({
                    message: "Check if you are connected to the internet!",
                    okButtonText: "OK"
                });
            }
}
exports.signIn = function() 
{
    showIndicator(); 
    user.login().catch(function(error){
        errorHandling(error);
        return Promise.reject();
        }).then(function(data){
            if(data.state==200){
                frameModule.topmost().navigate("views/main/main");
            }
        });
}
exports.register = function() {
    frameModule.topmost().navigate("views/register/register");
}
