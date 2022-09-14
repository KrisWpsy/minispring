package com.sg.gate.test.bean;

import com.sg.gate.minispringstep02.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserDao {

    private static Map<String, String> hashMap = new HashMap<>();

    static {
        hashMap.put("10001", "小王，上海，外滩");
        hashMap.put("10002", "八杯水，上海，尖沙咀");
        hashMap.put("10003", "阿毛，香港，铜锣湾");
    }

    /*public void initDataMethod(){
        System.out.println("执行：init-method");
        hashMap.put("10001", "小王");
        hashMap.put("10002", "八杯水");
        hashMap.put("10003", "阿毛");
    }*/

    public String queryUserName(String uId) {
        return hashMap.get(uId);
    }

    /*public void destroyDataMethod(){
        System.out.println("执行：destroy-method");
        hashMap.clear();
    }*/
}
