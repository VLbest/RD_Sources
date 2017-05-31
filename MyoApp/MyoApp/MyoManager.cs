using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using MyoSharp.Communication;
using MyoSharp.Device;
using MyoSharp.Exceptions;

namespace MyoApp
{
    public class MyoManager
    {
        public IChannel MyChannel { get; set; }
        public IHub MyHub { get; set; }
        public bool IsListenning { get; set; }
        private static MyoManager instance;

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
            MyHub.MyoConnected += OnMyoConnected;
            MyHub.MyoDisconnected += OnMyoDisconnected;
        }

        private void OnMyoConnected(object sender, MyoEventArgs e) {
            Debug.WriteLine("Myo {0} has connected on {1} arm.", e.Myo.Handle, e.Myo.Arm.ToString().ToLower());
            e.Myo.Vibrate(VibrationType.Short);
        }

        private void OnMyoDisconnected(object sender, MyoEventArgs e)
        {
            Debug.WriteLine("Oh no! It looks like {0} arm Myo has disconnected!", e.Myo.Arm);
            e.Myo.Vibrate(VibrationType.Medium);
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

    }
}
