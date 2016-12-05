var createViewModel = require("./main-view-model").createViewModel;

var Status = "Null";
exports.Status = Status;

function onNavigatingTo(args) {
    var page = args.object;
    page.bindingContext = createViewModel();
}
exports.onNavigatingTo = onNavigatingTo;

function RunTrigger(IndexOf) {
    switch(IndexOf) {
        case 0: {
            Status = "Start Game";
            alert("Start Game - Triggered");
            StartGameProcess();
            break;
        }
        case 1: {
            Status = "My Avatar";
            alert("My Avatar - Triggered");
            MyAvatarProcess();
            break;
        }
        case 2: {
            Status = "Achievements";
            alert("Achievements - Triggered");
            AchievementsProcess();
            break;
        }
        case 3: {
            Status = "Settings";
            alert("Settings - Triggered");
            SettingsProcess();
            break;
        }
        case 4: {
            Status = "Exit";
            ExitProcess();
            break;
        }
        default: {
            Status = "Default";
            break;
        }
    }
    exports.Status = Status;
}
exports.RunTrigger = RunTrigger;


function StartGameProcess() {
	// topmost.navigate("views/startgame/startgame");
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

function ExitProcess() {
	android.os.Process.killProcess(android.os.Process.myPid());
}

function MainMenuTap (TappedItem) {
    RunTrigger(TappedItem.index)
}
exports.MainMenuTap = MainMenuTap;