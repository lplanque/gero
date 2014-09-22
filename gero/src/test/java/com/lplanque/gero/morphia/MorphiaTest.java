package com.lplanque.gero.morphia;

import static org.junit.Assert.assertEquals;

import javax.json.JsonValue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;

import com.lplanque.gero.model.Bean;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

public class MorphiaTest {

	private static MongoServer server;
	private static MongoClient client;
	private static Datastore   store;
	
	@BeforeClass public static void before() {
		
		// Creates client and server for tests
		server = new MongoServer(new MemoryBackend());
		client = new MongoClient(new ServerAddress(server.bind()));
		
		// Our ODM !
		Morphia morphia = new Morphia();
		morphia.map(Bean.class);
		
		store = morphia.createDatastore(client, "test");
	}
	
	@AfterClass public static void after() { // Close all !
		client.close();
		server.shutdownNow();
	}
	
	
	private void testJsonValue(JsonValue value) {
		final Bean x = new Bean(value);
		Key<Bean> key = store.save(x);
		final Bean y = store.getByKey(Bean.class, key);
		assertEquals(x, y);
	}
	
	@Test public void go() { // Tests for all kind of JsonValue...
		
		testJsonValue(JsonValue.NULL);
		testJsonValue(JsonValue.TRUE);
		testJsonValue(JsonValue.FALSE);
		
		testJsonValue(MorphiaJsonStringBijectionTest.X);
		testJsonValue(MorphiaJsonNumberBijectionTest.X);
		testJsonValue(MorphiaJsonArrayBijectionTest.X);
		testJsonValue(MorphiaJsonObjectBijectionTest.X);
	}
}
