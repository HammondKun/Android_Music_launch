Written into an Android music player. The virtual machine fits android version 15.0, and screen resolution is 1920 * 720.
The whole application has been divided into three parts: Main View, CD View , and Tuner View. 
M-V-VM is the framework of this project, binding to databinding using LiveDaTa.
Most of functions were written in Library.java and they are called when needed. Use compose "android:visability" to define whether picture should be shown.

写入了一个安卓端的音乐播放器，虚拟机适配Android 15.0,屏幕分辨率是 1920 * 720 ,分为Main视图，CD视图，Tuner视图。整体开发框架采用MVVM框架,使用LiveDaTa绑定到databinding。
主要函数基本上写在Libraray.java文件中，在需要的情况下调用。图标替换(如暂停键点下后变成播放键)通过android：visability控制
