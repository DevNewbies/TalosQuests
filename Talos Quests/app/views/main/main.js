// Init View
var Observable = require("data/observable").Observable;
var frameModule = require("ui/frame");
var cookies = require("../../shared/view-models/cookies");
var CurrentPage;
var Status = "JustStarted";
exports.Status = Status;

// Define Menu's Data
var Menu = new Observable({
    MenuItems : ["Continue to game", "My Avatar", "Archivements", "Settings", "Log out", "Exit"]
});

// Unit Functions
function onNavigatingTo(args) {
    CurrentPage = args.object;
    CurrentPage.bindingContext = Menu;
}
exports.onNavigatingTo = onNavigatingTo;

function RunTrigger(IndexOf) {
    switch(IndexOf) {
        case 0: {
            Status = "Continue to game";
            alert(Status);
            StartGameProcess();
            break;
        }
        case 1: {
            Status = "My Avatar";
            alert(Status);
            MyAvatarProcess();
            break;
        }
        case 2: {
            Status = "Achievements";
            alert(Status);
            AchievementsProcess();
            break;
        }
        case 3: {
            Status = "Settings";
            alert(Status);
            SettingsProcess();
            break;
        }
        case 4: {
            Status= "Log Out";
            logOut();
            break;
        }
        case 5: {
            Status = "Exit";
            alert(Status);
            ExitProcess();
            break;
        }
        default: {
            Status = "Default";
            alert(Status);
            break;
        }
    }
    exports.Status = Status;
}
exports.RunTrigger = RunTrigger;

function StartGameProcess() {
    frameModule.topmost().navigate("views/maps/maps");
}

function MyAvatarProcess() {
	// topmost.navigate("views/myavatar/myavatar");
}

function AchievementsProcess() {
	// topmost.navigate("views/achievements/achievements");
}

function SettingsProcess() {
	// topmost.navigate("views/settings/settings");
}
function logOut(){
    cookies.deleteCookie();
    frameModule.topmost().navigate("views/login/login");
}
function ExitProcess() {
	android.os.Process.killProcess(android.os.Process.myPid());
}

// UI Functions
function MainMenuTap(TappedItem) {
  RunTrigger(TappedItem.index);
}
exports.MainMenuTap = MainMenuTap;
