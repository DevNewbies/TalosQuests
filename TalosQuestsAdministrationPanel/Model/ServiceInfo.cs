using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TalosQuestsAdministrationPanel.Model
{
    public class ServiceInfo
    {
        public String version { get; set; }
        public String buildNumber { get; set; }
        public Boolean online { get; set; }
        public String remoteAddr { get; set; }
        public long uptimeMilliseconds { get; set; }
        public string uptime { get; set; }
    }
}
