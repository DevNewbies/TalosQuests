# TalosQuests

## Backend Service
---
### Language **Java**
[![Build Status](https://api.travis-ci.org/DevNewbies/TalosQuests.svg?branch=master)](https://travis-ci.org/DevNewbies/TalosQuests)
[![Coverage Status](https://coveralls.io/repos/github/DevNewbies/TalosQuests/badge.svg?branch=master)](https://coveralls.io/github/DevNewbies/TalosQuests)
![Version](http://talosquests.devian.gr/badge.svg?type=version&nocache=5)

### **Service End Points**
End Point | Methods | Parameters | Request Body | States
--------- | ------- | ---------- | ------------ | ------
/ | GET |  | | 200, 504
/Auth | POST | | AuthRegisterModel | 200, 400, 415, 500
/Register | POST | | AuthRegisterModel | 200, 400, 500
/User | GET | Token | | 200, 401
/User | DELETE | Token, Password | | 200, 401, 500
/User | PUT | Token, Password | AuthRegisterModel | 200, 400, 401, 500
/Session | GET | Token | | 200, 401, 500
/Game | GET | Token | | 200, 401
/Game | DELETE | Token | | 200, 401, 415, 500
/Game/{id} | GET | Token | | 200, 401, 404
/Game/{id} | DELETE | Token | | 200, 401, 404, 415
/Game/Create | POST | Token | LatLng | 200, 400, 401, 404, 500, 504
/Game/Continue/{id} | GET | Token | | 200, 401, 404
/Game/Active | GET | Token | | 200, 401, 404
/Game/Quest | GET | Token | | 200, 401, 404
/Game/Quest/Active | GET | Token | | 200, 401, 404
/Game/Quest/Complete | GET | Token | | 200, 401, 404
/Game/Quest/Incomplete | GET | Token | | 200, 401, 404
/Game/Quest/Next | GET | Token | | 200, 401, 404
/Game/Quest/SubmitAnswer | POST | Token | QuestChoice | 200, 401, 404
/Admin/Quest | GET | Token | | 200, 401, 404, 415
/Admin/Quest | POST | Token | QuestModel | 200, 400, 401, 404, 415, 500
/Admin/Quest | DELETE | Token, Password | | 200, 401, 415, 500
/Admin/Quest/{id} | GET | Token | | 200, 401, 404, 415
/Admin/Quest/{id} | PUT | Token | QuestModel | 200, 400, 401, 415, 500
/Admin/Quest/{id} | DELETE | Token | | 200, 401, 415, 500
/Admin/User | GET | Token | | 200, 401, 415
/Admin/User | DELETE | Token, Password | | 200, 401, 415, 500
/Admin/User/{id} | GET | Token | | 200, 401, 415, 500
/Admin/User/{id} | DELETE | Token | | 200, 401, 415, 500
/Admin/User/{id} | PUT | Token | AuthRegisterModel | 200, 401, 415, 500
/Admin/User/SetBannedState/{id} | GET | Token, State | | 200, 400, 401, 415




## Backend Service Administration Panel
---
### Language C# #
[![Build Status](https://ci.appveyor.com/api/projects/status/suysvxkqmijayb6f?svg=true)](https://ci.appveyor.com/project/ProIcons/talosquests-ilq7l/)

TalosQuests is an RPG Quest Game for Mobile Phones on the Real World.

#### By DevNewbies Team.
