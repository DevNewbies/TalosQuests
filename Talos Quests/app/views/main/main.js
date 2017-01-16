// Init View
var Observable = require("data/observable").Observable;
var frameModule = require("ui/frame");
var GeoLocation = require("nativescript-geolocation");
var gameQueries = require("../maps/gameQueries/gameQueries");
var cookies = require("../../shared/view-models/cookies");
var CurrentPage;
var ListView;

var userLocation = new Observable({
    userLatitude: 0,
    userLongitude: 0,
});

// Unit Functions
function onNavigatingTo(args) {
    CurrentPage = args.object;
    CurrentPage.bindingContext = userLocation;
    enableLocation();
}
exports.onNavigatingTo = onNavigatingTo;

function enableLocation() {
    if (!GeoLocation.isEnabled()) {
        alert("Can't use GPS info.\nYou may see void map !!!\nPlease enable your GPS and open our game again!");
    } else {
        trackLocation();
    }
}
exports.enableLocation = enableLocation;

function trackLocation() {
    watchId = GeoLocation.watchLocation(
        function(loc) {
            if (loc) {
                userLocation.userLatitude = loc.latitude;
                userLocation.userLongitude = loc.longitude;
            }
        },
        function(e) {
            alert("Can't use GPS info.\nYou may see void map !!!\nPlease enable your GPS and open our game again!");
        }, {
            desiredAccuracy: 3,
            updateDistance: 10,
            minimumUpdateTime: 1000 * 2
        }
    );
}
exports.trackLocation = trackLocation;

function loadGames(args) {
    ListView = args.object;
    refreshGames();
}
exports.loadGames = loadGames;

function refreshGames() {
    gameQueries.getGame().then(function(data) {
        ListView.items = data;
    });
}

function logOut() {
    cookies.deleteCookie();
    frameModule.topmost().navigate("views/login/login");
}
exports.logOut = logOut;

function addGame(args) {
    gameQueries.createGame(userLocation.userLatitude, userLocation.userLongitude);
    refreshGames();
}
exports.addGame = addGame;

function startGame(args) {
    var navigationOptions = {
        moduleName: 'views/maps/maps',
        context: {
            GameID: args.object.id
        }
    }
    frameModule.topmost().navigate(navigationOptions);
}
exports.startGame = startGame;

function deleteGame(args) {
    gameQueries.deleteGame(args.object.id);
    refreshGames();
}
exports.deleteGame = deleteGame;
