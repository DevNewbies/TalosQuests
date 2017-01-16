var UserViewModel = require("../../../shared/view-models/user-view-model");
var user = new UserViewModel();

function getQuest(){
	user.getQuest().catch(function(error){
		getQuestErrorHandling(error);
	}).then(function(quest){
		return quest;
	});
}
function getNextQuest(){
	user.getNextQuest().catch(function(error){
		getQuestErrorHandling(error);
	}).then(function(quest){
        alert("Next Quest");
		return quest;
	});
}
function submitQuest(answer){
	user.questSubmit(answer).catch(function(error){
		getQuestErrorHandling(error);
	}).then(function(quest){
		alert("success");
	});
}
function getQuestErrorHandling(error){
	if(error==404){
    alert("Error: User doesn't have any active game.");
    }else if(error ==401){
    alert("Error: You need to login first to access this private resource.");
    }
}
exports.getNextQuest= getNextQuest;
exports.getQuest= getQuest;
exports.submitQuest= submitQuest;