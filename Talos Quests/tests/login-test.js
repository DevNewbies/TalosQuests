var log = require("../views/login/login");

QUnit.test("Login Test 1:", function(assert) {
	assert.expect(1);
	var done = assert.async(1);
	log.user.username="asd";
	log.user.password="asd";
	log.user.login().then(function(data){
		assert.equal(log.errorHandling(data.state),401,"username:'asd' password:'asd'!");
		done();
	});
});
QUnit.test("Login Test 2:", function(assert) {
	assert.expect(1);
	var done = assert.async(1);
	log.user.username="";
	log.user.password="";
	log.user.login().then(function(data){
		assert.equal(log.errorHandling(data.state),400,"username:'' password:''!");
		done();
});
});
QUnit.test("Login Test 3:", function(assert) {
	assert.expect(1);
	var done = assert.async(1);
	log.user.username="Antonis";
	log.user.password="Test1234@";
	log.user.login().then(function(data){
		assert.equal(data.state,200,"username:'Antonis' password:'Test1234@'!");
		done();
});
});
QUnit.test("Login With non-exist Cookie!", function(assert) {
	assert.expect(1);
	var done = assert.async(1);
	log.sign().then(function(data){
		assert.equal(data,0,"Login With non-exist Cookie!");
		done();
});
});
QUnit.test("Create Cookie!", function(assert) {
	assert.expect(1);
	var done = assert.async(1);
	var cookie = require("../shared/view-models/cookies");
	cookie.saveCookie("22").then(function(data){
		assert.equal(data,true,"Create Cookie!");
		done();
	});
});
QUnit.test("Update Cookie !", function(assert) {
	assert.expect(1);
	var done = assert.async(1);
	var cookie = require("../shared/view-models/cookies");
	var date = new Date();
    date.setTime(date.getTime() + 68789795342123);
	cookie.saveCookie("j2ndrnkikh8mgn74mdab7l9kco" + "|" + date).then(function(data){
		assert.equal(data,true,"Create Cookie while there is already one!");
		done();
	});
});
QUnit.test("Login With Cookies test!", function(assert) {
	assert.expect(1);
	var done = assert.async(1);
	log.sign().then(function(data){
			assert.equal(data.state,200,"Login with cookie!");
			done();
	});
});
QUnit.test("Delete succesfully Cookie test!", function(assert) {
	assert.expect(1);
	var done = assert.async(1);
	var cookie = require("../shared/view-models/cookies");
	cookie.deleteCookie().then(function(data){
		assert.equal(data,true,"Delete Cookie without success!");
		done();
	});
});
QUnit.test("Delete Cookie without success test!", function(assert) {
	assert.expect(1);
	var done = assert.async(1);
	var cookie = require("../shared/view-models/cookies");
	cookie.deleteCookie().then(function(data){
		assert.equal(data,false,"Delete Cookie without success!");
		done();
	});
});