var Observable = require("data/observable").Observable;

function createViewModel() {
    var viewModel = new Observable();
    var Options = ["Start Game", "My Avatar", "Archivements", "Settings", "Exit"];
    viewModel.MainMenu = Options;
    return viewModel;
}

exports.createViewModel = createViewModel;