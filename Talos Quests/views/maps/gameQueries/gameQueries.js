var UserViewModel = require("../../../shared/view-models/user-view-model");
var user = new UserViewModel();
var quest = require("../questQueries/questQueries");
function getGame(){
  return user.getGame().catch(function(error){
        gameCreateErrorHandling(error);
        return Promise.reject();
  }).then(function(data){
         return data.response;
  });
}
function createGame(lat,lng){
      user.gameCreate(lat,lng).catch(function(error)
      {
          gameCreateErrorHandling(error);
          return Promise.reject();
      }).then(function(data){
          alert("The game created succesfully.");
      });
}
function deleteGame(id){
  user.deleteGame(id).catch(function(error)
      {
          gameDeleteErrorHandling(error);
          return Promise.reject();
      }).then(function(){
          alert("Game removed.");
      });
}
function activeGame(){
  user.activeGame().catch(function(error)
      {
          gameCreateErrorHandling(error);
          return Promise.reject();
      });
}
function continueGame(id){
  user.continueGame(id).catch(function(error)
      {
          gameCreateErrorHandling(error);
          return Promise.reject();
      }).then(function(data){
          alert("The game continues.");
      });
}
function gameDeleteErrorHandling(error){
    if(error==404){
    alert("Error: User doesn't have any active game to delete.");
    }else if(error ==401){
    alert("Error: You need to login first to access this private resource.");
    }
}
function gameActiveErrorHandling(error){
    if(error==404){
    alert("Error: User doesn't have any active game.");
    }else if(error ==401){
    alert("Error: You need to login first to access this private resource.");
    }
}
function gameCreateErrorHandling(error){
  if(error==404){
    alert("Error: Location Not Provided. Game cannot be created.");
  }else if(error ==401){
    alert("Error: You need to login first to access this private resource.");
  }else if(error ==503){
    alert("Error:No available quests found for your Location.");
  }else if(error == 500){
        alert("Error: Bad Request. Missed location.");
  }
}
exports.activeGame = activeGame;
exports.getGame = getGame;
exports.createGame = createGame;
exports.deleteGame = deleteGame;