using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TalosQuestsAdministrationPanel.Model
{
    public class Session
    {
        public long sessionId { get; set; }

        public User user { get; set; }

        public String token { get; set; }

        public long expires { get; set; }
    }
}
