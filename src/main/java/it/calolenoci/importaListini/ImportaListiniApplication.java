package it.calolenoci.importaListini;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class ImportaListiniApplication  implements CommandLineRunner{


	@Autowired
	private BatchImportatoreListini batchImportatoreListini;

	public static void main(String[] args) {
		SpringApplication.run(ImportaListiniApplication.class, args);
	}

	@Override
	public void run(final String... s) {
		batchImportatoreListini.execute();
	}

}
