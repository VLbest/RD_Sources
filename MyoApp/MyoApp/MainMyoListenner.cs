using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Controls;
using MyoSharp.Communication;
using MyoSharp.Device;
using MyoSharp.Exceptions;

namespace MyoApp
{
    public class MainMyoListenner
    {
        private IChannel channel;
        private IHub hub;
        public DataCollector collectorRight { get; set; }
        public DataCollector collectorLeft { get; set; }
        public MyoDataSync synchronizer { get; set; }
        private bool isListenning;

        public MainMyoListenner(IChannel c, IHub h) {
            channel = c;
            hub = h;
            collectorRight = new DataCollector();
            collectorLeft = new DataCollector();
            synchronizer = new MyoDataSync();
            this.isListenning = false;
        }


        public void statListening()
        {
            // Hadle Multiple Sides
            Debug.WriteLine("start listening");
            this.purgeCollectors();

            foreach (IMyo myo in hub.Myos)
            {
                myo.Vibrate(VibrationType.Short);
                myo.PoseChanged += Myo_PoseChanged;
                myo.Locked += Myo_Locked;
                myo.Unlocked += Myo_Unlocked;
                myo.EmgDataAcquired += Myo_EmgData;
                myo.OrientationDataAcquired += Myo_OrientationData;
                myo.AccelerometerDataAcquired += Myo_AccelerationData;
                myo.GyroscopeDataAcquired += Myo_GyroscopeData;
                myo.SetEmgStreaming(true);
            }
            //channel.StartListening();
            this.isListenning = true;
        }

        public void stopListening()
        {
            Debug.WriteLine("stop listening");

            foreach (IMyo myo in hub.Myos)
            {
                myo.Vibrate(VibrationType.Short);
                myo.PoseChanged -= Myo_PoseChanged;
                myo.Locked -= Myo_Locked;
                myo.Unlocked -= Myo_Unlocked;
                myo.EmgDataAcquired -= Myo_EmgData;
                myo.OrientationDataAcquired -= Myo_OrientationData;
                myo.AccelerometerDataAcquired -= Myo_AccelerationData;
                myo.GyroscopeDataAcquired -= Myo_GyroscopeData;
                myo.SetEmgStreaming(false);
                myo.Vibrate(VibrationType.Short);
            }
            //channel.StopListening();
            this.isListenning = false;
        }

        private void purgeCollectors()
        {
            collectorLeft.Purge();
            collectorRight.Purge();
        }

        public List<MyoData> GetSyncdData() {
            MyoSigne myosigne = synchronizer.SyncData(collectorLeft, collectorRight);
            List<MyoData> dataLeft = myosigne.LeftMyoData;
            List<MyoData> dataRight = myosigne.RightMyoData;
            return dataLeft;
        }

        public void SaveRecodedData()
        {
            Debug.WriteLine("save start");

            MyoSigne myosigne = synchronizer.SyncData(collectorLeft, collectorRight);

            //create model classes
            List<MyoData> dataLeft = myosigne.LeftMyoData;
            List<MyoData> dataRight = myosigne.RightMyoData;
            /*
            Signe signe = new Signe();
            Geste gesteRight = new Geste();
            Geste gesteLeft = new Geste();
            gesteRight.MyoDatas = myosigne.RightMyoData;
            gesteRight.Main = "R";
            gesteLeft.MyoDatas = myosigne.LeftMyoData;
            gesteLeft.Main = "L";

            signe.Gestes.Add(gesteRight);
            signe.Gestes.Add(gesteLeft);
            signe.ID_Personne = personne.ID;
            signe.ID_Mot = mot.ID;

            model.Signes.Add(signe);
            Debug.WriteLine("save bdd");
            string dbAnswer = "";
            try
            {
                int savedDataCount = model.SaveChanges();
                dbAnswer = savedDataCount.ToString() + " Modifications";
            }
            catch (Exception ex)
            {
                dbAnswer = "ERROR: " + ex.Message;
            }

            Debug.WriteLine("retour base = " + dbAnswer);
            */
        }

        private void Myo_PoseChanged(object sender, PoseEventArgs e)
        {
            Debug.WriteLine("{0} arm Myo detected {1} pose!", e.Myo.Arm, e.Myo.Pose);
        }

        private void Myo_Unlocked(object sender, MyoEventArgs e)
        {
            Debug.WriteLine("{0} arm Myo has unlocked!", e.Myo.Arm);
        }

        private void Myo_Locked(object sender, MyoEventArgs e)
        {
            Debug.WriteLine("{0} arm Myo has locked!", e.Myo.Arm);
        }

        private void Myo_EmgData(object sender, MyoEventArgs e)
        {
            if (!isListenning) return;
            //nok
            if (e.Myo.Arm == Arm.Right)
            {
                collectorRight.emgData.Add((EmgDataEventArgs)e);
            }
            else if (e.Myo.Arm == Arm.Left)
            {
                collectorLeft.emgData.Add((EmgDataEventArgs)e);
            }
            else
            {
                //error
            }

            Debug.WriteLine("{0} arm Myo emgData: {1} {2}", e.Myo.Arm, e.Myo.EmgData.GetDataForSensor(1), e.Myo.EmgData.GetDataForSensor(2));
        }

        private void Myo_OrientationData(object sender, MyoEventArgs e)
        {
            if (!isListenning) return;
            //ok
            if (e.Myo.Arm == Arm.Right)
            {
                collectorRight.orientationData.Add((OrientationDataEventArgs)e);
            }
            else if (e.Myo.Arm == Arm.Left)
            {
                collectorLeft.orientationData.Add((OrientationDataEventArgs)e);
            }
            else
            {
                //error
            }

            Debug.WriteLine("{0} arm Myo orientationData: {1}", e.Myo.Arm, e.Myo.Orientation);
        }

        private void Myo_GyroscopeData(object sender, MyoEventArgs e)
        {
            if (!isListenning) return;
            //ok
            if (e.Myo.Arm == Arm.Right)
            {
                collectorRight.gyroscopeData.Add((GyroscopeDataEventArgs)e);
            }
            else if (e.Myo.Arm == Arm.Left)
            {
                collectorLeft.gyroscopeData.Add((GyroscopeDataEventArgs)e);
            }
            else
            {
                //error
            }

            //Debug.WriteLine("{0} arm Myo GyroscopeData: {1}", e.Myo.Arm, e.Myo.Gyroscope);
        }

        private void Myo_AccelerationData(object sender, MyoEventArgs e)
        {
            if (!isListenning) return;
            //ok
            if (e.Myo.Arm == Arm.Right)
            {
                collectorRight.accelerometerData.Add((AccelerometerDataEventArgs)e);
            }
            else if (e.Myo.Arm == Arm.Left)
            {
                collectorLeft.accelerometerData.Add((AccelerometerDataEventArgs)e);
            }
            else
            {
                //error
            }

            //Debug.WriteLine("{0} arm Myo accelerometerData: {1}", e.Myo.Arm, e.Myo.Accelerometer);
        }
    }
}
