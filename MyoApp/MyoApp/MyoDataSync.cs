using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using MyoSharp.Device;

namespace MyoApp
{
    public class MyoDataSync
    {

        private MyoDataSyncApi MDSA;

        public MyoDataSync()
        {
            this.MDSA = new MyoDataSyncApi();
        }

        public MyoSigne SyncData(DataCollector inputLeft, DataCollector inputRight, Int32? limit = null)
        {
            // ooohh, there's some stories to tell about this stuff... but let's pray it will work fine
            List<MyoData> LeftMyoData = SyncCollectedData(inputLeft);
            List<MyoData> RightMyoData = SyncCollectedData(inputRight);
            MyoSigne signe = SyncTwoArm(LeftMyoData, RightMyoData);
            return signe;
        }

        private MyoSigne SyncTwoArm(List<MyoData> inputLeft, List<MyoData> inputRight)
        {
            List<MyoData> baseData, unsyncedData;
            Dictionary<string, List<MyoData>> data = new Dictionary<string, List<MyoData>>();
            DateTime baseTime;
            List<MyoData> output = new List<MyoData>();
            if (inputLeft.Count > inputRight.Count)
            {
                baseData = inputRight;
                unsyncedData = inputLeft;
                data.Add("R", baseData);
                data.Add("L", output);
            }
            else if (inputLeft.Count < inputRight.Count)
            {
                baseData = inputLeft;
                unsyncedData = inputRight;
                data.Add("R", output);
                data.Add("L", baseData);
            }
            else
            {
                return new MyoSigne() { LeftMyoData = inputLeft, RightMyoData = inputRight };
            }

            foreach (MyoData myoData in baseData)
            {
                baseTime = myoData.Time_stamp;
                int index = baseData.IndexOf(myoData);
                if (index == 0) { output.Add(unsyncedData[0]); }
                else
                {
                    int backwardDiff = MDSA.getTimeDiff(baseTime, unsyncedData[index - 1].Time_stamp);
                    int forwardDiff = MDSA.getTimeDiff(baseTime, unsyncedData[index + 1].Time_stamp);
                    int twoStepDiff = MDSA.getTimeDiff(baseTime, unsyncedData[index + 2].Time_stamp);

                    if (twoStepDiff < forwardDiff && output.IndexOf(unsyncedData[index + 2]) == -1)
                    {
                        output.Add(unsyncedData[index + 2]);
                    }
                    else if (backwardDiff >= forwardDiff && output.IndexOf(unsyncedData[index + 1]) == -1)
                    {
                        output.Add(unsyncedData[index + 1]);
                    }
                    else if (backwardDiff <= forwardDiff && output.IndexOf(unsyncedData[index - 1]) == -1)
                    {
                        output.Add(unsyncedData[index - 1]);
                    }
                    else
                    {
                        output.Add(unsyncedData[index + 1]);
                    }
                }
            }

            //MDSA.printTimeDiff(data["L"], data["R"]);

            return new MyoSigne()
            {
                LeftMyoData = data["L"],
                RightMyoData = data["R"]
            };
        }

        public List<MyoData> SyncCollectedData(DataCollector input)
        {
            List<MyoData> output = new List<MyoData>();
            DateTime baseTime;

            foreach (AccelerometerDataEventArgs baseData in input.accelerometerData)
            {
                baseTime = baseData.Timestamp;
                for (int i = 0; i < 100; i++)
                {
                    if (MDSA.isEMGexists(input, baseTime, i))
                    {
                        if (!output.Any(x => x.Time_stamp == baseTime.AddMilliseconds(i)))
                        {
                            MyoData data = MDSA.buildRecordUsing(input, baseTime, baseTime.AddMilliseconds(i));
                            output.Add(data);
                            break;
                        }
                        else
                        {
                            break;
                        }
                    }
                    else if (MDSA.isEMGexists(input, baseTime, i * -1))
                    {
                        if (!output.Any(x => x.Time_stamp == baseTime.AddMilliseconds(i * -1)))
                        {
                            MyoData data = MDSA.buildRecordUsing(input, baseTime, baseTime.AddMilliseconds(i * -1));
                            output.Add(data);
                            break;
                        }
                        else
                        {
                            break;
                        }
                    }
                }
            }
            return output;
        }
    }
}
