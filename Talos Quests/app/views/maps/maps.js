// Init View
var Observable = require("data/observable").Observable;
var GeoLocation = require("nativescript-geolocation");
var mapsModule = require("nativescript-google-maps-sdk");
var Color = require("color").Color;
var CurrentPage;

// Define GPS's Class
var GPSData = new Observable({
    PosX : "40.7696886",
    PosY : "23.5531552"
});

// Unit Functions
function onNavigatingTo(args) {
    CurrentPage = args.object;
    CurrentPage.bindingContext = GPSData;
    enableLocation();
}
exports.onNavigatingTo = onNavigatingTo;

function enableLocation(args) {
    if(!GeoLocation.isEnabled()) {
      GeoLocation.enableLocationRequest();
    }
    trackLocation();
}
exports.enableLocation = enableLocation;

function trackLocation() {
  watchId = GeoLocation.watchLocation(
    function (loc) {
        if (loc) {
            console.log("Received location: " + loc.latitude + " " + loc.longitude);
            GPSData.PosX = loc.latitude;
            GPSData.PosY = loc.longitude;
        }
    },
    function(e){
        console.log("Error: " + e.message);
    },
    {
      desiredAccuracy: 3,
      updateDistance: 10,
      minimumUpdateTime : 1000 * 20
    }
  );
}
exports.trackLocation = trackLocation;

function mapIsReady(args) {
    showLocation(args);
    showRadius(args);
    generateQuest(args);
}
exports.mapIsReady = mapIsReady;

function showLocation(args) {
  var mapView = args.object;

  var marker = new mapsModule.Marker();
  marker.position = mapsModule.Position.positionFromLatLng(GPSData.PosX,GPSData.PosY);
  marker.title = "Me";
  marker.userData = {index: 1};
  mapView.addMarker(marker);
}

function showRadius(args) {
  var mapView = args.object;

  var circle = new mapsModule.Circle();
  circle.center = mapsModule.Position.positionFromLatLng(GPSData.PosX, GPSData.PosY);
  circle.visible = true;
  circle.radius = 100;
  circle.fillColor = new Color('#99ff8800');
  circle.strokeColor = new Color('#99ff0000');
  circle.strokeWidth = 2;
  mapView.addCircle(circle);
}

function generateQuest(args) {
  var mapView = args.object;
  var marker = new mapsModule.Marker();
  marker.position = mapsModule.Position.positionFromLatLng(GPSData.PosX + 0.001,GPSData.PosY + 0.001);
  marker.title = "Quest";
  marker.userData = {index: 2};
  mapView.addMarker(marker);
}
