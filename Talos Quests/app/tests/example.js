var CurrentView = require("./views/main/main");

QUnit.test("Test #1 - Just Open App", function (assert) {
	Assert(CurrentView.createViewModel.Status, "Null", "Just Open App, Success");
});

QUnit.test("Test #2 - Select Item (My Avatar)", function (assert) {
	CurrentView.RunTrigger(1);
	Assert(CurrentView.createViewModel.Status, "Null", "Select Item (My Avatar), Success");
});

QUnit.test("Test #3 - False Item", function (assert) {
	CurrentView.RunTrigger(10);
	Assert(CurrentView.createViewModel.Status, "Default", "False Item, Success");
});