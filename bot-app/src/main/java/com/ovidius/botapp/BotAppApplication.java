package com.ovidius.botapp;

import net.dv8tion.jda.api.JDA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BotAppApplication implements CommandLineRunner {

	@Autowired
	private JDA jda;

	public void run(String... args) throws Exception {
		System.out.println("JDA bean has been loaded and the bot should be online!");
	}

	public static void main(String[] args) {
		SpringApplication.run(BotAppApplication.class, args);
	}

}
