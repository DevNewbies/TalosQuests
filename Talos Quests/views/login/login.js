var dialogsModule = require("ui/dialogs");
var frameModule = require("ui/frame");
var viewModule = require("ui/core/view");
var config = require("../../shared/config");
var UserViewModel = require("../../shared/view-models/user-view-model");
//var anime = require("../animation/animation");
var user = new UserViewModel();
var page;
exports.user = user;
exports.loaded = function(args) {
    page = args.object;
    page.bindingContext = user;
    //anime.multiFadeAnimation(page,"child","StackLayout",1000);
}
exports.sign= function(){
    showIndicator(); 
   return user.readCookie()
    /*.catch(function(error){
        console.log("error " + error);
        hideIndicator();
        anime.multiFadeAnimation(page,"child","StackLayout",1000);
        return Promise.reject();
        })*/
        .then(function(data){
            hideIndicator();
            if(data==200){
                frameModule.topmost().navigate("views/main/main");
            }
            return data;
        });
}
function hideIndicator(){
    user.isLoading= false;
    user.isItemVisible= true;
}
function showIndicator(){
    user.isLoading=true;
    user.isItemVisible=false;
}
exports.errorHandling = function (error){
      if(error === 401)
            {
                hideIndicator();
                dialogsModule.alert({
                    message: "Username or password is incorrect!",
                    okButtonText: "OK"
                });
                return 401;
            }
            else if(error === 400)
            {
                hideIndicator();
                dialogsModule.alert({
                    message: "You did not filled the fields!",
                    okButtonText: "OK"
                });
                return 400;
            }
            else if(error === 500)
            {
                hideIndicator();
                    dialogsModule.alert({
                    message: "Server is busy right now, please try again later!",
                    okButtonText: "OK"
                });
                    return 500;
            }else{
                hideIndicator();
                    dialogsModule.alert({
                    message: "Server is down, please try again later!",
                    okButtonText: "OK"
                });
                    return 0;
            }
}
exports.signIn = function() 
{
    showIndicator(); 
    user.login().catch(function(error){
        errorHandling(error);
        return Promise.reject();
        }).then(function(data){
            hideIndicator();
            if(data.state==200){
                frameModule.topmost().navigate("views/main/main");
            }
        });
}
exports.register = function() {
    frameModule.topmost().navigate("views/register/register");
}