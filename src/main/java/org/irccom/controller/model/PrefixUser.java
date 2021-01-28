package org.irccom.controller.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.kitteh.irc.client.library.element.User;

@NoArgsConstructor
@Getter
@Setter
public class PrefixUser{
	 User user;
	char prefix;

public PrefixUser( User user, char prefix ){
	this.user = user;
	this.prefix = prefix;
}

}
