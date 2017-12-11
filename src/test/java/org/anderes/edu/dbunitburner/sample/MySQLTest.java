package org.anderes.edu.dbunitburner.sample;

import static org.eclipse.persistence.config.PersistenceUnitProperties.ECLIPSELINK_PERSISTENCE_XML;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.anderes.edu.dbunitburner.DbUnitRule;
import org.anderes.edu.dbunitburner.DbUnitRule.ShouldMatchDataSet;
import org.anderes.edu.dbunitburner.DbUnitRule.UsingDataSet;
import org.anderes.edu.dbunitburner.sample.data.Recipe;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

@Ignore("Für diesen Test muss eine MySQL-Server-Instanz laufen und "
                + "auf dieser eine Datenbank mittels dem Script 'sql/mysql/mysql-createUserAndSchema.sql' ein DB-schema erstellt sein.")
public class MySQLTest {
    
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("mySqlPU", getProperties());
    private EntityManager manager;
    @Rule
    public DbUnitRule dbUnitRule = new DbUnitRule(getConnection());

    @Before
    public void setup() {
        manager = entityManagerFactory.createEntityManager();
    }
    
    @After
    public void tearDown() {
        manager.close();
    }

    @Test
    @UsingDataSet(value = { "/sample/prepareForMySql.json" })
    @ShouldMatchDataSet(
            value = { "/sample/prepare.json" },
            orderBy = { "RECIPE.UUID", "INGREDIENT.ID" })
    public void shouldBeFindAll() {
        
        // when
        final Iterable<Recipe> recipes = manager.createQuery("select r from Recipe r", Recipe.class).getResultList();
        
        // then
        assertThat(recipes, is(notNullValue()));
        assertThat(recipes.iterator().hasNext(), is(true));
        int counter = 0;
        for (Recipe recipe : recipes) {
            assertThat(recipe.getTitle(), is(notNullValue()));
            counter++;
        }
        assertThat(counter, is(2));
    }
    
    private static Map<String, String> getProperties() {
        final Map<String, String> properties = new HashMap<>(1);
        properties.put(ECLIPSELINK_PERSISTENCE_XML, "META-INF/mysql-persistence.xml");
        return properties;
    }
    
    private static Connection getConnection() {
        EntityManager manager = entityManagerFactory.createEntityManager();
        manager.getTransaction().begin();
        Connection connection = manager.unwrap(Connection.class);
        manager.getTransaction().commit();
        return connection;
    }
}
