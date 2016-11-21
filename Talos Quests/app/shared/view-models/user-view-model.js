var config = require("../../shared/config");
var fetchModule = require("fetch");
var Observable = require("data/observable").Observable;

function User(info) {
    info = info || {};

    // You can add properties to observables on creation
    var viewModel = new Observable({
        email: info.email || "",
        password: info.password || "",
        name: info.name || ""
    });
    viewModel.login = function() {
        //return fetchModule.fetch(config.apiUrl + "oauth/token", {
        return fetchModule.fetch(config.apiUrl + "Auth", {
            method: "POST",
            body: JSON.stringify({
                facebookId: 0, 
                userName: viewModel.get("email"), 
                passWord: viewModel.get("password"),
                token: 0
                //grant_type: "password"
            }),
            headers: {
                "Content-Type": "application/json"
            }
        })
        .then(handleErrors)
        .then(function(response) {
            return response.json();
        })
        .then(function(data) {
            config.token = data.Result.access_token;
        });
    };
    viewModel.register = function() {
        return fetchModule.fetch(config.apiUrl + "Users", {
            method: "POST",
            body: JSON.stringify({
                facebookId: 0, 
                userName: viewModel.get("email"), 
                passWord: viewModel.get("password"),
                fullName: viewModel.get("name")
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
        throw Error(response.statusText);
    }
    return response;
}

module.exports = User;