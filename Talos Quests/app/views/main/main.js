var createViewModel = require("./main-view-model").createViewModel;

function onNavigatingTo(args) {
    var page = args.object;
    page.bindingContext = createViewModel();
}

function MainMenuTap (args) {
    var Selected = GetSelectedItem(args);
    switch(Selected) {
    	case 0: {
    		alert("Start Game - Triggered");
    		StartGameProcess();
    		break;
    	}
    	case 1: {
    		alert("My Avatar - Triggered");
    		MyAvatarProcess();
    		break;
    	}
    	case 2: {
    		alert("Achievements - Triggered");
    		AchievementsProcess();
    		break;
    	}
    	case 3: {
    		alert("Settings - Triggered");
    		SettingsProcess();
    		break;
    	}
    	case 4: {
    		ExitProcess();
    		break;
    	}
    }
};

function GetSelectedItem(args) {
	return args.index;
}

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

exports.onNavigatingTo = onNavigatingTo;
exports.MainMenuTap = MainMenuTap;