// Init View
var Observable = require("data/observable").Observable;
var ObservableArray = require("data/observable-array").ObservableArray;
var GeoLocation = require("nativescript-geolocation");
var mapsModule = require("nativescript-google-maps-sdk");
var cookies = require("../../shared/view-models/cookies");
var gameQueries = require("./gameQueries/gameQueries");
var questQueries = require("./questQueries/questQueries");
var Color = require("color").Color;
var frameModule = require("ui/frame");
var disabledLocation = false;
var CurrentPage;
var mapView;
var ListView;
var GameID;

var userLocation = new Observable({
    userLatitude: 0,
    userLongitude: 0,
    userLocated: false
});

var playQuest = new Observable({
    QuestTitle: "Title",
    QuestExp: "Exp",
    QuestQuestion: "Question",
    QuestChoiceA: "A",
    QuestChoiceB: "B",
    QuestChoiceC: "C",
    QuestChoiceD: "D",
    QuestLatidude: 0,
    QuestLongitude: 0
});

// Unit Functions
function onNavigatingTo(args) {
    CurrentPage = args.object;
    var gotData = CurrentPage.navigationContext;
    GameID = gotData.GameID;
    gameQueries.continueGame(GameID);
    CurrentPage.bindingContext = userLocation;
    CurrentPage.bindingContext = playQuest;
    enableLocation();

}
exports.onNavigatingTo = onNavigatingTo;

function enableLocation() {
    if (!GeoLocation.isEnabled()) {
        alert("Can't use GPS info.\nYou may see void map !!!");
        disabledLocation = true;
    }
}
exports.enableLocation = enableLocation;

function mapIsReady(args) {
    mapView = args.object;
    if (!disabledLocation) {
        trackLocation();
    }
}
exports.mapIsReady = mapIsReady;

function trackLocation() {
    watchId = GeoLocation.watchLocation(
        function(loc) {
            if (loc) {
                userLocation.userLatitude = loc.latitude;
                userLocation.userLongitude = loc.longitude;
                userLocation.userLocated = true;
                findMe();
            }
        },
        function(e) {
            alert("Can't use GPS info.\nYou may see void map !!!");
        }, {
            desiredAccuracy: 3,
            updateDistance: 10,
            minimumUpdateTime: 1000 * 2
        }
    );
}
exports.trackLocation = trackLocation;

function findMe() {
    if (userLocation.userLocated) {
        mapView.latitude = userLocation.userLatitude;
        mapView.longitude = userLocation.userLongitude;
        mapView.zoom = 17;

        var UserMarker = new mapsModule.Marker();
        mapView.findMarker(function(marker) {
            if (marker.userData.index == 1) {
                mapView.removeMarker(marker);
            }
        });
        UserMarker.position = mapsModule.Position.positionFromLatLng(userLocation.userLatitude, userLocation.userLongitude);
        UserMarker.title = "My Location";
        UserMarker.userData = {
            index: 1
        };
        mapView.addMarker(UserMarker);

        var UserCircle = new mapsModule.Circle();
        mapView.removeAllCircles();
        UserCircle.center = mapsModule.Position.positionFromLatLng(userLocation.userLatitude, userLocation.userLongitude);
        UserCircle.visible = true;
        UserCircle.radius = 50;
        UserCircle.fillColor = new Color('#99ff8800');
        UserCircle.strokeColor = new Color('#99ff0000');
        UserCircle.strokeWidth = 2;
        mapView.addCircle(UserCircle);
        refreshQuest();
    }
}
exports.findMe = findMe;

function changeTab(args) {
    if (userLocation.userLocated) {
        findMe();
    }
}
exports.changeTab = changeTab;

function refreshQuest() {
    questQueries.getNextQuest().then(function(data) {
        playQuest.QuestTitle = data.response.quest.name;
        playQuest.QuestExp = data.response.quest.exp;
        playQuest.QuestQuestion = data.response.quest.content;

        playQuest.QuestChoiceA = data.response.quest.availableChoices[0].content;
        playQuest.QuestChoiceB = data.response.quest.availableChoices[1].content;
        playQuest.QuestChoiceC = data.response.quest.availableChoices[2].content;
        playQuest.QuestChoiceD = data.response.quest.availableChoices[3].content;

        playQuest.QuestLatidude = data.response.quest.location.lat;
        playQuest.QuestLongitude = data.response.quest.location.log;
    });
    addQuestPoint();
}
exports.refreshQuest = refreshQuest;

function addQuestPoint() {
    var polyline = new mapsModule.Polyline();
    var point = mapsModule.Position.positionFromLatLng(playQuest.QuestLatidude, playQuest.QuestLongitude);
    mapView.removeAllPolylines();
    polyline.addPoints([
        mapsModule.Position.positionFromLatLng(playQuest.QuestLatidude + 4, playQuest.QuestLongitude + 4),
        point,
        mapsModule.Position.positionFromLatLng(playQuest.QuestLatidude - 4, playQuest.QuestLongitude - 4)
    ]);
    polyline.visible = true;
    polyline.width = 8;
    polyline.color = new Color('#DD00b3fd');
    polyline.geodesic = true;
    mapView.addPolyline(polyline);
}
exports.addQuestPoint = addQuestPoint;

function stopGame(args) {
    frameModule.topmost().navigate("views/main/main");
}
exports.stopGame = stopGame;

function submitChoice(args) {
    questQueries.submitQuest(args.object.text);
    var navigationOptions = {
        moduleName: 'views/maps/maps',
        context: {
            GameID: GameID
        }
    }
    frameModule.topmost().navigate(navigationOptions);
}
exports.submitChoice = submitChoice;
