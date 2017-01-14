var Observable = require("data/observable").Observable;
var CurrentPage;

var Settings = new Observable({
  SoundEffectsEnabled : true,
  MusicEnabled : true
});

function onNavigatingTo(args) {
    CurrentPage = args.object;
    CurrentPage.bindingContext = Settings;
}
exports.onNavigatingTo = onNavigatingTo;
