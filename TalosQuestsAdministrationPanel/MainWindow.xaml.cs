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
            questMode = QuestMode.Add;
            questChoiceMode = QuestMode.Add;
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
            clientCanWipeSessions.IsChecked = TalosQuests.Instance.User.access.canWipeSessions;

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
                    userCanWipeSessions.IsChecked = usr.access.canWipeSessions;
                    userBanned.IsChecked = usr.banned;
                    wipeUsers.IsEnabled = usr.access.canWipeUsers;
                    wipeQuests.IsEnabled = usr.access.canWipeQuests;
                    wipeSessions.IsEnabled = usr.access.canWipeSessions;

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

            questGrid.SelectionMode = DataGridSelectionMode.Single;
            questGrid.SelectionUnit = DataGridSelectionUnit.FullRow;
            questGrid.CanUserAddRows = false;
            questGrid.ItemsSource = TalosQuests.Instance.Quests;
            questGrid.SelectionChanged += (sender, args) =>
            {
                if (questGrid.SelectedIndex >= 0)
                {
                    questMode = QuestMode.Update;
                    questName.IsReadOnly = true;
                    resetQuestFields();

                    var quest = ((Quest)questGrid.SelectedItem);
                    questName.Text = quest.name;
                    questContent.Text = quest.content;
                    Choices = quest.availableChoices;
                    questChoices.ItemsSource = Choices;
                    questCorrectChoice.ItemsSource = Choices;
                    int index = 0;
                    foreach (QuestChoice choice in Choices)
                    {
                        if (choice.content.Equals(quest.correctChoice.content))
                        {
                            questCorrectChoice.SelectedIndex = index;
                        }
                        index++;
                    }
                    questExp.Text = quest.exp + "";
                    questLat.Text = quest.location.lat + "";
                    questLng.Text = quest.location.lng + "";
                }
                else
                {
                    questMode = QuestMode.Add;
                    questName.IsReadOnly = false;
                    resetQuestFields();
                }
            };

            questChoices.ItemsSource = Choices;
            questCorrectChoice.ItemsSource = Choices;

            questChoices.SelectionChanged += (sender, args) =>
            {
                if (questChoices.SelectedIndex >= 0)
                {
                    questChoiceMode = QuestMode.Update;
                    questChoiceDelete.IsEnabled = true;
                    questChoiceAdd.IsEnabled = false;
                    questChoiceContent.Text = ((QuestChoice)questChoices.SelectedItem).content;

                }
                else
                {
                    questChoiceMode = QuestMode.Add;
                    questChoiceDelete.IsEnabled = false;
                    questChoiceAdd.IsEnabled = false;
                    questChoiceUpdate.IsEnabled = false;
                    questChoiceContent.Text = String.Empty;

                }
            };

        }

        private List<QuestChoice> Choices = new List<QuestChoice>();

        public Quest getQuestModel()
        {
            if (validateQuest())
            {
                Quest model = new Quest();
                model.content = questContent.Text;
                model.exp = Int32.Parse(questExp.Text);
                model.location = new LatLng() { lat = Double.Parse(questLat.Text.Replace(".",",")), lng = Double.Parse(questLng.Text.Replace(".", ",")) };
                model.name = questName.Text;
                model.availableChoices = Choices;
                model.correctChoice = (QuestChoice)questCorrectChoice.SelectedItem;
                return model;
            }
            return null;
        }

        private void resetQuestFields()
        {
            Choices = new List<QuestChoice>();
            questChoices.ItemsSource = Choices;
            questName.Text = String.Empty;
            questExp.Text = String.Empty;
            questLat.Text = String.Empty;
            questLng.Text = String.Empty;
            questChoiceContent.Text = String.Empty;
            questContent.Text = String.Empty;
            questCorrectChoice.ItemsSource = Choices;
        }

        enum QuestMode
        {
            Add,
            Update
        }



        private QuestMode questMode;
        private QuestMode questChoiceMode;

        private void QuestValidationFields(object sender, TextChangedEventArgs e)
        {
            validateQuest();
        }

        private Boolean validateQuest()
        {
            int intTmp = 0;
            double doubleTmp = 0D;
            var state = !String.IsNullOrEmpty(questContent.Text) &&
                        !questChoices.Items.IsEmpty &&
                        Int32.TryParse(questExp.Text, out intTmp) &&
                        Double.TryParse(questLat.Text, out doubleTmp) &&
                        Double.TryParse(questLng.Text, out doubleTmp) &&
                        !String.IsNullOrEmpty(questName.Text) &&
                        questCorrectChoice.SelectedIndex > -1;

            if (state)
            {

                if (questMode == QuestMode.Add)
                {
                    questAdd.IsEnabled = true;
                    questDelete.IsEnabled = false;
                    questUpdate.IsEnabled = false;
                }
                else
                {
                    questAdd.IsEnabled = false;
                    questDelete.IsEnabled = true;
                    questUpdate.IsEnabled = true;
                }
            }
            else
            {
                questAdd.IsEnabled = false;
                questUpdate.IsEnabled = false;
                questDelete.IsEnabled = questMode == QuestMode.Update;
            }
            return state;
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
            userCanWipeSessions.IsChecked = false;
            userBan.IsEnabled = false;
            userDelete.IsEnabled = false;

        }
        private void Grid_OnMouseUp(object sender, MouseButtonEventArgs e)
        {


            var grid = sender as DataGrid;
            if (grid?.SelectedItems == null || grid.SelectedItems.Count != 1) return;
            var dgr = grid.ItemContainerGenerator.ContainerFromItem(grid.SelectedItem) as DataGridRow;
            if (dgr != null && !dgr.IsMouseOver)
            {
                ((DataGridRow)dgr).IsSelected = false;
            }
        }

        private void List_OnMouseUp(object sender, MouseButtonEventArgs e)
        {

            var grid = sender as ListBox;
            if (grid?.SelectedItems == null || grid.SelectedItems.Count != 1) return;
            var dgr = grid.ItemContainerGenerator.ContainerFromItem(grid.SelectedItem) as ListBoxItem;
            if (dgr != null && !dgr.IsMouseOver)
            {
                ((ListBoxItem)dgr).IsSelected = false;
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


        private void QuestChoiceContent_OnTextChanged(object sender, TextChangedEventArgs e)
        {
            if (questChoiceMode == QuestMode.Update && questChoices.SelectedIndex > -1)
            {
                questChoiceDelete.IsEnabled = true;
                questChoiceAdd.IsEnabled = false;
            }
            if (!String.IsNullOrEmpty(questChoiceContent.Text))
            {
                if (questChoiceMode == QuestMode.Add)
                {
                    questChoiceAdd.IsEnabled = true;
                    questChoiceUpdate.IsEnabled = false;
                    questChoiceDelete.IsEnabled = false;
                }
                else
                {
                    questChoiceAdd.IsEnabled = false;
                    questChoiceUpdate.IsEnabled = true;
                }
            }
            else
            {
                if (questChoiceMode == QuestMode.Add)
                {
                    questChoiceAdd.IsEnabled = false;

                }
            }
        }

        private void questChoiceAdd_Click(object sender, RoutedEventArgs e)
        {
            if (questChoiceMode == QuestMode.Add)
            {
                if (!String.IsNullOrEmpty(questChoiceContent.Text))
                {
                    Choices.Add(new QuestChoice() { content = questChoiceContent.Text });
                    questChoices.Items.Refresh();
                    questCorrectChoice.Items.Refresh();
                    questChoiceContent.Text = String.Empty;
                    validateQuest();
                }

            }
        }

        private void QuestCorrectChoice_OnSelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            validateQuest();
        }

        private void QuestChoiceDelete_OnClick(object sender, RoutedEventArgs e)
        {
            if (questChoiceMode == QuestMode.Update)
            {
                if (questChoices.SelectedIndex > -1)
                {
                    var choice = (QuestChoice)questChoices.SelectedItem;
                    Choices.Remove(choice);
                    questChoices.Items.Refresh();
                    questCorrectChoice.Items.Refresh();
                    questChoiceContent.Text = String.Empty;
                    validateQuest();
                }
            }
        }

        private void QuestChoiceUpdate_OnClick(object sender, RoutedEventArgs e)
        {
            if (questChoiceMode == QuestMode.Update)
            {
                if (questChoices.SelectedIndex > -1)
                {
                    var choice = (QuestChoice)questChoices.SelectedItem;
                    choice.content = questChoiceContent.Text;
                    questChoices.Items.Refresh();
                    questCorrectChoice.Items.Refresh();
                    if (questChoices.SelectedIndex == questCorrectChoice.SelectedIndex)
                    {
                        questCorrectChoice.SelectedIndex = -1;
                        questCorrectChoice.SelectedIndex = questChoices.SelectedIndex;
                    }
                    questChoiceContent.Text = String.Empty;
                    validateQuest();
                }
            }
        }

        private async void questAdd_Click(object sender, RoutedEventArgs e)
        {
            var temp = getQuestModel();
            if (temp != null)
            {

                MetroDialogOptions.ColorScheme = MetroDialogColorScheme.Accented;
                await Dispatcher.InvokeAsync(new Action(async () =>
                {
                    MainDialogController =
                        await this.ShowProgressAsync("Adding Quest", "Please Wait...");
                    MainDialogController.SetIndeterminate();
                    try
                    {
                        var state = await TalosQuests.Instance.AddQuest(temp);
                        await Task.Run(() =>
                        {
                            Dispatcher.Invoke(DispatcherPriority.Normal, new Action(async () =>
                            {
                                if (state)
                                {
                                    MainDialogController.SetMessage("Quest Added. Populating Quests...");
                                    await TalosQuests.Instance.FetchInfo();
                                    questGrid.ItemsSource = null;
                                    questGrid.Items.Refresh();
                                    questGrid.ItemsSource = TalosQuests.Instance.Quests;
                                    questGrid.Items.Refresh();
                                }
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

        private async void questDelete_Click(object sender, RoutedEventArgs e)
        {
            MetroDialogOptions.ColorScheme = MetroDialogColorScheme.Accented;
            await Dispatcher.InvokeAsync(new Action(async () =>
            {
                var quest = ((Quest)questGrid.SelectedItem);
                MainDialogController =
                    await this.ShowProgressAsync("Removing Quest", "Please Wait...");
                MainDialogController.SetIndeterminate();
                try
                {

                    var state = await TalosQuests.Instance.DeleteQuest(quest.id);
                    await Task.Run(() =>
                    {
                        Dispatcher.Invoke(DispatcherPriority.Normal, new Action(async () =>
                        {
                            if (state)
                            {
                                MainDialogController.SetMessage("Quest Removed. Populating Quests...");
                                await TalosQuests.Instance.FetchInfo();
                                questGrid.ItemsSource = null;
                                questGrid.Items.Refresh();
                                questGrid.ItemsSource = TalosQuests.Instance.Quests;
                                questGrid.Items.Refresh();
                            }
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

        private async void questUpdate_Click(object sender, RoutedEventArgs e)
        {
            MetroDialogOptions.ColorScheme = MetroDialogColorScheme.Accented;
            await Dispatcher.InvokeAsync(new Action(async () =>
            {
                var quest = ((Quest)questGrid.SelectedItem);
                MainDialogController =
                    await this.ShowProgressAsync("Updating Quest", "Please Wait...");
                MainDialogController.SetIndeterminate();
                try
                {

                    var state = await TalosQuests.Instance.UpdateQuest(quest.id,getQuestModel());
                    await Task.Run(() =>
                    {
                        Dispatcher.Invoke(DispatcherPriority.Normal, new Action(async () =>
                        {
                            if (state)
                            {
                                MainDialogController.SetMessage("Quest Updated. Populating Quests...");
                                await TalosQuests.Instance.FetchInfo();
                                questGrid.ItemsSource = null;
                                questGrid.Items.Refresh();
                                questGrid.ItemsSource = TalosQuests.Instance.Quests;
                                questGrid.Items.Refresh();
                            }
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

        private async void wipeUsers_Click(object sender, RoutedEventArgs e)
        {
            MetroDialogOptions.ColorScheme = MetroDialogColorScheme.Accented;
            await Dispatcher.InvokeAsync(new Action(async () =>
            {
                MainDialogController =
                    await this.ShowProgressAsync("Wiping Users", "Please Wait...");
                MainDialogController.SetIndeterminate();
                try
                {

                    var state = await TalosQuests.Instance.WipeUsers();
                    await Task.Run(() =>
                    {
                        Dispatcher.Invoke(DispatcherPriority.Normal, new Action(async () =>
                        {
                            if (state)
                            {
                                MainDialogController.SetMessage("Users Wiped.");
                                await TalosQuests.Instance.FetchInfo();
                                TalosQuests.Instance.Users.Clear();
                                userGrid.Items.Refresh();
                            }
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

        private async void wipeSessions_Click(object sender, RoutedEventArgs e)
        {
            MetroDialogOptions.ColorScheme = MetroDialogColorScheme.Accented;
            await Dispatcher.InvokeAsync(new Action(async () =>
            {
                MainDialogController =
                    await this.ShowProgressAsync("Wiping Sessions", "Please Wait...");
                MainDialogController.SetIndeterminate();
                try
                {

                    var state = await TalosQuests.Instance.WipeSessions();
                    await Task.Run(() =>
                    {
                        Dispatcher.Invoke(DispatcherPriority.Normal, new Action(async () =>
                        {
                            if (state)
                            {
                                MainDialogController.SetMessage("Sessions Wiped.");
                                await TalosQuests.Instance.FetchInfo();
                            }
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

        private async void wipeQuests_Click(object sender, RoutedEventArgs e)
        {
            MetroDialogOptions.ColorScheme = MetroDialogColorScheme.Accented;
            await Dispatcher.InvokeAsync(new Action(async () =>
            {
                MainDialogController =
                    await this.ShowProgressAsync("Wiping Quests", "Please Wait...");
                MainDialogController.SetIndeterminate();
                try
                {

                    var state = await TalosQuests.Instance.WipeQuests();
                    await Task.Run(() =>
                    {
                        Dispatcher.Invoke(DispatcherPriority.Normal, new Action(async () =>
                        {
                            if (state)
                            {
                                MainDialogController.SetMessage("Quests Wiped.");
                                await TalosQuests.Instance.FetchInfo();
                            }
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
