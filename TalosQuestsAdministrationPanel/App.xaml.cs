using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Linq;
using System.Threading.Tasks;
using System.Windows;
using RestSharp;

namespace TalosQuestsAdministrationPanel
{
    /// <summary>
    /// Interaction logic for App.xaml
    /// </summary>
    public partial class App : Application
    {
        public static RestClient RestClient = new RestClient("http://127.0.0.1:8080/");
    }
}
