/**
 * 
 */
package ec.com.dinersclub.dddmodules.infrastructure.extensiones;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Método de extension para strings
 *
 */
public final class ObjectExt {

	private ObjectExt() {

	}

	public static String convertirObjetToString(Object conectorAS400Entrada) {
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonIn = "";
		try {
			return objectMapper.writeValueAsString(conectorAS400Entrada);
		} catch (JsonProcessingException e) {
			return jsonIn;
		}
	}

}
