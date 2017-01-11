using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TalosQuestsAdministrationPanel.Model
{
    public class User
    {
        public long id { get; set; }
        public List<Game> games { get; set; }
        public String userName { get; set; }
        public String passWord { get; set; }
        public String email { get; set; }
        public String salt { get; set; }
        public Game activeGame { get; set; }
        public String accessLevel { get; set; }
        public Boolean banned { get; set; }
        public String deviceIMEI { get; set; }
        public LatLng lastLocation { get; set; }
        public AccessLevel access { get; set; }
    }
}