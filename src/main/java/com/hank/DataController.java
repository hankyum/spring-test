package com.hank;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {

	@RequestMapping("/")
	public String index() {
		return "Hello World";
	}
}
