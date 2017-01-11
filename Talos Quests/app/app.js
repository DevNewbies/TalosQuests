var application = require("application");
application.start({ moduleName: "views/login/login"});
if(application.ios){
	GMSServices.provideAPIKey("AIzaSyA5iCaRwzYYpQkdjVzWQwLzpke3p1wXT4A");
}