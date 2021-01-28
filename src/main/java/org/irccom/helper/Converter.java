package org.irccom.helper;

import javafx.scene.paint.Color;
import org.irccom.controller.preset.DefaultSettings;

public class Converter{
private String format(double val) {
	String in = Integer.toHexString((int) Math.round(val * 255));
	return in.length() == 1 ? "0" + in : in;
}

public String toHexString( Color value) {
	return "#" + (format(value.getRed()) + format(value.getGreen()) + format(value.getBlue()) + format(value.getOpacity()))
			             .toUpperCase();
}

public javafx.scene.paint.Color convertToFXColor( char mapkey){
	java.awt.Color javaAWTColor = java.awt.Color.decode(DefaultSettings.colorMap.get(mapkey));
	int r = javaAWTColor.getRed();
	int g = javaAWTColor.getGreen();
	int b = javaAWTColor.getBlue();
	int a = javaAWTColor.getAlpha();
	double alphaChannel = a / 255.0 ;
	return javafx.scene.paint.Color.rgb(r, g, b, alphaChannel);
}

}
