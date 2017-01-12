var Observable = require("data/observable").Observable;
var CurrentPage;

function onNavigatingTo(args) {
    CurrentPage = args.object;
}
exports.onNavigatingTo = onNavigatingTo;
