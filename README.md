# TalosQuests

## Backend Service
---
### Language **Java**
[![Build Status](https://api.travis-ci.org/DevNewbies/TalosQuests.svg?branch=master)](https://travis-ci.org/DevNewbies/TalosQuests)
[![Coverage Status](https://coveralls.io/repos/github/DevNewbies/TalosQuests/badge.svg)](https://coveralls.io/github/DevNewbies/TalosQuests)
![Version](http://talosquests.devian.gr/badge.svg?type=version&version=1.3.0-build84)

### **Service End Points**
End Point | Methods | Parameters | Request Body | States
--------- | ------- | ---------- | ------------ | ------
/ | GET |  | | 200, 504
/Auth | POST | | AuthRegisterModel | 200, 400, 403, 500
/Register | POST | | AuthRegisterModel | 200, 400, 500
/User | GET | token | | 200, 401
/User | DELETE | token, password | | 200, 401, 500
/User | PUT | token, password | AuthRegisterModel | 200, 400, 401, 500
/Session | GET | token | | 200, 401, 500
/Game | GET | token | | 200, 401
/Game | DELETE | token | | 200, 401, 403, 500
/Game/{id} | GET | token | | 200, 401, 404
/Game/{id} | DELETE | token | | 200, 401, 403, 404
/Game/Create | POST | token | LatLng | 200, 400, 401, 404, 500, 504
/Game/Continue/{id} | GET | token | | 200, 401, 404
/Game/Active | GET | token | | 200, 401, 404
/Game/Quest | GET | token | | 200, 401, 404
/Game/Quest/Active | GET | token | | 200, 401, 404
/Game/Quest/Complete | GET | token | | 200, 401, 404
/Game/Quest/Incomplete | GET | token | | 200, 401, 404
/Game/Quest/Next | GET | token | | 200, 401, 404
/Game/Quest/SubmitAnswer | POST | token | QuestChoice | 200, 401, 404
/Admin/Quest | GET | token | | 200, 401, 403, 404
/Admin/Quest | POST | token | QuestModel | 200, 400, 401, 403, 404, 500
/Admin/Quest | DELETE | token, password | | 200, 401, 403, 500
/Admin/Quest/{id} | GET | token | | 200, 401, 403
/Admin/Quest/{id} | PUT | token | QuestModel | 200, 400, 401, 403, 500
/Admin/Quest/{id} | DELETE | token | | 200, 401, 403, 500
/Admin/User | GET | token | | 200, 401, 403
/Admin/User | DELETE | token, password | | 200, 401, 403, 500
/Admin/User/{id} | GET | token | | 200, 401, 403, 500
/Admin/User/{id} | DELETE | token | | 200, 401, 403, 500
/Admin/User/{id} | PUT | token | AuthRegisterModel | 200, 401, 403, 500
/Admin/User/SetBannedState/{id} | GET | token, ban | | 200, 400, 401, 403




## Backend Service Administration Panel
---
### Language C# #
[![Build Status](https://ci.appveyor.com/api/projects/status/suysvxkqmijayb6f?svg=true)](https://ci.appveyor.com/project/ProIcons/talosquests-ilq7l/)

TalosQuests is an RPG Quest Game for Mobile Phones on the Real World.

#### By DevNewbies Team.
