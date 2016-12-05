var Observable = require("data/observable").Observable;

function createViewModel() {
    var viewModel = new Observable();

    var MainMenu = ["Start Game", "My Avatar", "Archivements", "Settings", "Exit"];
    viewModel.MainMenu = MainMenu;

    return viewModel;
}

exports.createViewModel = createViewModel;