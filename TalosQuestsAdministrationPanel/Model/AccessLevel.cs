using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TalosQuestsAdministrationPanel.Model
{
    public class AccessLevel
    {
        public long id { get; set; }
        public String name { get; set; }
        public Boolean canManageOwnData { get; set; }
        public Boolean canManageUsers { get; set; }
        public Boolean canManageService { get; set; }
        public Boolean canManageQuests { get; set; }
        public Boolean canWipeUsers { get; set; }
        public Boolean canWipeGames { get; set; }
        public Boolean canWipeQuests { get; set; }
        public Boolean canBanUsers { get; set; }
        public Boolean canWipeSessions { get; set; }
        public Boolean canManageGames { get; set; }
    }
}
