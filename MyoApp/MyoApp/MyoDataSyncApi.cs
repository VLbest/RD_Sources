using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using MyoSharp.Device;

namespace MyoApp
{
    public class MyoDataSyncApi
    {




        public MyoData buildRecordUsing(DateTime time, DataCollector data)
        {

            AccelerometerDataEventArgs Acc = data.accelerometerData.First(X => X.Timestamp == time);
            EmgDataEventArgs Emg = data.emgData.First(X => X.Timestamp == time);
            OrientationDataEventArgs Ori = data.orientationData.First(X => X.Timestamp == time);
            GyroscopeDataEventArgs Gyro = data.gyroscopeData.First(X => X.Timestamp == time);
            return buildRecordUsing(Emg, Acc, Gyro, Ori);
        }

        public void printTimeDiff(List<MyoData> left, List<MyoData> right)
        {
            foreach (MyoData myoData in left)
            {
                TimeSpan span = myoData.Time_stamp - right[left.IndexOf(myoData)].Time_stamp;
                Debug.WriteLine("Diff: {0}", (int)span.TotalMilliseconds);
            }
        }

        public MyoData buildRecordUsing(DataCollector data, DateTime AGOStamp, DateTime EMGStamp)
        {
            AccelerometerDataEventArgs Acc = data.accelerometerData.First(X => X.Timestamp == AGOStamp);
            EmgDataEventArgs Emg = data.emgData.First(X => X.Timestamp == EMGStamp);
            OrientationDataEventArgs Ori = data.orientationData.First(X => X.Timestamp == AGOStamp);
            GyroscopeDataEventArgs Gyro = data.gyroscopeData.First(X => X.Timestamp == AGOStamp);
            return buildRecordUsing(Emg, Acc, Gyro, Ori);
        }

        public MyoData buildRecordUsing(EmgDataEventArgs Emg, AccelerometerDataEventArgs Acc, GyroscopeDataEventArgs Gyro, OrientationDataEventArgs Ori)
        {
            return new MyoData()
            {
                Time_stamp = Acc.Timestamp,
                Acc_X = Acc.Accelerometer.X,
                Acc_Y = Acc.Accelerometer.Y,
                Acc_Z = Acc.Accelerometer.Z,
                Gyro_X = Gyro.Gyroscope.X,
                Gyro_Y = Gyro.Gyroscope.Y,
                Gyro_Z = Gyro.Gyroscope.Z,
                Ori_X = Ori.Orientation.X,
                Ori_Y = Ori.Orientation.Y,
                Ori_Z = Ori.Orientation.Z,
                Ori_W = Ori.Orientation.W,
                Emg_1 = Emg.EmgData.GetDataForSensor(0),
                Emg_2 = Emg.EmgData.GetDataForSensor(1),
                Emg_3 = Emg.EmgData.GetDataForSensor(2),
                Emg_4 = Emg.EmgData.GetDataForSensor(3),
                Emg_5 = Emg.EmgData.GetDataForSensor(4),
                Emg_6 = Emg.EmgData.GetDataForSensor(5),
                Emg_7 = Emg.EmgData.GetDataForSensor(6),
                Emg_8 = Emg.EmgData.GetDataForSensor(7),
            };
        }

        internal int getTimeDiff(DateTime A, DateTime B)
        {
            TimeSpan span = A - B;
            return Math.Abs((int)span.TotalMilliseconds);
        }

        public bool isEMGexists(DataCollector input, DateTime baseTime, int msOffset)
        {
            return input.emgData.Any(x => x.Timestamp == baseTime.AddMilliseconds(msOffset));
        }
    }
}
