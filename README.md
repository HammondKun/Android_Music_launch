写入了一个安卓端的音乐播放器，屏幕适配是 1920 * 720 ,。
分为Main视图，CD视图，Tuner视图。整体开发模式采用MVVM框架。
主要函数基本上写在Libraray.java文件中，在需要的情况下调用。
图标替换(如暂停键点下后变成播放键)通过android：visability控制
