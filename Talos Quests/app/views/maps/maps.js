// Init View
var Observable = require("data/observable").Observable;
var GeoLocation = require("nativescript-geolocation");
var mapsModule = require("nativescript-google-maps-sdk");
var Color = require("color").Color;
var disabledLocation = false;
var CurrentPage;
var mapView;

var UserLocation = new Observable({
    userLatitude: 11.00000,
    userLongitude: 11.00000,
    userZoom: 17,
    userLocated: false
});

// Unit Functions
function onNavigatingTo(args) {
    CurrentPage = args.object;
    CurrentPage.bindingContext = UserLocation;
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
                console.log("Received location: " + loc.latitude + " " + loc.longitude);
                CurrentPage.userLatitude = loc.latitude;
                CurrentPage.userlongitude = loc.longitude;
                CurrentPage.userZoom = 17;
                CurrentPage.userLocated = true;
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
    if (CurrentPage.userLocated == true) {
        mapView.latitude = CurrentPage.userLatitude;
        mapView.longitude = CurrentPage.userlongitude;
        mapView.zoom = CurrentPage.userZoom;


        var UserMarker = new mapsModule.Marker();
        mapView.findMarker(function(marker) {
            if (marker.userData.index == 1) {
                mapView.removeMarker(marker);
            }
        });

        UserMarker.position = mapsModule.Position.positionFromLatLng(CurrentPage.userLatitude, CurrentPage.userlongitude);
        UserMarker.title = "My Location";
        UserMarker.userData = {
            index: 1
        };
        mapView.addMarker(UserMarker);

        var UserCircle = new mapsModule.Circle();
        mapView.removeAllCircles();

        UserCircle.center = mapsModule.Position.positionFromLatLng(CurrentPage.userLatitude, CurrentPage.userlongitude);
        UserCircle.visible = true;
        UserCircle.radius = 50;
        UserCircle.fillColor = new Color('#99ff8800');
        UserCircle.strokeColor = new Color('#99ff0000');
        UserCircle.strokeWidth = 2;
        mapView.addCircle(UserCircle);
    }
}
exports.findMe = findMe;

function changeTab(args) {
    if (CurrentPage.userLocated == true) {
        findMe();
    }
}
exports.changeTab = changeTab;
