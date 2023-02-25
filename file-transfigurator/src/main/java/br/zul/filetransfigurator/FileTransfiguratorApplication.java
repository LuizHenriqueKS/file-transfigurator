package br.zul.filetransfigurator;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.zul.filetransfigurator.core.ColorUtils;
import java.awt.Color;
import lombok.extern.log4j.Log4j2;

@SpringBootApplication
@Log4j2
public class FileTransfiguratorApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(FileTransfiguratorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		test(Byte.MIN_VALUE);
		test(Byte.MAX_VALUE);
	}

	private void test(byte a) {
		Color b = ColorUtils.getColor(a);
		byte c = ColorUtils.getByte(b);
		log.info("{} = {}", a, c);
	}

}
