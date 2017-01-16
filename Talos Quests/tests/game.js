var game = require("../views/maps/gameQueries/gameQueries");
var config = require("../shared/config");
config.token = "j2ndrnkikh8mgn74mdab7l9kco";


QUnit.test("Success game create.", function(assert) {
	assert.expect(1);
	var done = assert.async(1);
	var lat = 40.6760444;
  	var lng = 22.9126758;
	game.user.gameCreate(lat,lng).then(function(response){
		console.log(response.message);
		assert.equal(game.gameCreateErrorHandling(response.state),200,"Success game create!");
		done();
	});
});
QUnit.test("Fail game create:No available quests found for your location.", function(assert) {
	assert.expect(1);
	var done = assert.async(1);
	var lat = 70.6760444;
  	var lng = 29.9126758;
	game.user.gameCreate(lat,lng).then(function(response){
		console.log(response.message);
		assert.equal(game.gameCreateErrorHandling(response.state),503,"Fail game create:No available quests found for your location!");
		done();
	});
});
QUnit.test("Fail game create: No location given.", function(assert) {
	assert.expect(1);
	var done = assert.async(1);
	game.user.gameCreate().then(function(response){
		console.log(response.message);
		assert.equal(game.gameCreateErrorHandling(response.state),404,"No location given!");
		done();
	});
});
QUnit.test("Fail game create:No token given.", function(assert) {
	assert.expect(1);
	var done = assert.async(1);
	var lat = 40.6760444;
  	var lng = 22.9126758;
  	config.token="";
	game.user.gameCreate(lat,lng).then(function(response){
		console.log(response.message);
		assert.equal(game.gameCreateErrorHandling(response.state),401,"Fail game create:No token given!");
		done();
	});
});
config.token = "flh3d94vsjpho5p84gf0n6ri1d";
QUnit.test("Get game succesfully.", function(assert) {
	assert.expect(1);
	var done = assert.async(1);
	game.user.getGame().then(function(response){
		assert.equal(game.gameCreateErrorHandling(response.state),200,"Get game succesfully!");
		done();
	});
});
QUnit.test("Get game without success.", function(assert) {
	assert.expect(1);
	var done = assert.async(1);
	config.token="";
	game.user.getGame().then(function(response){
		assert.equal(game.gameCreateErrorHandling(response.state),401,"Get game without success.");
		done();
	});
});
config.token = "flh3d94vsjpho5p84gf0n6ri1d";
QUnit.test("Continue game succesfully.", function(assert) {
	assert.expect(1);
	var done = assert.async(1);
	game.user.continueGame().then(function(response){
		assert.equal(game.gameContinueErrorHandling(response.state),200,"Continue game succesfully.");
		done();
	});
});