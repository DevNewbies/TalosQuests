using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TalosQuestsAdministrationPanel.Model
{
    public class Quest
    {
        public long id { get; set; }

        public String name { get; set; }
        public String content { get; set; }
        public int exp { get; set; }

        public LatLng location { get; set; }


        public QuestChoice correctChoice { get; set; }

        public List<QuestChoice> availableChoices { get; set; }
    }
}
