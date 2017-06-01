using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using MyoSharp.Device;

namespace MyoApp
{
    public class DataCollector
    {
        public List<AccelerometerDataEventArgs> accelerometerData { get; private set; }
        public List<GyroscopeDataEventArgs> gyroscopeData { get; private set; }
        public List<OrientationDataEventArgs> orientationData { get; private set; }
        public List<EmgDataEventArgs> emgData { get; private set; }

        public DataCollector()
        {
            this.accelerometerData = new List<AccelerometerDataEventArgs>();
            this.gyroscopeData = new List<GyroscopeDataEventArgs>();
            this.orientationData = new List<OrientationDataEventArgs>();
            this.emgData = new List<EmgDataEventArgs>();
        }

        public void Purge()
        {
            this.orientationData.Clear();
            this.gyroscopeData.Clear();
            this.accelerometerData.Clear();
            this.emgData.Clear();
        }
    }
}
