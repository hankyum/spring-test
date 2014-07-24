package com.hank.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.GatewayHeader;
import org.springframework.integration.annotation.MessagingGateway;

import com.hank.domain.Person;

@MessagingGateway
public interface PersonGateway {
	@Gateway(requestChannel = "personObjChannel", replyTimeout = 2, requestTimeout = 200)
	public void createPerson(Person p);
	
	@Gateway(requestChannel = "personStrChannel", replyTimeout = 2, requestTimeout = 200, headers={@GatewayHeader(name="spiltter", value="\\|")})
	public void createPerson(String nameVal);
	
	@Gateway(requestChannel = "personQueryChannel")
	public void queryPerson(String query);
}
