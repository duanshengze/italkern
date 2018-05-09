package com.victoryze.web.italker.push.utils;

import com.gexin.rp.sdk.base.IBatch;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.google.common.base.Strings;
import com.victoryze.web.italker.push.bean.api.base.PushModel;
import com.victoryze.web.italker.push.bean.db.User;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 消息推送工具
 * Created by dsz on 2018/4/20.
 */
public class PushDispatcher {
    private static final String APP_ID = "G8qEbZxIrfAO5wB8xA7Us9";
    private static final String APP_SERCRET = "jCPzG5UtVX73DlymAeHqr2";
    private static final String APP_KEY = "7Byr8CDj8fAvvxKnDRa6l";
    private static final String MASTER_SECRET = "vB1W0Dp2Pk9ka1FWx197B4";
    private static final String HOST = "http://sdk.open.api.igexin.com/apiex.htm";

    private final IGtPush pusher;

    private final List<BatchBean> beans = new ArrayList<>();

    public PushDispatcher() {
        pusher = new IGtPush(HOST, APP_KEY, MASTER_SECRET);
    }


    public boolean add(User receiver, PushModel model) {
        if (receiver == null || model == null || Strings.isNullOrEmpty(receiver.getPushId())) {
            return false;
        }
        String pushStr = model.getPushString();
        if (Strings.isNullOrEmpty(pushStr)) {
            return false;
        }
        //构建一个目标+内容
        BatchBean bean = buildMessage(receiver.getPushId(), pushStr);
        beans.add(bean);
        return true;


    }

    /**
     * 对发送的数据进行格式化封装
     *
     * @param pushId
     * @param pushStr
     * @return
     */
    private BatchBean buildMessage(String pushId, String pushStr) {

        //透传消息，不是通知栏显示，而是在MessageReceiver收到
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(APP_ID);
        template.setAppkey(APP_KEY);
        template.setTransmissionContent(pushStr);
        template.setTransmissionType(0);//填1表示自动启动app
        SingleMessage message = new SingleMessage();
        message.setData(template);//把透传消息设置到单消息模板中
        message.setOffline(true);//是否运行离线发送
        message.setOfflineExpireTime(24 * 3600 * 1000);//离线消息时常

        Target target = new Target();
        target.setAppId(APP_ID);
        target.setClientId(pushId);

        return new BatchBean(message, target);
    }


    private static class BatchBean {
        SingleMessage message;
        Target traget;

        public BatchBean(SingleMessage message, Target traget) {
            this.message = message;
            this.traget = traget;
        }
    }

    //进行消息最终推送
    public boolean submit() {
        //构建打包工具类
        IBatch batch = pusher.getBatch();
        //是否有数据需要发送
        boolean haveData = false;

        for (BatchBean bean : beans) {
            try {
                batch.add(bean.message, bean.traget);
                haveData=true;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (!haveData) {
            return false;
        }

        IPushResult result = null;
        try {
            result = batch.submit();
        } catch (IOException e) {
            e.printStackTrace();

            try {
                batch.retry();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        if (result != null) {
            try {
                Logger.getLogger("PushDispatcher").log(Level.INFO,
                        (String) result.getResponse().get("result"));
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Logger.getLogger("PushDispatcher").log(Level.WARNING, "推送服务器响应异常");
        return false;

    }
}
