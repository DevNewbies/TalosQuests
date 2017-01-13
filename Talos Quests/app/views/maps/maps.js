// Init View
var Observable = require("data/observable").Observable;
var GeoLocation = require("nativescript-geolocation");
var mapsModule = require("nativescript-google-maps-sdk");
var Color = require("color").Color;
var CurrentPage;
var mapView;
var justTrack = false;

// Unit Functions
function onNavigatingTo(args) {
    CurrentPage = args.object;
    enableLocation();
}
exports.onNavigatingTo = onNavigatingTo;

function enableLocation(args) {
    if(!GeoLocation.isEnabled()) {
      GeoLocation.enableLocationRequest();
    }
}
exports.enableLocation = enableLocation;

function mapIsReady(args) {
    mapView = args.object;
    trackLocation();
}
exports.mapIsReady = mapIsReady;

function trackLocation() {
  watchId = GeoLocation.watchLocation(
    function (loc) {
        if (loc) {
            console.log("Received location: " + loc.latitude + " " + loc.longitude);
            mapView.latitude = loc.latitude;
            mapView.longitude = loc.longitude;
            if(justTrack) {
              var marker = mapView.findMarker(function (marker) {
                  return marker.userData.index === 1;
              });
              marker.position = mapsModule.Position.positionFromLatLng(mapView.latitude,mapView.longitude);
            } else {
              justTrack = true;
              var marker = new mapsModule.Marker();
              marker.position = mapsModule.Position.positionFromLatLng(mapView.latitude,mapView.longitude);
              marker.title = "Me";
              marker.userData = {index: 1};
              mapView.addMarker(marker);
            }
        }
    },
    function(e){
        alert("Can't use GPS info.\nYou may see void map !!!");
    },
    {
      desiredAccuracy: 3,
      updateDistance: 10,
      minimumUpdateTime : 1000 * 2
    }
  );
}
exports.trackLocation = trackLocation;
