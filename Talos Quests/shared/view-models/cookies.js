var fs = require("file-system");
var documents = fs.knownFolders.documents();
var file = documents.getFile("cookies.txt");
var config = require("../../shared/config");

exports.saveCookie= function(cookie){
        return file.writeText(cookie).then(function(){
            console.log("Cookie was succesfully written!");
            return true;
        }, function(error){
            console.log("Error: Could not write cookie in the file!" + error);
            return false;
        });
}
exports.readCookie= function(getRequest){
	return file.readText().then(function(content){
            config.token=content.split("|");
                if(config.token[0]!="")
                {
                    console.log("token :"+ config.token[0]);
                   return getRequest("Session?token=" + config.token[0]);
                }else
                    return false;
        }, function(error){
            console.log("Error: " + error);
            return 0;
        });
}
exports.deleteCookie = function(){
    return file.remove().then(function (result) {
        console.log("The file was succesfully deleted!");
         return true;
    }, function (error) {
        console.log("Error: Could not delete that file!" + error);
         return false;
    });
}