var reg = require("../views/register/register");

QUnit.test("Already existed account register Test:", function(assert) {
	assert.expect(1);
	var done = assert.async(1);
	reg.user.username="Antonis";
	reg.user.email="tonymis.16@hotmail.com";
	reg.user.password="Test1234@";
	reg.user.imei="000000000000000";
	reg.user.register().then(function(data){
		assert.equal(reg.errorHandling(data.state),302,"Already existed account.");
		done();
	});
});
QUnit.test("Not valid username, register Test:", function(assert) {
	assert.expect(1);
	reg.user.username="dik";
	reg.user.email="asdggg@hotmail.com";
	reg.user.password="Test1234@";
	reg.user.imei="000000000000000";
	assert.equal(reg.validator(),0,"Not valid username.");
	console.log(reg.validator());
});
QUnit.test("Not valid email, register Test:", function(assert) {
	assert.expect(1);
	reg.user.username="Panagiotis";
	reg.user.email="astm";
	reg.user.password="Test1234567!";
	reg.user.imei="000000000000000";
	assert.equal(reg.validator(),2,"Not valid email.");
	console.log(reg.validator());
});
QUnit.test("Not valid password, register Test:", function(assert) {
	assert.expect(1);
	reg.user.username="Panagiotis";
	reg.user.email="asd@hotmail.com";
	reg.user.password="1235";
	reg.user.imei="000000000000000";
	assert.equal(reg.validator(),1,"Not valid password!");
	console.log(reg.validator());
});
QUnit.test("Valid username/password/email for registration Test:", function(assert) {
	assert.expect(1);
	reg.user.username="Panagiotis";
	reg.user.email="asdefg@hotmail.com";
	reg.user.password="Test12345#";
	reg.user.imei="000000000000002";
	assert.equal(reg.validator(),true,"Valid Registration!");
	console.log(reg.validator());
});
QUnit.test("Not valid imei register Test:", function(assert) {
	assert.expect(1);
	var done = assert.async(1);
	reg.user.username="Panagiotis";
	reg.user.email="asd@hotmail.com";
	reg.user.password="6939714716";
	reg.user.imei="00000000000";
	reg.user.register().then(function(data){
		console.log(data.state);
		assert.equal(reg.errorHandling(data.state),400,"Not valid imei!");
		done();
});
});