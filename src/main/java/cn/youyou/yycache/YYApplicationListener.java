package cn.youyou.yycache;

import cn.youyou.yycache.plugin.YYPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 监听器：监听来自spring容器的启停事件，安插进插件的启动和停止
 */
@Component
public class YYApplicationListener implements ApplicationListener<ApplicationEvent> {

    @Autowired
    List<YYPlugin> plugins;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationReadyEvent are) {
            for (YYPlugin plugin : plugins) {
                plugin.init();
                plugin.startup();
            }
        }
        if (event instanceof ContextClosedEvent cce) {
            for (YYPlugin plugin : plugins) {
                plugin.shutdown();
            }
        }
    }
}
