package com.alibaba.csp.sentinel.dashboard.controller.gateway;


import com.alibaba.csp.sentinel.dashboard.config.NacosConfigUtil;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.GatewayFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: hengrui.liu
 * @createTime 2022年 11月14日 15:32
 **/
/**
 * <h3>Sentinel动态数据源的网关流控规则发布器</h3>
 *
 * <p>把通过控制台配置好的规则发布到动态数据源(nacos)中，当前使用的是push模式，
 * 规则被推送至nacos后，网关将会通过监听器自动获取最新的规则，并更新到网关的本地缓
 * 存中。
 * </p>
 */
@Component("gatewayFlowRuleNacosPublisher")
public class GatewayFlowRuleNacosPublisher implements DynamicRulePublisher<List<GatewayFlowRuleEntity>> {
    // 数据源的配置服务
    @Autowired
    private ConfigService configService;
    /**
     * <p>数据通信的转换器。
     * <p>在config包下的NacosConfig类中声明的Spring Bean对象。
     * <p>负责将实体对象转换为json格式的字符串</p>
     */
    @Autowired
    private Converter<List<GatewayFlowRuleEntity>, String> converter;

    @Override
    public void publish(String app, List<GatewayFlowRuleEntity> rules) throws Exception {
        AssertUtil.notEmpty(app, "app name cannot be empty");
        if (rules == null) {
            return;
        }
        /*
         * 将规则发布到动态数据源作持久化，第一个参数是app+后缀，此处用的是-flow-rules的后缀；
         * 第二个参数是nacos分组id，这个用默认提供的sentinel预留项即可；最后一个参数是数据转换
         * 器，要将对象转换成统一的格式后，网络传输到nacos。
         */
        configService.publishConfig(app + NacosConfigUtil.FLOW_DATA_ID_POSTFIX,
                NacosConfigUtil.GROUP_ID, converter.convert(rules));
    }
}
