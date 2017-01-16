var UserViewModel = require("../../../shared/view-models/user-view-model");
var user = new UserViewModel();
function getGame(){
  return user.getGame().catch(function(error){
        gameCreateErrorHandling(error);
        return Promise.reject();
  }).then(function(data){
        console.dir(data.response);
        return data.status;
  });
}
function createGame(lat,lng){
  var lat = 40.6760444;
  var lng = 22.9126758;
  //console.log("lat " + UserLocation.userLatitude + " log " + UserLocation.userLongitude);
      user.gameCreate(lat,lng).catch(function(error)
      {
          gameCreateErrorHandling(error);
          return Promise.reject();
      }).then(function(data){
          return data.status;
      });
}
function deleteGame(){
  user.activeGame().catch(function(error)
      {
          gameDeleteErrorHandling(error);
          return Promise.reject();
      }).then(function(data){
          return data.status;
      });
}
function continueGame(id){
  user.continueGame(id).catch(function(error)
      {
          gameDeleteErrorHandling(error);
          return Promise.reject();
      }).then(function(data){
          return data.status;
      });
}
function activeGame(){
  user.activeGame().catch(function(error)
      {
          gameCreateErrorHandling(error);
          return Promise.reject();
      }).then(function(data){
          return data.status;
      });
}
function gameContinueErrorHandling(error){
    if(error==404){
        alert("Error: User doesn't have any active game to delete.");
        return 404;
    }else if(error ==401){
        alert("Error: You need to login first to access this private resource.");
        return 401;
    }
}
function gameDeleteErrorHandling(error){
    if(error==404){
        alert("Error: User doesn't have any active game to delete.");
        return 404;
    }else if(error ==401){
        alert("Error: You need to login first to access this private resource.");
        return 401;
    }
}
function gameActiveErrorHandling(error){
    if(error==404){
        alert("Error: User doesn't have any active game.");
        return 404;
    }else if(error ==401){
        alert("Error: You need to login first to access this private resource.");
        return 401;
    }
}
function gameCreateErrorHandling(error){
  if(error==404){
        alert("Error: Location Not Provided. Game cannot be created.");
         return 404;
  }else if(error ==401){
        alert("Error: You need to login first to access this private resource.");
        return 401;
  }else if(error ==503){
        alert("Error:No available quests found for your Location.");
         return 503;
  }else if(error == 500){
        alert("Error: Bad Request. Missed location.");
        return 500;
  }
}
exports.user = user;
exports.activeGame = activeGame;
exports.getGame = getGame;
exports.createGame = createGame;
exports.continueGame = continueGame;
exports.deleteGame = deleteGame;
exports.gameContinueErrorHandling = gameContinueErrorHandling;
exports.gameCreateErrorHandling=gameCreateErrorHandling;
exports.gameDeleteErrorHandling=gameDeleteErrorHandling;
exports.gameActiveErrorHandling=gameActiveErrorHandling;