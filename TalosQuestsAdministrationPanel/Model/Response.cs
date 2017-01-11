using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TalosQuestsAdministrationPanel.Model
{
    public class Response<T>
    {
        public int state { get; set; }


        public String message { get; set; }


        public long timestamp { get; set; }


        public T response { get; set; }

        public String view { get; set; }
    }
}
