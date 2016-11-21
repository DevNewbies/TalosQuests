var dialogsModule = require("ui/dialogs");
var frameModule = require("ui/frame");
var config = require("../../shared/config");
var UserViewModel = require("../../shared/view-models/user-view-model");
var Observable = require("data/observable").Observable;
var user = new UserViewModel();
var page;
/*var user = new UserViewModel({
    email: "username@domain.com",
    password: "password"
});*/
var indicator = new Observable({
    isLoading: true,
    isItemVisible: false
});
/*indicator.hideIndicator = function() {
    indicator.isLoading=false;
    indicator.isItemVisible=true;
    page.bindingContext= indicator;
};*/

exports.loaded = function(args) {
    page = args.object;
};
function hideIndicator(){
    indicator.isLoading=false;
    indicator.isItemVisible=true;
    page.bindingContext= indicator;
};
exports.signIn = function() {
    page.bindingContext= indicator;
    user.login().catch(function(error){
            hideIndicator()
            console.log(error);
            dialogsModule.alert({
                message: "Username or password are incorrect.",
                okButtonText: "OK"
            });
            return Promise.reject();
        })
        .then(function() {
            hideIndicator()
            frameModule.topmost().navigate("views/test/test");
        });
};
exports.register = function() {
    frameModule.topmost().navigate("views/register/register");
};