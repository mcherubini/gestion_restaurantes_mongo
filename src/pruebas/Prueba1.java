package pruebas;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.mongodb.Block;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.mongodb.Block;

public class Prueba1 {

	private static MongoClient mongoClient;
	private static MongoDatabase database;
	
	
	public static void main(String[] args) {
		
		MongoCollection<Document> collection = conectar("192.168.1.52","test","restaurants");
		//insertar();
		
		System.out.println(collection.count());
		Document doc = new Document("name", "MongoDB")
                .append("type", "database")
                .append("count", 1)
                .append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
                .append("info", new Document("x", 203).append("y", 102));
		
		//insertar(doc,"restaurants");
		//buscarPrimerResultado();
		//listarTodo();
		//insertarMultiple();
		//buscarConFiltro(collection);
		//listarBBDD();
		//listarColecciones();
		//listarTodosLosDocumentos("restaurants");
		//listarCampos("restaurants");
		buscarMultipleConFiltro("name","Credit Suisse","restaurants");
		/*MongoCursor<Document> prueba = collection.find(eq("address.name", "Luigis Pizzeria")).iterator();
		while(prueba.hasNext())
			System.out.println(prueba.next().toJson());*/
	}
	
	
	private static void listarBBDD() {
		MongoCursor<String> dbsCursor = mongoClient.listDatabaseNames().iterator();
		while(dbsCursor.hasNext()) {
		    System.out.println(dbsCursor.next());
		}
	}
	
	private static void listarColecciones() {
		MongoCursor<String> collections = database.listCollectionNames().iterator();
		
        while(collections.hasNext()) {
            System.out.println(collections.next());
        }
	}
	
	private static MongoCollection<Document> conectar(String ip,String db,String coleccion){
		mongoClient = new MongoClient(ip);
		database = mongoClient.getDatabase(db);
		return database.getCollection(coleccion);
	}
	
	private static void listarCampos(String coleccion) {
		MongoCursor<Document> documents = database.getCollection(coleccion).find().iterator();
		Set<String> key = new HashSet<String>();
		
		while(documents.hasNext()) {
			Document doc = documents.next();
			key.add(doc.keySet().toString());
		}
		System.out.println(key);
	}
	
	private static void listarTodosLosDocumentos(String coleccion) {
		MongoCursor<Document> documents = database.getCollection(coleccion).find().iterator();
		Set<String> key = new HashSet<String>();
		
		while(documents.hasNext()) {
			Document doc = documents.next();
			System.out.println(doc.toJson());
			
		}
		System.out.println(key);
	}
	
	private static void filtrarPorRango(String campo1,String valor1,String campo2,String valor2, String coleccion) {
		MongoCursor<Document> documentos = 
				database.getCollection(coleccion).find(and(gt(campo1, valor1), lte(campo2, valor2))).iterator();
		while(documentos.hasNext()) {
			System.out.println(documentos.next().toJson());
		}
	}
	
	private static void buscarMultipleConFiltro(String campo,String valor,String coleccion) {
		MongoCursor<Document> documents = database.getCollection(coleccion).find(eq(campo, valor)).iterator();
		while(documents.hasNext()) {
			System.out.println(documents.next().toJson());
		}
			
	}
	
	private static void buscarSingleConFiltro(String campo,String valor,String coleccion) {
		Document myDoc = database.getCollection(coleccion).find(eq(campo, valor)).first();
		if(myDoc != null) {
			System.out.println(myDoc.toJson());
		}
		
	}
	
	private static void borrarMuchosDocumentos(String campoBuscar,String valorBuscar,String coleccion) {
		database.getCollection(coleccion).deleteMany(lt(campoBuscar,valorBuscar));
	}
	
	private static void borrarUnDocumentos(String campoBuscar,String valorBuscar,String coleccion) {
		database.getCollection(coleccion).deleteOne(lt(campoBuscar,valorBuscar));
	}
	
	private static void actualizarMuchosDocumentos(String campoBuscar,String valorBuscar,Document docNuevo,
			String coleccion) {
		UpdateResult updateResult = database.getCollection(coleccion).updateMany(lt(campoBuscar, docNuevo), 
				new Document("$set", docNuevo));
	}
	
	private static void actualizarUnDocumento(String campoBuscar,String valorBuscar,Document docNuevo,
			String coleccion) {
		database.getCollection(coleccion).updateOne(eq(campoBuscar, valorBuscar),
				new Document("$set", docNuevo));
	}
	
	private static void insertar(Document doc,String coleccion) {
		MongoCollection<Document> collection = database.getCollection(coleccion);
		collection.insertOne(doc);
	}
	
	

}
