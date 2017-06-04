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

namespace MyoApp
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window {

        private Timer timer;
        public bool IsRecording { get; set; }

        public MainWindow()
        {
            InitializeComponent();
            StartListenning.IsEnabled = false;
            StopListenning.IsEnabled = false;
            ComboBoxWordList.Items.Add("OK");
            ComboBoxWordList.Items.Add("NO");
            ComboBoxWordList.Items.Add("QUICK");
            ComboBoxWordList.Items.Add("PISTOL");
        }

        private void InitConnection_Click(object sender, RoutedEventArgs e)
        {
            MyoManager.Instance.InitMyoConnection();
            StartListenning.IsEnabled = true;
            InitConnection.IsEnabled = false;
            timer = new Timer(UpdateInfo, new AutoResetEvent(true), 1000, 250);

        }

        private void UpdateInfo(object state) {
            String info = $"{MyoManager.Instance.MyHub.Myos.Count} Myos";
            foreach (var myo in MyoManager.Instance.MyHub.Myos) {
                info += $"\n{myo.Handle} : {myo.Arm} & {myo.IsConnected}";
            }
            this.Dispatcher.Invoke(() =>
            {
                StatusLabel.Content = "";
                StatusLabel.Content = info;
            });
        }

        private void StartListenning_Click(object sender, RoutedEventArgs e)
        {
            MyoManager.Instance.StartListenning();
            StartListenning.IsEnabled = false;
            StopListenning.IsEnabled = true;
        }

        private void StopListenning_Click(object sender, RoutedEventArgs e)
        {
            MyoManager.Instance.StopListenning();
            StartListenning.IsEnabled = true;
            StopListenning.IsEnabled = false;
        }

        private void StartRecordingClick(object sender, RoutedEventArgs e)
        {
            if (IsRecording) {
                BtnRecGo.Content = "(waiting) START";
                TxtRecResults.Content = MyoManager.Instance.StopRecording();
            } else {
                BtnRecGo.Content = "(recording) STOP";
                MyoManager.Instance.StartRecording();
                TxtRecResults.Content = "";
            }
            IsRecording = !IsRecording;
        }

        private void BtnSave_Click(object sender, RoutedEventArgs e) {
            TxtRecResults.Content = "Saving.... wait";
            String response = MyoManager.Instance.SaveData(ComboBoxWordList.SelectedItem.ToString());
            TxtRecResults.Content = response;
        }
    }
}
