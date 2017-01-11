using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TalosQuestsAdministrationPanel.Model
{
    public class LatLng
    {
        public double lat { get; set; }
        public double lng { get; set; }

        public override String ToString()
        {
            return $"{lat},{lng}";
        }
    }
}
