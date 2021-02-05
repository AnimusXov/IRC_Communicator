package org.irccom.controller.factory;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class MessageCompomentFactory{


public ImageView getImageFromWeb( String url) throws IOException{
	URLConnection connection = new URL(url).openConnection();
	connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
	System.out.println(connection.getContentType());
	if ( connection.getContentType().equals("image/png") || connection.getContentType().equals("image/jpeg")) {
		Image image = new Image(connection.getInputStream());
		ImageView imageView = new ImageView();
		imageView.setSmooth(false);
		imageView.setPreserveRatio(true);
		imageView.setFitWidth(400);
		imageView.setFitHeight(200);
		imageView.setImage(image);
		return imageView;
	}
	return null;
}


}
