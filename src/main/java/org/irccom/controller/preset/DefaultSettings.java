package org.irccom.controller.preset;

import java.util.HashMap;

public class DefaultSettings {
    public HashMap<String, String> colorMap = new HashMap<>();

    public DefaultSettings() {
    }

    public void init(){
        colorMap.put("USER","#2196f3");
        colorMap.put("OTHER_USER","#424242");
        colorMap.put("OP","#558b2f");
        colorMap.put("OWNER","#c62828");
        colorMap.put("SERVER","#00897b");
        colorMap.put("HALF_OP","#9e9d24");
    }
}
