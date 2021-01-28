package org.irccom.controller.preset;

import java.util.HashMap;

public class DefaultSettings {
    public static HashMap<Character, String> colorMap = new HashMap<>();
    public static HashMap<Character, Integer> metaMap = new HashMap<>();

    public DefaultSettings() {
    }

    public void init(){
	    colorMap.put('~',"#00897b");
	    colorMap.put('@',"#558b2f");
	    colorMap.put('%',"#c62828");
	    colorMap.put('&',"#9e9d24");
	    colorMap.put('U',"#2196f3");
	    colorMap.put('+',"#424242");
	    colorMap.put(' ',"#2196f3");
	    
	    metaMap.put('&',7);
	    metaMap.put('~',6);
	    metaMap.put('@',5);
	    metaMap.put('%',4);
	    metaMap.put('U',3);
	    metaMap.put('+',2);
	    metaMap.put(' ',1);
    }


    
    
    
}
