var fs = require("file-system");
var documents = fs.knownFolders.documents();
var file = documents.getFile("cookies.txt");
var config = require("../../shared/config");

exports.saveCookie = function(cookie){
    file.writeText(cookie).then(function(){
       // console.log("Cookie was succesfully written!");
    }, function(error){
       // console.log("Error: Could not write cookie in the file!" + error);
    });
}

exports.readCookie = function(getRequest){
	return file.readText().then(function(content){
            config.token=content.split("|");
                if(config.token[0]!="")
                {
                   // console.log("token :"+ config.token[0]);
                   return getRequest("Session?token=" + config.token[0]);
                }
        }, function(error){
           // console.log("Error: " + error);
            return error;
        });
}
exports.deleteCookie = function(){
    file.remove()
    .then(function (result) {
       // console.log("The file was succesfully deleted!");
    }, function (error) {
       // console.log("Error: Could not delete that file!" + error);
    });
}