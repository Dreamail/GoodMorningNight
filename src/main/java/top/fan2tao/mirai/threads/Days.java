package top.fan2tao.mirai.threads;

import top.fan2tao.mirai.GMN;

import java.util.Calendar;

public class Days extends Thread {
    private boolean start = true;

    @Override
    public void run() {
        while (start) {
            synchronized (GMN.class) {
                //��ȡ��ǰ��ʱ��
                Calendar c = Calendar.getInstance();//���Զ�ÿ��ʱ���򵥶��޸�
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                int second = c.get(Calendar.SECOND);
                //�֣��붼����0 ʱ��������
                if (second == 0 && minute == 0 && hour == 0) {
                    GMN.setNewDay();
                }

                try {
                    Thread.sleep(30000);
                    //���һ��
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
