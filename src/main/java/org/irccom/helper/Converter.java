package org.irccom.helper;

import com.google.common.base.Joiner;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Converter{
private String format(double val) {
	String in = Integer.toHexString((int) Math.round(val * 255));
	return in.length() == 1 ? "0" + in : in;
}

public String toHexString( Color value) {
	return "#" + (format(value.getRed()) + format(value.getGreen()) + format(value.getBlue()) + format(value.getOpacity()))
			             .toUpperCase();
}

public javafx.scene.paint.Color convertToFXColor(HashMap<Character,String> map,char mapkey){
	java.awt.Color javaAWTColor;
	if(!map.get(mapkey).startsWith("#")){
		javaAWTColor = java.awt.Color.decode("#"+map.get(mapkey));
	}
	else {
		javaAWTColor = java.awt.Color.decode(map.get(mapkey));
	}
	int r = javaAWTColor.getRed();
	int g = javaAWTColor.getGreen();
	int b = javaAWTColor.getBlue();
	int a = javaAWTColor.getAlpha();
	double alphaChannel = a / 255.0 ;
	return javafx.scene.paint.Color.rgb(r, g, b, alphaChannel);
	
}
public  Color hexToColor(HashMap<Character,String> map,char mapkey)
{
	String hex = map.get(mapkey);
	return Color.valueOf(hex);
}

public ArrayList<String> stringToArray ( String string){
	return new ArrayList<>(Arrays.asList(string.split(",")));
}
public HashMap<Character, String> stringToHashMap (String string){
	return (HashMap<Character, String>) Arrays.stream(string.split(",")).map(s -> s.split(":"))
	.collect(Collectors.toMap(s -> s[0].charAt(0), s -> (s[1])));
}


public String mapToString(Map<Character, ?> map) {
	return Joiner.on(",").withKeyValueSeparator(":").join(map);
}

}
