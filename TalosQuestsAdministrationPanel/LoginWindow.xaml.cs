using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Windows.Threading;
using MahApps.Metro;
using MahApps.Metro.Controls;
using MahApps.Metro.Controls.Dialogs;
using RestSharp;
using RestSharp.Deserializers;
using TalosQuestsAdministrationPanel.Model;
using Duration = System.Windows.Duration;

namespace TalosQuestsAdministrationPanel
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class LoginWindow : MetroWindow
    {
        private ProgressDialogController AuthDialogController = null;
        public LoginWindow()
        {
            Resources.Add("DownColor", null);
            Resources.Add("UpColor", null);
            InitializeComponent();
            Resources["UpColor"] = (Color)ThemeManager.GetAccent("Blue").Resources["AccentColor"];
            var convertFromString = ColorConverter.ConvertFromString("#333333");
            if (convertFromString != null)
                Resources["DownColor"] = (Color)convertFromString;
        }



        #region UserInterface Methods

        public string RandomResourceKey(int length = 5)
        {
            while (true)
            {
                const string chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
                var random = new Random();

                var z = new string(Enumerable.Repeat(chars, length).Select(s => s[random.Next(s.Length)]).ToArray()) + DateTime.Now.Ticks;
                if (Resources.Contains(z)) continue;
                return z;
            }
        }

        public void AnimateHeader(Color First, Color Second, Double DurationSecs = 0.3)
        {
            var duration = new Duration(TimeSpan.FromSeconds(DurationSecs));
            var firstAnimation = new ColorAnimation();
            var secondAnimation = new ColorAnimation();

            var sb = new Storyboard();
            sb.Children.Add(firstAnimation);
            sb.Children.Add(secondAnimation);
            firstAnimation.Duration = duration;
            firstAnimation.To = First;
            firstAnimation.RepeatBehavior = new RepeatBehavior(1);
            secondAnimation.Duration = duration;
            secondAnimation.To = Second;
            secondAnimation.RepeatBehavior = new RepeatBehavior(1);

            Storyboard.SetTarget(firstAnimation, xHeader);
            Storyboard.SetTargetProperty(firstAnimation, new PropertyPath("Foreground.(GradientBrush.GradientStops)[0].(GradientStop.Color)"));
            Storyboard.SetTarget(secondAnimation, xHeader);
            Storyboard.SetTargetProperty(secondAnimation, new PropertyPath("Foreground.(GradientBrush.GradientStops)[1].(GradientStop.Color)"));
            var rsName = RandomResourceKey();
            Resources.Add(rsName, sb);
            sb.Completed += (o, s) => { Resources.Remove(rsName); };
            sb.Begin();
        }
        private void XHeader_OnMouseEnter(object sender, MouseEventArgs e)
        {
            AnimateHeader((Color)Resources["DownColor"], (Color)Resources["UpColor"]);
        }

        private void XHeader_OnMouseLeave(object sender, MouseEventArgs e)
        {
            AnimateHeader((Color)Resources["UpColor"], (Color)Resources["DownColor"]);
        }
        private void ValidateLogin()
        {
            if (StringEmpty(xPassword.Password) || StringEmpty(xUsername.Text))
                xLogin.IsEnabled = false;
            else
                xLogin.IsEnabled = true;
        }
        private void VerifyLoginUsername(object sender, TextChangedEventArgs e)
        {
            ValidateLogin();
        }
        private void VerifyLoginPassword(object sender, RoutedEventArgs e)
        {
            ValidateLogin();
        }
        private Boolean StringEmpty(String val)
        {
            return String.IsNullOrEmpty(val.Trim()) || String.IsNullOrWhiteSpace(val.Trim());
        }

        private async void XLogin_OnClick(object sender, RoutedEventArgs e)
        {
            this.MetroDialogOptions.ColorScheme = MetroDialogColorScheme.Accented;
            AuthDialogController = await this.ShowProgressAsync("Login", "Please Wait...");
            AuthDialogController.SetIndeterminate();

            var state = await TalosQuests.Instance.Login(xUsername.Text, xPassword.Password);
            if (state)
            {
                AuthDialogController.SetMessage("Welcome.\nLoading Data...");
                await TalosQuests.Instance.FetchInfo();
                await AuthDialogController.CloseAsync();
                MainWindow l = new MainWindow();
                l.Show();
                Close();
            }
            else
            {
                await Task.Run(() =>
                {
                    Dispatcher.Invoke(DispatcherPriority.Normal, new Action(() =>
                    {
                        AuthDialogController.SetMessage(
                            "Either your Credentials are incorrect either your have no access.");
                    }));
                    Thread.Sleep(3000);
                });
                await AuthDialogController.CloseAsync();
            }

        }
    }
    #endregion
}
