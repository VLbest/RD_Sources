using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MyoApp
{
    public partial class MyoData
    {
        public double Acc_X { get; set; }

        public double Acc_Y { get; set; }

        public double Acc_Z { get; set; }

        public double Gyro_X { get; set; }

        public double Gyro_Y { get; set; }

        public double Gyro_Z { get; set; }

        public double Ori_X { get; set; }

        public double Ori_Y { get; set; }

        public double Ori_Z { get; set; }

        public double Ori_W { get; set; }

        public double Emg_1 { get; set; }

        public double Emg_2 { get; set; }

        public double Emg_3 { get; set; }

        public double Emg_4 { get; set; }

        public double Emg_5 { get; set; }

        public double Emg_6 { get; set; }

        public double Emg_7 { get; set; }

        public double Emg_8 { get; set; }
        public DateTime Time_stamp { get; set; }

        public double Pitch { get; set; }
        public double Roll { get; set; }
        public double Yaw { get; set; }
        public double EmgMAV { get; set; }

    }
}
