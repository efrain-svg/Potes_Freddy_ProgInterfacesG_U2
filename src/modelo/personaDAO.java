package modelo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;


// Definicion de la clase publica "personaDAO"
public class personaDAO {

	private static final String CABECERA_NUEVA = "NOMBRE;TELEFONO;EMAIL;CATEGORIA;FAVORITO;FECHA_REGISTRO";
	private static final String CABECERA_ANTERIOR = "NOMBRE;TELEFONO;EMAIL;CATEGORIA;FAVORITO";
	private static final char SEPARADOR_EXPORTACION = ';';

	// Declaracion de atributos privados de la clase "personaDAO"
	private File archivo;
	private persona persona;

	// Constructor publico de la clase "personaDAO" que recibe un objeto "persona" como parametro
	public personaDAO(persona persona) {
		this.persona = persona;
		File base = new File("c:/gestionContactos");
		if (!base.exists()) {
			base.mkdirs();
		}
		archivo = new File(base.getAbsolutePath(), "datosContactos.csv");
		prepararArchivo();
	}

	// Metodo privado para gestionar el archivo utilizando la clase File
	private void prepararArchivo() {
		if (!archivo.exists()) {
			try {
				archivo.createNewFile();
				escribir(CABECERA_NUEVA);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void escribir(String texto) {
		try (FileWriter escribir = new FileWriter(archivo.getAbsolutePath(), true)) {
			escribir.write(texto + System.lineSeparator());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Metodo publico para escribir en el archivo
	public boolean escribirArchivo() {
		if (persona == null) {
			return false;
		}
		escribir(persona.datosContacto());
		return true;
	}

	// Metodo publico para leer los datos del archivo
	public List<persona> leerArchivo() throws IOException {
		List<persona> personas = new ArrayList<persona>();
		StringBuilder contenido = new StringBuilder();

		try (FileReader leer = new FileReader(archivo.getAbsolutePath())) {
			int c;
			while ((c = leer.read()) != -1) {
				contenido.append((char) c);
			}
		}

		String[] datos = contenido.toString().split("\\r?\\n");
		for (String contacto : datos) {
			if (contacto == null || contacto.trim().isEmpty()) {
				continue;
			}

			String registro = contacto.trim();
			if (CABECERA_NUEVA.equalsIgnoreCase(registro) || CABECERA_ANTERIOR.equalsIgnoreCase(registro)) {
				continue;
			}

			String[] columnas = registro.split(";");
			if (columnas.length < 5) {
				continue;
			}

			persona p = new persona();
			p.setNombre(columnas[0]);
			p.setTelefono(columnas[1]);
			p.setEmail(columnas[2]);
			p.setCategoria(normalizarCategoria(columnas[3]));
			p.setFavorito(Boolean.parseBoolean(columnas[4]));

			if (columnas.length >= 6) {
				try {
					p.setFechaRegistro(LocalDate.parse(columnas[5]));
				} catch (DateTimeParseException ex) {
					p.setFechaRegistro(LocalDate.now());
				}
			} else {
				p.setFechaRegistro(LocalDate.now());
			}

			personas.add(p);
		}

		return personas;
	}

	// Metodo publico para guardar los contactos modificados o eliminados
	public void actualizarContactos(List<persona> personas) throws IOException {
		try (PrintWriter writer = new PrintWriter(archivo)) {
			writer.println(CABECERA_NUEVA);
			for (persona p : personas) {
				writer.println(p.datosContacto());
			}
		}
	}

	public void exportarCsv(File destino, List<persona> personas) throws IOException {
		exportarCsv(destino, new String[] {
			"nombre", "telefono", "email", "categoria", "favorito", "fecha_registro"
		}, construirFilasPorDefecto(personas));
	}

	public void exportarCsv(File destino, String[] cabeceras, List<String[]> filas) throws IOException {
		if (destino == null) {
			throw new FileNotFoundException("No se especifico ruta de exportacion");
		}

		try (PrintWriter writer = new PrintWriter(destino)) {
			writer.println(armarLineaCsv(cabeceras));
			for (String[] fila : filas) {
				writer.println(armarLineaCsv(fila));
			}
		}
	}

	private List<String[]> construirFilasPorDefecto(List<persona> personas) {
		List<String[]> filas = new ArrayList<String[]>();
		for (persona p : personas) {
			filas.add(new String[] {
				p.getNombre(),
				p.getTelefono(),
				p.getEmail(),
				p.getCategoria(),
				String.valueOf(p.isFavorito()),
				String.valueOf(p.getFechaRegistro())
			});
		}
		return filas;
	}

	private String normalizarCategoria(String valor) {
		if (valor == null) {
			return "";
		}

		String limpio = valor.trim();
		if ("FAMILY".equalsIgnoreCase(limpio) || "Familia".equalsIgnoreCase(limpio) || "Family".equalsIgnoreCase(limpio)) {
			return "FAMILY";
		}
		if ("FRIENDS".equalsIgnoreCase(limpio) || "Amigos".equalsIgnoreCase(limpio) || "Friends".equalsIgnoreCase(limpio)) {
			return "FRIENDS";
		}
		if ("WORK".equalsIgnoreCase(limpio) || "Trabajo".equalsIgnoreCase(limpio) || "Trabalho".equalsIgnoreCase(limpio)) {
			return "WORK";
		}
		return limpio;
	}

	private String armarLineaCsv(String[] columnas) {
		StringBuilder linea = new StringBuilder();
		for (int i = 0; i < columnas.length; i++) {
			if (i > 0) {
				linea.append(SEPARADOR_EXPORTACION);
			}
			linea.append(escaparCsv(columnas[i]));
		}
		return linea.toString();
	}

	private String escaparCsv(String valor) {
		String limpio = valor == null ? "" : valor;
		if (limpio.indexOf(SEPARADOR_EXPORTACION) >= 0 || limpio.contains("\"") || limpio.contains("\n")) {
			limpio = limpio.replace("\"", "\"\"");
			return "\"" + limpio + "\"";
		}
		return limpio;
	}
}
