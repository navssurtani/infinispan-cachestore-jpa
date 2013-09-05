package org.infinispan.loaders.jpa;

import junit.framework.Assert;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.infinispan.loaders.jpa.configuration.JpaCacheStoreConfiguration;
import org.infinispan.loaders.jpa.configuration.JpaCacheStoreConfigurationBuilder;
import org.infinispan.loaders.jpa.entity.Vehicle;
import org.infinispan.loaders.jpa.entity.VehicleId;
import org.infinispan.loaders.spi.CacheStore;
import org.infinispan.test.fwk.TestCacheManagerFactory;
import org.testng.annotations.Test;

/**
 * 
 * @author <a href="mailto:rtsang@redhat.com">Ray Tsang</a>
 *
 */
@Test (groups = "functional", testName = "loaders.jdbc.binary.JpaCacheStoreEmfTest")
public class JpaCacheStoreVehicleEntityTest extends BaseJpaCacheStoreTest {

	@Override
	protected CacheStore createCacheStore() throws Exception {
      JpaCacheStoreConfiguration storeConfiguration = TestCacheManagerFactory.getDefaultCacheConfiguration(false)
            .loaders()
               .addLoader(JpaCacheStoreConfigurationBuilder.class)
                  .persistenceUnitName("org.infinispan.loaders.jpa")
                  .entityClass(Vehicle.class)
               .purgeSynchronously(true)
               .create();

		JpaCacheStore store = new JpaCacheStore();
		store.init(storeConfiguration, cm.getCache(), getMarshaller());
		store.start();

      Assert.assertNotNull(store.getEntityManagerFactory());
      Assert.assertTrue(store.getEntityManagerFactory() instanceof  HibernateEntityManagerFactory);
		return store;
	}

	@Override
	protected TestObject createTestObject(String key) {
		VehicleId id = new VehicleId("CA" + key, key);
		Vehicle v = new Vehicle();
		v.setId(id);
		v.setColor("c_" + key);
		
		return new TestObject(v.getId(), v);
	}
}
