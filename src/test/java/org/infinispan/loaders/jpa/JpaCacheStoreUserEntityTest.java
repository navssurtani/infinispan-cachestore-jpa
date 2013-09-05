package org.infinispan.loaders.jpa;

import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.infinispan.loaders.jpa.configuration.JpaCacheStoreConfiguration;
import org.infinispan.loaders.jpa.configuration.JpaCacheStoreConfigurationBuilder;
import org.infinispan.loaders.jpa.entity.User;
import org.infinispan.loaders.spi.CacheStore;
import org.infinispan.manager.CacheContainer;
import org.infinispan.test.TestingUtil;
import org.infinispan.test.fwk.TestCacheManagerFactory;
import org.testng.annotations.Test;

/**
 *
 * @author <a href="mailto:rtsang@redhat.com">Ray Tsang</a>
 *
 */
@Test (groups = "functional", testName = "loaders.jdbc.binary.JpaCacheStoreEmfTest")
public class JpaCacheStoreUserEntityTest extends BaseJpaCacheStoreTest {

	@Override
	protected CacheStore createCacheStore() throws Exception {

      JpaCacheStoreConfiguration storeConfiguration = TestCacheManagerFactory.getDefaultCacheConfiguration(false)
            .loaders()
               .addLoader(JpaCacheStoreConfigurationBuilder.class)
                  .persistenceUnitName("org.infinispan.loaders.jpa")
                  .entityClass(User.class)
                  .purgeSynchronously(true)
               .create();


		JpaCacheStore store = new JpaCacheStore();
		store.init(storeConfiguration, cm.getCache(), getMarshaller());
		store.start();

		assert store.getEntityManagerFactory() != null;
		assert store.getEntityManagerFactory() instanceof HibernateEntityManagerFactory;

		return store;
	}

	public void testSimple() throws Exception {
		CacheContainer cm = null;
		try {
			assert cs.getConfiguration() instanceof JpaCacheStoreConfig;
		} finally {
			TestingUtil.killCacheManagers(cm);
		}
	}

	@Override
	protected TestObject createTestObject(String suffix) {
		User user = new User();
		user.setUsername("u_" + suffix);
		user.setFirstName("fn_" + suffix);
		user.setLastName("ln_" + suffix);
		user.setNote("Some notes " + suffix);

		return new TestObject(user.getUsername(), user);
	}
}
