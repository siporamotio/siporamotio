
package de.htwberlin.Configuration;

import java.util.logging.Level;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

import de.htwberlin.kba_projekt_ui.api.KbaUiController;



/**
 * Konfiguration mit Dependency Injection mit Spring und Annotationen.
 *
 */

@Configuration
public class ConfigurationSpringAnnotation 
{
	

	
		private static ConfigurableApplicationContext context = new AnnotationConfigApplicationContext("de.htwberlin");

		public static void main(String[] args) {
			java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
			KbaUiController controller = context.getBean(KbaUiController.class);
			controller.run();
		}
	}

