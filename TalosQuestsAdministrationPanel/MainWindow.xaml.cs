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
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Windows.Threading;
using MahApps.Metro.Controls;
using MahApps.Metro.Controls.Dialogs;
using TalosQuestsAdministrationPanel.Model;
using Timer = System.Timers.Timer;

namespace TalosQuestsAdministrationPanel
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : MetroWindow
    {
        private ProgressDialogController MainDialogController = null;
        public MainWindow()
        {
            InitializeComponent();
            Timer timer = new Timer(1000);
            timer.AutoReset = true;
            timer.Elapsed += (sender, args) =>
            {
                TimeSpan ts = TimeSpan.FromMilliseconds(TalosQuests.Instance.Uptime);
                Dispatcher.Invoke(DispatcherPriority.Normal, new Action(() => { serverUptime.Text = ts.ToString(@"hh\:mm\:ss"); }));

            };
            timer.Start();

            clientAddress.Text = TalosQuests.Instance.RemoteAddr;
            serverVersion.Text = TalosQuests.Instance.Version;
            serverAddress.Text = TalosQuests.Address + ":" + TalosQuests.Port;

            clientUsername.Text = TalosQuests.Instance.User.userName;
            clientEmail.Text = TalosQuests.Instance.User.email;
            clientAccessRole.Text = TalosQuests.Instance.User.accessLevel;

            clientCanBanUsers.IsChecked = TalosQuests.Instance.User.access.canBanUsers;
            clientCanManageUsers.IsChecked = TalosQuests.Instance.User.access.canManageUsers;
            clientCanWipeUsers.IsChecked = TalosQuests.Instance.User.access.canWipeUsers;
            clientCanManageOwnData.IsChecked = TalosQuests.Instance.User.access.canManageOwnData;
            clientCanManageService.IsChecked = TalosQuests.Instance.User.access.canManageService;
            clientCanManageQuests.IsChecked = TalosQuests.Instance.User.access.canManageQuests;
            clientCanWipeQuests.IsChecked = TalosQuests.Instance.User.access.canWipeQuests;
            clientCanWipeGames.IsChecked = TalosQuests.Instance.User.access.canWipeGames;

            userGrid.SelectionMode = DataGridSelectionMode.Single;
            userGrid.SelectionUnit = DataGridSelectionUnit.FullRow;
            userGrid.CanUserAddRows = false;
            userGrid.ItemsSource = TalosQuests.Instance.Users;

            userGrid.SelectionChanged += (sender, args) =>
            {
                if (userGrid.SelectedIndex >= 0)
                {
                    clearUserTab();
                    var usr = (User)userGrid.SelectedItem;
                    userUsername.Text = usr.userName;
                    userEmail.Text = usr.email;
                    userIMEI.Text = usr.deviceIMEI;
                    userRole.Text = usr.accessLevel;

                    userCanBanUsers.IsChecked = usr.access.canBanUsers;
                    userCanManageOwnData.IsChecked = usr.access.canManageOwnData;
                    userCanManageQuests.IsChecked = usr.access.canManageQuests;
                    userCanManageService.IsChecked = usr.access.canManageService;
                    userCanManageUsers.IsChecked = usr.access.canManageUsers;
                    userCanWipeGames.IsChecked = usr.access.canWipeGames;
                    userCanWipeQuests.IsChecked = usr.access.canWipeQuests;
                    userCanWipeUsers.IsChecked = usr.access.canWipeUsers;
                    userBanned.IsChecked = usr.banned;

                    userBan.Content = usr.banned ? "Unban" : "Ban";

                    if (usr.userName.Equals(TalosQuests.Instance.User.userName)) return;
                    if (usr.access.canManageService && !TalosQuests.Instance.User.access.canManageService) return;

                    userBan.IsEnabled = TalosQuests.Instance.User.access.canBanUsers;
                    userDelete.IsEnabled = TalosQuests.Instance.User.access.canManageUsers;
                }
                else
                {
                    clearUserTab();
                }

            };

        }

        private void clearUserTab()
        {
            userUsername.Text = String.Empty;
            userEmail.Text = String.Empty;
            userIMEI.Text = String.Empty;
            userRole.Text = String.Empty;
            userPassword.Password = String.Empty;

            userBan.Content = "Ban";
            userCanBanUsers.IsChecked = false;
            userCanManageOwnData.IsChecked = false;
            userCanManageQuests.IsChecked = false;
            userCanManageService.IsChecked = false;
            userCanManageUsers.IsChecked = false;
            userCanWipeGames.IsChecked = false;
            userCanWipeQuests.IsChecked = false;
            userCanWipeUsers.IsChecked = false;
            userBan.IsEnabled = false;
            userDelete.IsEnabled = false;

        }
        private void UserGrid_OnMouseUp(object sender, MouseButtonEventArgs e)
        {
            var grid = sender as DataGrid;
            if (grid?.SelectedItems == null || grid.SelectedItems.Count != 1) return;
            var dgr = grid.ItemContainerGenerator.ContainerFromItem(grid.SelectedItem) as DataGridRow;
            if (dgr != null && !dgr.IsMouseOver)
            {
                ((DataGridRow)dgr).IsSelected = false;
            }
        }

        private async void UserBan_OnClick(object sender, RoutedEventArgs e)
        {
            if (userGrid.SelectedIndex < 0) return;

            var usr = (User)userGrid.SelectedItem;
            userBan.IsEnabled = false;
            userDelete.IsEnabled = false;

            MetroDialogOptions.ColorScheme = MetroDialogColorScheme.Accented;
            await Dispatcher.InvokeAsync(new Action(async () =>
            {
                MainDialogController =
                    await this.ShowProgressAsync("Changing Ban State", "Please Wait...");
                MainDialogController.SetIndeterminate();
                try
                {
                    var state = await TalosQuests.Instance.SetBannedState(usr, !usr.banned);
                    await Task.Run(() =>
                    {
                        Dispatcher.Invoke(DispatcherPriority.Normal, new Action(() =>
                        {
                            if (state)
                            {
                                userBanned.IsChecked = usr.banned;
                                userGrid.Items.Refresh();
                            }
                            userBan.Content = usr.banned ? "Unban" : "Ban";
                            MainDialogController.SetMessage(usr.banned ? "User Banned." : "Ban Removed from User.");


                        }));
                        Thread.Sleep(1500);

                    });
                    await MainDialogController.CloseAsync();
                }
                catch (TalosQuestsException exc)
                {
                    MainDialogController.SetMessage("Error: \n" + exc.Message);
                    MainDialogController.SetCancelable(true);
                    MainDialogController.Canceled += (o, args) => MainDialogController.CloseAsync();

                }
                userBan.IsEnabled = true;
                userDelete.IsEnabled = TalosQuests.Instance.User.access.canManageUsers;

            }));



        }


        private async void UserDelete_OnClick(object sender, RoutedEventArgs e)
        {
            if (userGrid.SelectedIndex < 0) return;

            var usr = (User)userGrid.SelectedItem;
            userBan.IsEnabled = false;
            userDelete.IsEnabled = false;
            MetroDialogOptions.ColorScheme = MetroDialogColorScheme.Accented;
            await Dispatcher.InvokeAsync(new Action(async () =>
            {
                MainDialogController =
                    await this.ShowProgressAsync("Deleting User", "Please Wait...");
                MainDialogController.SetIndeterminate();
                try
                {
                    var state = await TalosQuests.Instance.DeleteUser(usr);
                    await Task.Run(() =>
                    {
                        Dispatcher.Invoke(DispatcherPriority.Normal, new Action(() =>
                        {
                            if (state)
                            {
                                TalosQuests.Instance.Users.Remove(usr);
                                userGrid.Items.Refresh();
                                MainDialogController.SetMessage("User Deleted.");
                            }

                        }));
                        Thread.Sleep(1500);
                    });
                    await MainDialogController.CloseAsync();
                }
                catch (TalosQuestsException exc)
                {
                    MainDialogController.SetMessage("Error: \n" + exc.Message);
                    MainDialogController.SetCancelable(true);
                    MainDialogController.Canceled += (o, args) => MainDialogController.CloseAsync();
                }
            }));
        }

        private async void Button_OnClick(object sender, RoutedEventArgs e)
        {
            MetroDialogOptions.ColorScheme = MetroDialogColorScheme.Accented;
            await Dispatcher.InvokeAsync(new Action(async () =>
            {
                MainDialogController =
                    await this.ShowProgressAsync("Searching Users", "Please Wait...");
                MainDialogController.SetIndeterminate();
                try
                {
                    await TalosQuests.Instance.SearchUsers(searchUserField.Text);
                    await Task.Run(() =>
                    {
                        Dispatcher.Invoke(DispatcherPriority.Normal, new Action(() =>
                        {
                            userGrid.ItemsSource = null;
                            userGrid.Items.Refresh();
                            userGrid.ItemsSource = TalosQuests.Instance.Users;
                            userGrid.Items.Refresh();
                        }));
                    });
                    await MainDialogController.CloseAsync();
                }
                catch (TalosQuestsException exc)
                {
                    MainDialogController.SetMessage("Error: \n" + exc.Message);
                    MainDialogController.SetCancelable(true);
                    MainDialogController.Canceled += (o, args) => MainDialogController.CloseAsync();

                }
            }));
        }
    }
}
