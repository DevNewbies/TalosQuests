using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TalosQuestsAdministrationPanel.Model
{
    public class Game
    {
        public long id { get; set; }
        public User user { get; set; }
        public UserQuest activeQuest { get; set; }
        public int experiencePoints { get; set; }
        public Boolean active { get; set; }
        public List<UserQuest> completedQuests { get; set; }
        public List<UserQuest> incompleteQuests { get; set; }
    }
}
