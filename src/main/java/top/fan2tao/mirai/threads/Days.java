package top.fan2tao.mirai.threads;

import top.fan2tao.mirai.GMN;

import java.util.Calendar;

public class Days extends Thread {
    private boolean start = true;

    @Override
    public void run() {
        while (start) {
            synchronized (GMN.class) {
                //获取当前的时间
                Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                int second = c.get(Calendar.SECOND);
                //分，秒都等于0 时就是整点
                if (second == 0 && minute == 0 && hour == 0) {
                    GMN.setNewDay();
                }

                try {
                    Thread.sleep(30000);
                    //间隔一秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void mstop() {
        this.start = false;
    }
}
