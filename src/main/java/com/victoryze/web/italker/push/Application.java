package com.victoryze.web.italker.push;



import com.victoryze.web.italker.push.provider.AuthRequestFilter;
import com.victoryze.web.italker.push.provider.GsonProvider;
import com.victoryze.web.italker.push.service.AccountService;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * Created by dsz on 2018/1/14.
 */
public class Application extends ResourceConfig {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public Application() {

        logger.warn("----------start--------");
        //注册逻辑处理的包名
        //packages("net.qiujuer.web.italker.push.service")
        packages(AccountService.class.getPackage().getName());
        //注册全局的请求拦截器
        register(AuthRequestFilter.class);
        //        //注册Json解析器
        //替换解析器Gson
        register(GsonProvider.class);
        //替换日志打印输出
        register(Logger.class);
    }

}
