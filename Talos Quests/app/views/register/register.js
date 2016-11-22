var dialogsModule = require("ui/dialogs");
var frameModule = require("ui/frame");
var UserViewModel = require("../../shared/view-models/user-view-model");
var Observable = require("data/observable").Observable;
var user = new UserViewModel();
var page;
/*var user = new UserViewModel({
    email: email,
    password: password,
    name: name
});*/
var indicator = new Observable({
    isLoading: true,
    isItemVisible: false
});
exports.loaded = function(args) {
     page = args.object;
};
exports.register = function() {
    page.bindingContext= indicator;
    completeRegistration();
};
function hideIndicator(){
    indicator.isLoading=false;
    indicator.isItemVisible=true;
    page.bindingContext= indicator;
};
function completeRegistration() {
    user.register().then(function() {
            hideIndicator()
            dialogsModule.alert("Your account was successfully created.")
                .then(function() {
                    frameModule.topmost().navigate("views/main/main");
                });
        }).catch(function(error) {
            console.log(error);
            hideIndicator()
            dialogsModule
                .alert({
                    message: "Unfortunately we were unable to create your account.",
                    okButtonText: "OK"
                });
        });
}