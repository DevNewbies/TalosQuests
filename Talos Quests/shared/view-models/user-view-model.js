var config = require("../../shared/config");
var fetchModule = require("fetch");
var Observable = require("data/observable").Observable;
var fs = require("file-system");
var documents = fs.knownFolders.documents();
var file = documents.getFile("cookies.txt");
var cookie = require ("../../shared/view-models/cookies");
function User(info) {
    info = info || {};

    // You can add properties to observables on creation
    var viewModel = new Observable({
        email:  info.email || "",
        password:  info.password || "",
        username: info.username || "",
        imei: "" || info.imei,
        isLoading: false,
        isItemVisible: true,
        error: ""
    });
    viewModel.login = function() {
        return fetchModule.fetch(config.apiUrl + "Auth", {
            method: "POST",
            body: JSON.stringify({
                userName: viewModel.username,
                passWord: viewModel.password
            }),
            headers: {
                "Content-Type": "application/json"
            }
        })
        .then(handleErrors);
        /*.then(function(response) {
            return response.json();
        }).then(function(data) {
            var date = new Date();
            date.setTime(date.getTime() + (data.response.expireDate));
            config.token = data.response.token;
            cookie.saveCookie(data.response.token + "|" + date);
            return data;
        });*/
    }
    viewModel.register = function() {
        return fetchModule.fetch(config.apiUrl + "Register", {
            method: "POST",
            body: JSON.stringify({
                userName: viewModel.username, 
                passWord: viewModel.password,
                email: viewModel.email,
                imei:  viewModel.imei
            }),
            headers: {
                "Content-Type": "application/json"
            }
        }).then(handleErrors);
    }
    viewModel.readCookie = function(){
       return cookie.readCookie(getRequest);
}
    return viewModel;
}

function getRequest(service){
    return fetchModule.fetch(config.apiUrl + service, {
            headers: {
                "Content-Type": "application/json"
            }}).then(handleErrors);
            /*.then(function(response){
                return response.json();
            }).then(function(data){
                var date = new Date();
                date.setTime(date.getTime() + (data.response.expireDate));
                cookie.saveCookie(data.response.token + "|" + date);
                return data.state;
            });*/
}
function handleErrors(response) {
   /* if (!response.ok) {
        console.log("response not ok : " + JSON.stringify(response));
        throw Error(response.status).message;
    }*/
     //console.log("response ok : " + JSON.stringify(response));
    return response.json();
}
module.exports = User;