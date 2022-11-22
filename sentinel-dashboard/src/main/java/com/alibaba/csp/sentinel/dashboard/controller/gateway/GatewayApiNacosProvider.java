package com.alibaba.csp.sentinel.dashboard.controller.gateway;

/**
 * @author: hengrui.liu
 * @createTime 2022年 11月14日 14:48
 **/
import com.alibaba.csp.sentinel.dashboard.config.NacosConfigUtil;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.ApiDefinitionEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.GatewayFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("gatewayApiNacosProvider")
public class GatewayApiNacosProvider implements DynamicRuleProvider<List<ApiDefinitionEntity>> {
    @Autowired
    private ConfigService configService;
    // 规则对象的转换器，获取到的数据根据使用的数据类型的不同，需要用不同的转换器转化后使用
    @Autowired
    private Converter<String, List<ApiDefinitionEntity>> converter;

    @Override
    public List<ApiDefinitionEntity> getRules(String appName) throws Exception {
        String rules = configService.getConfig(appName + NacosConfigUtil.API_DATA_ID_POSTFIX,
                NacosConfigUtil.GROUP_ID, 3000);
        if (StringUtil.isEmpty(rules)) {
            return new ArrayList<>(0);
        }
        return converter.convert(rules);
    }
}
