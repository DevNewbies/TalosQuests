var dialogsModule = require("ui/dialogs");
var frameModule = require("ui/frame");
var UserViewModel = require("../../shared/view-models/user-view-model");
var telephony = require("nativescript-telephony");
var anime = require("../animation/animation");
var user = new UserViewModel();
exports.user = user;
var page;
exports.loaded = function(args) {
    page = args.object;
    page.bindingContext= user;
    anime.multiFadeAnimation(page,"child","StackLayout",1000);
    user.username= "Antonis";
    user.email="asd@hotmail.com";
    user.password="Test1234@";
}
 function getDeviceId (){
    telephony.Telephony().then(function(resolved)
    {
        user.imei= resolved.deviceId;
        console.log("user: "+ user.imei);
        }).catch(function(error) {
    console.error('error >', error)
    console.dir(error);
    });
}
exports.register = function() {
    completeRegistration();
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
    if(error===302)
        {
            hideIndicator();
            dialogsModule.alert({
                message: "Username is already used!",
                okButtonText: "OK"
            });
            return 302;
        }else if(error===400)
        {
            hideIndicator();
            dialogsModule.alert({
                message: "You did not filled all the fields!",
                okButtonText: "OK"
            });
            return 400;
        }else{
            hideIndicator();
            dialogsModule.alert({
                message: "Server is down. Please try again later!",
                okButtonText: "OK"
            });
            return 0;
        }
}
exports.validator = function(){
    if(/^[a-zA-Z0-9_\-]{4,32}$/.test(user.username))
    {
        if(/^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,32}$/.test(user.password))
        {
            if(/^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/.test(user.email))
            {
                    return true;
            }else
            {
                    return 2;
                    //return validatorErrorHandler(2);
                   // return false;
            }
        }else
        {
            return 1;
            //validatorErrorHandler(1);
            //return false;
        }
    
    }else
    {
        return 0;
        //return validatorErrorHandler(0);
        //return false;
    }
}
function validatorErrorHandler(error){
    if(error==0){
        user.error="Username length needs to be at least 4 chars included 1 uppercase.";
    }else if(error==1){
        user.error="Password must be at least 6 chars included at least 1 letter numbers and 1 symbol.";
    }else if(error==2){
        user.error="Email doesnt seems to follow the 'email' format.";
    }
}
function completeRegistration() { 
    if(validator()){
            showIndicator();
            getDeviceId();
            user.register().then(function(r) {
            hideIndicator();   
            dialogsModule.alert("Your account was successfully created.")
                .then(function() {
                    frameModule.topmost().navigate("views/login/login");
                });
        }).catch(function(error) {
            errorHandling(error);
        });
    }
}