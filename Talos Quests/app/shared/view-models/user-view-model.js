var config = require("../../shared/config");
var fetchModule = require("fetch");
var Observable = require("data/observable").Observable;

function User(info) {
    info = info || {};

    // You can add properties to observables on creation
    var viewModel = new Observable({
        email:  info.email || "",
        password:  info.password || "",
        username: info.username || "",
        isLoading: false,
        isItemVisible: true
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
            //console.log(JSON.stringify(response));
            return response.json();
        })
        //.then(function(data) {
            //config.token = data.Result.access_token;
           // console.log(data.stringify);
        //});
    };
    viewModel.register = function() {
        return fetchModule.fetch(config.apiUrl + "Register", {
            method: "POST",
            body: JSON.stringify({
                userName: viewModel.username, 
                passWord: viewModel.password,
                email: viewModel.email,
                imei: 2223566
            }),
            headers: {
                "Content-Type": "application/json"
            }
        }).then(handleErrors);
    };
    return viewModel;
}

function handleErrors(response) {
    if (!response.ok) {
        console.log(JSON.stringify(response));
       throw Error(response.status);
    }
    return response;
}

module.exports = User;