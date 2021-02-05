package org.irccom.controller.preset;

import org.irccom.helper.Converter;
import org.irccom.sqlite.GenericDao;
import org.irccom.sqlite.PreferenceGenericDaoImpl;
import org.irccom.sqlite.ServerGenericDaoImpl;
import org.irccom.sqlite.model.Preference;
import org.irccom.sqlite.model.Server;

import java.util.HashMap;

public class Config{
	Converter converter = new Converter();
    private final GenericDao<Preference,Integer> PREFERENCE_DAO = new PreferenceGenericDaoImpl();
    public static HashMap<Character, String> defaultColorMap = new HashMap<>();
    public static HashMap<Character, String> userColorMap = new HashMap<>();
    public static HashMap<Character, Integer> metaMap = new HashMap<>();

    public Config() {
    }

    public void init(){
    	PREFERENCE_DAO.get(1).ifPresent(c -> userColorMap.putAll(converter.stringToHashMap(c.getColor())));
	    defaultColorMap.put('~',"#00897b");
	    defaultColorMap.put('@',"#558b2f");
	    defaultColorMap.put('%',"#c62828");
	    defaultColorMap.put('&',"#9e9d24");
	    defaultColorMap.put('U',"#2196f3");
	    defaultColorMap.put('+',"#424242");
	    defaultColorMap.put(' ',"#2196f3");
	    
	    metaMap.put('&',7);
	    metaMap.put('~',6);
	    metaMap.put('@',5);
	    metaMap.put('%',4);
	    metaMap.put('U',3);
	    metaMap.put('+',2);
	    metaMap.put(' ',1);
	    if(userColorMap.isEmpty()){
	    	userColorMap = defaultColorMap;
	    }
    }


    
    
    
}
