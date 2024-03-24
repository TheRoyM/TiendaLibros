package TheRoy.tienda_libros;

import TheRoy.tienda_libros.Presentacion.LibroForm;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.awt.*;

@SpringBootApplication
public class TiendaLibrosApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext contextSpring =
		new SpringApplicationBuilder(TiendaLibrosApplication.class).
				headless(false).
				web(WebApplicationType.NONE).
				run(args);

		//codigo para cargar el formulario
		EventQueue.invokeLater(()->{
			//Obtengo el objeto form a traves de Spring
			LibroForm libroForm = contextSpring.getBean(LibroForm.class);
			libroForm.setVisible(true);
			libroForm.setLocationRelativeTo(null);
		});
	}

}
