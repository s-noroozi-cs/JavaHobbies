package com.keycloak.client.reasapi.util;

import com.keycloak.client.restapi.util.ConfigUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;
import uk.org.webcompere.systemstubs.jupiter.SystemStub;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;

import java.util.UUID;

@ExtendWith(SystemStubsExtension.class)
public class ConfigUtilTest {

    @SystemStub
    private static EnvironmentVariables environmentVariables;


    @Test
    void check_config_default_value(){
        String cfgName = UUID.randomUUID().toString();
        String defValue = UUID.randomUUID().toString();
        Assertions.assertEquals(defValue, ConfigUtil.getConfig(cfgName,defValue));
    }

    @Test
    void check_config_env_value(){
        String cfgName = UUID.randomUUID().toString();
        String cfgValue = UUID.randomUUID().toString();
        String defValue = UUID.randomUUID().toString();

        environmentVariables.set(cfgName, cfgValue);

        Assertions.assertEquals(cfgValue, ConfigUtil.getConfig(cfgName,defValue));
    }

    @Test
    void check_config_sys_param_value(){
        String cfgName = UUID.randomUUID().toString();
        String cfgValue = UUID.randomUUID().toString();
        String defValue = UUID.randomUUID().toString();

        System.setProperty(cfgName,cfgValue);

        Assertions.assertEquals(cfgValue, ConfigUtil.getConfig(cfgName,defValue));
    }

    @Test
    void check_config_sys_param_over_env_value(){
        String cfgName = UUID.randomUUID().toString();
        String cfgValueSysParam = UUID.randomUUID().toString();
        String cfgValueEnvVal = UUID.randomUUID().toString();
        String defValue = UUID.randomUUID().toString();

        System.setProperty(cfgName,cfgValueSysParam);
        environmentVariables.set(cfgName, cfgValueEnvVal);

        Assertions.assertEquals(cfgValueSysParam, ConfigUtil.getConfig(cfgName,defValue));
    }

}
