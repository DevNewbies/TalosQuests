using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading.Tasks;
using System.Timers;
using Newtonsoft.Json;
using RestSharp;
using RestSharp.Serializers;
using TalosQuestsAdministrationPanel.Model;

namespace TalosQuestsAdministrationPanel
{
    public class TalosQuests
    {
        private static TalosQuests _instance;
        public static TalosQuests Instance => _instance ?? (_instance = new TalosQuests());

        public Session Session => _session;

        public User User => _user;

        public List<QuestModel> Quests => _quests;

        public List<User> Users => _users;

        public long Uptime => _uptime;

        public string Version => _version;

        public string RemoteAddr => _remoteAddr;

        private readonly RestClient _client = new RestClient("http://" + Address + ":" + Port);
        private string _token;

        public static readonly String Address = "127.0.0.1";
        public static readonly int Port = 8080;

        private Session _session;
        private User _user;
        private List<QuestModel> _quests;
        private List<User> _users;

        private long _uptime;
        private string _version;
        private string _remoteAddr;
        private Timer _timer = new Timer();

        public async Task<Boolean> Login(String username, String password)
        {
            try
            {
                var request = new RestRequest("/Auth", Method.POST);
                request.AddJsonBody(new AuthRegisterModel() { userName = username, passWord = password });

                var data = await _client.ExecuteTaskAsync<Response<Session>>(request);

                if (data.Data.state != 200)
                {
                    return false;
                }
                _session = data.Data.response;
                _token = _session.token;
                request = new RestRequest("/User");
                request.AddParameter("token", _token);

                var response = await _client.ExecuteTaskAsync<Response<User>>(request);
                _user = response.Data.response;
                return _user.access.canManageUsers;
            }
            catch (Exception)
            {
                return false;
            }
        }
        public async Task FetchInfo()
        {

            var request = new RestRequest("/", Method.GET);
            var response3 = await _client.ExecuteTaskAsync<Response<ServiceInfo>>(request);
            _version = response3.Data.response.version;
            _uptime = response3.Data.response.uptimeMilliseconds;
            _remoteAddr = response3.Data.response.remoteAddr;
            if (_timer != null)
            {
                _timer.Stop();
                _timer.Dispose();
            }
            _timer = new Timer(1000) { AutoReset = true };
            _timer.Elapsed += (sender, args) =>
            {
                _uptime += 1000;
            };
            _timer.Start();

            request = new RestRequest("/Admin/User", Method.GET);
            request.AddParameter("token", _token);

            var response = await _client.ExecuteTaskAsync<Response<List<User>>>(request);
            _users = response.Data.response;


            request = new RestRequest("/Admin/Quest", Method.GET);
            request.AddParameter("token", _token);

            var response2 = await _client.ExecuteTaskAsync<Response<List<QuestModel>>>(request);
            _quests = response2.Data.response;

        }

        public async Task<Boolean> SetBannedState(User user, Boolean ban)
        {
            var request = new RestRequest("/Admin/User/SetBannedState/" + user.id, Method.GET);
            request.AddParameter("token", _token);
            request.AddParameter("ban", ban);

            var response = await _client.ExecuteTaskAsync<Response<String>>(request);
            if (response.Data.state != 200) throw new TalosQuestsException(response.Data.message, response.Data.state);
            user.banned = ban;
            return true;
        }

        public async Task SearchUsers(String searchPattern)
        {
            var request = new RestRequest("/Admin/User/" + searchPattern, Method.GET);
            request.AddParameter("token", _token);

            var response = await _client.ExecuteTaskAsync<Response<List<User>>>(request);
            if (response.Data.state != 200) throw new TalosQuestsException(response.Data.message, response.Data.state);
            _users = response.Data.response;
        }

        public async Task<Boolean> DeleteUser(User user)
        {
            var request = new RestRequest("/Admin/User/" + user.id, Method.DELETE);
            request.AddParameter("token", _token);

            var response = await _client.ExecuteTaskAsync<Response<String>>(request);
            if (response.Data.state != 200) throw new TalosQuestsException(response.Data.message, response.Data.state);
            return true;
        }

        public async Task<Boolean> AddQuest(QuestModel quest)
        {
            var request = new RestRequest("/Admin/Quest", Method.POST);
            request.AddHeader("Content-Type", "application/json");
            request.AddParameter("token", _token, ParameterType.QueryString);
            request.RequestFormat = DataFormat.Json;
            request.AddJsonBody(quest);

            var response = await _client.ExecuteTaskAsync<Response<QuestModel>>(request);
            if (response.Data.state != 200) throw new TalosQuestsException(response.Data.message, response.Data.state);
            return true;

        }

        public async Task<Boolean> UpdateQuest(long id, QuestModel quest)
        {
            var request = new RestRequest("/Admin/Quest/" + id, Method.PUT);
            request.AddHeader("Content-Type", "application/json");
            request.AddParameter("token", _token, ParameterType.QueryString);
            request.AddParameter("password", "_", ParameterType.QueryString);
            request.RequestFormat = DataFormat.Json;
            request.AddJsonBody(quest);

            var response = await _client.ExecuteTaskAsync<Response<QuestModel>>(request);
            if (response.Data.state != 200) throw new TalosQuestsException(response.Data.message, response.Data.state);
            return true;
        }

        public async Task<Boolean> DeleteQuest(long id)
        {
            var request = new RestRequest("/Admin/Quest/" + id, Method.DELETE);
            request.AddParameter("token", _token, ParameterType.QueryString);
            request.AddParameter("password", "_", ParameterType.QueryString);

            var response = await _client.ExecuteTaskAsync<Response<String>>(request);
            if (response.Data.state != 200) throw new TalosQuestsException(response.Data.message, response.Data.state);
            return true;
        }

        private TalosQuests()
        {

        }
    }

    public class TalosQuestsException : Exception
    {
        public int State { get; private set; }
        public TalosQuestsException(String message, int state) : base(message)
        {
            State = state;
        }
    }
}
