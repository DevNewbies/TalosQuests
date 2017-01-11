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

        public DateTime started { get; set; }
        public DateTime completed { get; set; }

        public Duration duration { get; set; }
        public Boolean succeed { get; set; }

        public Boolean active { get; set; }

        public LatLng location { get; set; }

        public QuestModel quest { get; set; }
    }
}
