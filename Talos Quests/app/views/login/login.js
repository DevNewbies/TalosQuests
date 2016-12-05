var dialogsModule = require("ui/dialogs");
var frameModule = require("ui/frame");
var viewModule = require("ui/core/view");
var config = require("../../shared/config");
var UserViewModel = require("../../shared/view-models/user-view-model");
var Observable = require("data/observable").Observable;
var user = new UserViewModel();
var page;

exports.loaded = function(args) {
    page = args.object;
    page.bindingContext = user;
};
function hideIndicator(){
    user.isLoading= false;
    user.isItemVisible= true;
};
function showIndicator(){
    user.isLoading=true;
    user.isItemVisible=false;
};
exports.signIn = function() 
{
    showIndicator(); 
    user.login().catch(function(error) {
      /* console.log(error);
        console.log(error.message);*/
             if(error.message === "401")
            {
                hideIndicator();
                dialogsModule.alert({
                    message: "Username or password is incorrect!",
                    okButtonText: "OK"
                });
            }
            else if(error.message=== "400")
            {
                hideIndicator();
                dialogsModule.alert({
                    message: "You did not filled the fields!",
                    okButtonText: "OK"
                });
            }
            else if(error.message === "500")
            {
                hideIndicator();
                dialogsModule.alert({
                    message: "Server is busy right now, please try again later!",
                    okButtonText: "OK"
                });
            }
        return Promise.reject();
        }).then(function(r){
            //console.log(JSON.stringify(r));
                hideIndicator();
                if(r.state==200){
                frameModule.topmost().navigate("views/main/main");
            }
        });
};
exports.fbSignIn = function(){

}
exports.register = function() {
    frameModule.topmost().navigate("views/register/register");
};