using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Documents;
using MyoSharp.Communication;
using MyoSharp.Device;
using MyoSharp.Exceptions;
using Newtonsoft.Json;
using RestSharp;
namespace MyoApp
{
    public class MyoManager
    {
        public IChannel MyChannel { get; set; }
        public IHub MyHub { get; set; }
        public bool IsListenning { get; set; }
        private static MyoManager instance;
        public MainMyoListenner MyoListenner { get; set; }
        private List<MyoData> myoDatasSynced;
        private List<MyoData> myoDatasRaw;

        public static MyoManager Instance {
            get => instance ?? (instance = new MyoManager());
            set => instance = value;
        }

        private MyoManager()
        {
        }

        public void InitMyoConnection() {
            MyChannel = Channel.Create(ChannelDriver.Create(ChannelBridge.Create(), MyoErrorHandlerDriver.Create(MyoErrorHandlerBridge.Create())));
            MyHub = Hub.Create(MyChannel);
            MyoListenner = new MainMyoListenner(MyChannel, MyHub);
        }


        public void StartListenning() {
            IsListenning = true;
            MyChannel.StartListening();
        }

        public void StopListenning()
        {
            IsListenning = false;
            MyChannel.StopListening();
        }

        public String StopRecording() {
            MyoListenner.stopListening();
            string desciption =
                $"Original: \n IMU: {MyoListenner.collectorLeft.accelerometerData.Count}, EMG: {MyoListenner.collectorLeft.emgData.Count}, Time: {MyoListenner.collectorLeft.emgData.AsEnumerable().Select(x => x.Timestamp).Max() - MyoListenner.collectorLeft.emgData.AsEnumerable().Select(x => x.Timestamp).Min()}, s";

            MyoDataSync dataSync = new MyoDataSync();

            //myoDatasRaw = dataSync.SyncCollectedDataWithZeros(MyoListenner.collectorLeft);
            myoDatasSynced = dataSync.SyncCollectedData(MyoListenner.collectorLeft);

            desciption += $"\nSync: \n IMU: {myoDatasSynced.Count}, EMG: {myoDatasSynced.Count}";

            return desciption;
        }

        public void StartRecording() {
            MyoListenner.statListening();
        }

        public String SaveData(String word) {
            var client = new RestClient("http://146.148.104.110:5000/");
            var request = new RestRequest("insert_sync", Method.POST);
            request.Method = Method.POST;
            request.AddHeader("Accept", "application/json");
            request.Parameters.Clear();
            var content = new GestureObj() {
                GestureName = word,
                Data = myoDatasSynced.ToArray()
            };
            var json = JsonConvert.SerializeObject(content);
            request.AddParameter("application/json", json, ParameterType.RequestBody);
            IRestResponse response = client.Execute(request);
            return response.StatusCode.ToString();
        }
    }
}