var dialogsModule = require("ui/dialogs");
var frameModule = require("ui/frame");
var UserViewModel = require("../../shared/view-models/user-view-model");
var Observable = require("data/observable").Observable;
var user = new UserViewModel();
var page;
exports.loaded = function(args) {
    page = args.object;
    page.bindingContext= user;
};
exports.register = function() {
    completeRegistration();
};
function hideIndicator(){
    user.isLoading= false;
    user.isItemVisible= true;
};
function showIndicator(){
    user.isLoading=true;
    user.isItemVisible=false;
};
function completeRegistration() {
    showIndicator();
    user.register().then(function(r) {
            hideIndicator();   
            dialogsModule.alert("Your account was successfully created.")
                .then(function() {
                    frameModule.topmost().navigate("views/main/main");
                });
        }).catch(function(error) {
            console.log(error);
             if(error.message==="301")
            {
                hideIndicator();
                dialogsModule.alert({
                    message: "Username is already used!",
                    okButtonText: "OK"
                });
                log.hideIndicator();
            }else if(error.message==="400")
            {
                hideIndicator();
                dialogsModule.alert({
                    message: "You did not filled all the fields!",
                    okButtonText: "OK"
                });
            }else{
                hideIndicator();
                dialogsModule.alert({
                    message: "Something went wrong.Try again!",
                    okButtonText: "OK"
                });
            }
        });
}