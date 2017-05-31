﻿using System;
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

        public MainWindow()
        {
            InitializeComponent();
            StartListenning.IsEnabled = false;
            StopListenning.IsEnabled = false;
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
            MyoManager.start 
        }
    }
}