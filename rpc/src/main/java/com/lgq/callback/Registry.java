package com.lgq.callback;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author lgq
 */
public class Registry {
    private Map<String, TreeSet<String>> registryData;

    public void start() {
        registryData = new HashMap<String, TreeSet<String>>();
        System.out.println(">>>> 注册中心创建成功");
    }

    public void stop() {
        registryData.clear();
    }

    public boolean registry(Set<String> keys, String value) {
        if (keys == null || keys.size() == 0
                || value == null || value.trim().length() == 0) {
            return false;
        }

        for (String key : keys) {
            TreeSet<String> values = registryData.get(key);
            if (values == null) {
                values = new TreeSet<>();
                registryData.put(key, values);
            }
            values.add(value);
        }
        System.out.println(">>>> 服务注册成功");
        return true;
    }

    public boolean remove(Set<String> keys, String value) {
        if (keys == null || keys.size() == 0
                || value == null || value.trim().length() == 0) {
            return false;
        }

        for (String key : keys) {
            TreeSet<String> values = registryData.get(key);
            if (values != null) {
                values.remove(value);
            }
        }
        System.out.println(">>>> 服务移除成功");
        return true;
    }
}
