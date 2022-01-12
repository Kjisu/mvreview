package org.zerock.mvreview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MvreviewApplication {

	public static void main(String[] args) {
		SpringApplication.run(MvreviewApplication.class, args);
	}

}
