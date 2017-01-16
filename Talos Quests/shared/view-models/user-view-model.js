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
        .then(handleErrors)
        .then(function(response) {
            return response.json();
        }).then(function(data) {
            var date = new Date();
            date.setTime(date.getTime() + (data.response.expireDate));
            config.token = data.response.token;
            cookie.saveCookie(data.response.token + "|" + date);
            return data;
        });
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
    viewModel.gameCreate = function(Lat,Lng){
        return fetchModule.fetch(config.apiUrl + "Game/Create?token=" + config.token, {
            method: "POST",
            body:JSON.stringify({
            lat: Lat, 
            lng:Lng
            }),
            headers:{
                "Content-Type": "application/json"
            }
        }).then(handleErrors);
        /*.then(function(data){
                return data.json();
            });*/
    }
    viewModel.getGame = function(){
        return fetchModule.fetch(config.apiUrl + "Game?token=" + config.token)
            .then(handleErrors);
            /*.then(function(data){
                return data.json();
            });*/
    }
    viewModel.activeGame= function(){
        return fetchModule.fetch(config.apiUrl + "Game/Active?token=" + config.token)
            .then(handleErrors);
            /*.then(function(data){
                return data.json();
            });*/
    }
    viewModel.continueGame= function(id){
        return fetchModule.fetch(config.apiUrl + "Game/Continue/"+ id + "?token=" + config.token)
            .then(handleErrors);
            /*.then(function(data){
                return data.json();
            });*/
    }
    viewModel.deleteGame= function(){
        return fetchModule.fetch(config.apiUrl + "Delete/Game/" + id  + "?token=" + config.token)
            .then(handleErrors);
            /*.then(function(data){
                return data.json();
            });*/
    }
    viewModel.getQuest = function(){
        return fetchModule.fetch(config.apiUrl + "Game/Quest?token=" + config.token, {
            }).then(handleErrors);
        /*.then(function(data){
                return data.json();
            });*/
    }
     viewModel.getNextQuest = function(){
        return fetchModule.fetch(config.apiUrl + "/Game/Quest/Next?token=" + config.token, {
            }).then(handleErrors);
        /*.then(function(data){
                return data.json();
            });*/
    }
    viewModel.questSubmit = function(choice){
        fetchModule.fetch(config.apiUrl + "Game/Quest/SubmitAnswer?token=" + config.token, {
            method: "POST",
            body: JSON.stringify({
                QuestChoice: choice
            }),
            headers: {
                "Content-Type": "application/json"
            }
        }).then(handleErrors);/*.then(function(data){
                return data.json();
            });*/
    }
    return viewModel;
}

function getRequest(service){
    return fetchModule.fetch(config.apiUrl + service, {
            }).then(handleErrors).then(function(response){
                return response.json();
            }).then(function(data){
                var date = new Date();
                date.setTime(date.getTime() + (data.response.expireDate));
                cookie.saveCookie(data.response.token + "|" + date);
                return data;
            });
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