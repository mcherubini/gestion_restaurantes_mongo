package agenda;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.lt;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;

public class AgendaRestaurantes {

	private static MongoClient mongoClient;
	private static MongoDatabase mongoDatabase;
	private static MongoCollection<Document> collection;
	private static Scanner scan = new Scanner(System.in);
	private static boolean selectDB = false;
	private static boolean selectCollec = false;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		mongoClient = new MongoClient("192.168.1.52");
		//-------------------- MENU BASE DE  DATOS-----------------------------
		while(!selectDB) {
			
			System.out.println("Elige una opción: \n" +"1.Listar bases de datos \n"
					+ "2.Crear base de datos/seleccionar \n"+
					"3.Salir");
			/*
			 * Todos los input utilizan el metodo nextLine ya que cada vez que se introduce un enter
			 * se crea un salto de linea, aunque se utilice un nextInt o cualquier otro input
			 * almacenara el salto de linea en un buffer hasta que llame un nextLine y pueda
			 * vaciar el buffer en el, por ello de esta manera nos aseguramos de limpiar el buffer
			 * y no ignore ninguno
			 */
			int input = 3;//salir por defecto
			
			try {
				
				input = Integer.parseInt(scan.nextLine());
			}catch(Exception e) {}
			
			switch(input) {
			
			case 1:
				listarBBDD();
				break;
			case 2:
				seleccionarBD();
				selectDB = true;	
				break;
			case 3:
				scan.close();
				mongoClient.close();
				System.exit(0);
			default:
				System.out.println("Opcion incorrecta, introduce valor válido");
				break;
			}
			
		}//while
		//-------------------------MENU COLECCIONES--------------------------------
		while(!selectCollec) {
			System.out.println("Elige una opción: \n" +"1.Listar colecciones \n"
					+ "2.Crear coleccion/seleccionar \n"+
					"3.Salir");
			
			int input = 3;//salir por defecto
			try {
				input = Integer.parseInt(scan.nextLine());
			}catch(Exception e) {}
			
			switch(input) {
			
			case 1:
				listarColecciones();
				break;
			case 2:
				seleccionarColeccion();
				selectCollec = true;
				break;
			case 3:
				scan.close();
				mongoClient.close();
				System.exit(0);
			default:
				System.out.println("Opcion incorrecta, introduce valor válido");
				break;
			}
			
		}//while
		//---------------------------MENU OPERACIONES DOCUMENTOS------------------------------------
		while(true) {
			System.out.println("Elige una opción: \n" +"1.Listar campos \n"
					+ "2.listar todos los documentos \n"+
					"3.Buscar \n"+
					"4.Insertar \n"+
					"5.Actualizar \n"+
					"6.Borrar \n" +
					"7.Listar colecciones \n"+
					"8.Seleccionar coleccion \n"+
					"9.Salir");
			
			int input = 9;//salir por defecto
			try {
				input = Integer.parseInt(scan.nextLine());
			}catch(Exception e) {}
			
			switch(input) {
			case 1:
				listarCampos();
				break;
			case 2:
				listarTodosLosDocumentos();
				break;
			case 3:
				buscarMultipleConFiltro();
				break;
			case 4:
				insertarDocumento();
				break;
			case 5:
				actualizarMuchosDocumentos();
				break;
			case 6:
				borrarMuchosDocumentos();
				break;
			case 7:
				listarColecciones();
				break;
			case 8:
				seleccionarColeccion();
				break;
			case 9:
				scan.close();
				mongoClient.close();
				System.exit(0);
			default:
				System.out.println("Opcion incorrecta, introduce valor válido");
				break;
			}
		}//while
		
	}//main
	
	
	private static void listarBBDD() {
		MongoCursor<String> dbsCursor = mongoClient.listDatabaseNames().iterator();
		while(dbsCursor.hasNext()) {
		    System.out.println(dbsCursor.next());
		}
	}//listarBBDD
	
	private static void seleccionarBD() {
		System.out.println("Selecciona base de datos");
		String db = scan.nextLine();
		mongoDatabase = mongoClient.getDatabase(db);
	}//seleccionarBD
	
	private static void listarColecciones() {
		MongoCursor<String> collections = mongoDatabase.listCollectionNames().iterator();
		
        while(collections.hasNext()) {
            System.out.println(collections.next().toString());
        }
	}//listarColecciones
	
	private static void seleccionarColeccion() {
		System.out.println("Selecciona coleccion");
		String coleccion = scan.nextLine();
		collection = mongoDatabase.getCollection(coleccion);
	}//seleccionarColeccion
	
	private static void listarCampos() {
		MongoCursor<Document> documents = collection.find().iterator();
		Set<String> key = new HashSet<String>();
		
		while(documents.hasNext()) {
			Document doc = documents.next();
			key.add(doc.keySet().toString());
		}
		System.out.println(key);
	}//listarCampos
	
	private static void listarTodosLosDocumentos() {
		MongoCursor<Document> documents = collection.find().iterator();
		Set<String> key = new HashSet<String>();
		
		while(documents.hasNext()) {
			Document doc = documents.next();
			System.out.println(doc.toJson());
			
		}
		System.out.println(key);
	}//listarTodosLosDocumentos
	
	private static Document crearDocumento() {
		boolean anadiendo = true;
		Document docNuevo = new Document();
		
		while(anadiendo) {
			System.out.println("Inserte campo a insertar");
			String campoNuevo = scan.nextLine();
			System.out.println("Inserte valor a insertar");
			String valorNuevo = scan.nextLine();
			
			docNuevo.append(campoNuevo, valorNuevo);
			
			System.out.println("Quiere añadir otro campo?:y/N");
			String opcion = scan.nextLine();
			if(opcion.equals("Y") || opcion.equals("y"))
				System.out.println("Entonces se añade un nuevo campo");
			else {
				anadiendo = false;
				System.out.println("El documento nuevo es el siguiente: \n"+
				docNuevo.toJson());
			}
		}
			return docNuevo;
	}//crearDocumento
	
	private static void buscarMultipleConFiltro() {
		System.out.println("Introduzca campo a buscar");
		String campo = scan.nextLine();
		
		System.out.println("Introduzca valor a buscar:");
		
		String valor = scan.nextLine();
		
		System.out.println(campo);
		System.out.println(valor);
		MongoCursor<Document> documents = collection.find(eq(campo, valor)).iterator();
		while(documents.hasNext()) {
			System.out.println(documents.next().toJson());
		}	
	}//buscarMultipleConFiltro
	
	private static void insertarDocumento() {
		
		System.out.println("Creando documento nuevo:");
		Document docNuevo = crearDocumento();
		collection.insertOne(docNuevo);
	}//insertarDocumento
	
	private static void actualizarMuchosDocumentos() {
		boolean anadiendo = true;
		String campoBuscar ="";
		String valorBuscar = "";
		Document docNuevo;
		
		System.out.println("Inserte campo a buscar:");
		campoBuscar = scan.nextLine();
		System.out.println("Inserte valor a buscar:");
		valorBuscar = scan.nextLine();
		
		System.out.println("Creando documento nuevo a reemplazar:");
		
		docNuevo = crearDocumento();
		
		collection.updateMany(eq(campoBuscar, valorBuscar), 
				new Document("$set", docNuevo));
	}//actualizarMuchosDocumentos

	private static void borrarMuchosDocumentos() {
		String campoBuscar ="";
		String valorBuscar = "";
		
		System.out.println("Inserte campo a buscar:");
		campoBuscar = scan.nextLine();
		System.out.println("Inserte valor a buscar:");
		valorBuscar = scan.nextLine();
		
		collection.deleteMany(eq(campoBuscar,valorBuscar));
		System.out.println("Documentos borrados");
	}//borrarMuchosDocumentos
	
}
