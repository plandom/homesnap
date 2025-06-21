package com.poc.homesnap.homesnap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.tei.ezcommon", "com.tei.ezpayment", "com.tei.eziam", "com.poc.homesnap"})
public class HomesnapApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomesnapApplication.class, args);
	}

}
