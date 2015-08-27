package org.anderes.edu.dbunitburner.sample;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.anderes.edu.dbunitburner.DbUnitRule;
import org.anderes.edu.dbunitburner.DbUnitRule.ShouldMatchDataSet;
import org.anderes.edu.dbunitburner.DbUnitRule.UsingDataSet;
import org.anderes.edu.dbunitburner.sample.data.Recipe;
import org.anderes.edu.dbunitburner.sample.data.RecipeRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/sample/application-context.xml", "classpath:/sample/rule-customfactory-context.xml" })
public class WithCustomFactoryIT {

    @Inject
    private RecipeRepository repository;
    @Inject
    private EntityManager manager;
    
    @Inject @Rule 
    public DbUnitRule dbUnitRule;
   
    @Before
    public void setup() {
    }
    
    @After
    public void tearDown() {
        manager.clear();
    }
    
    @Test
    @UsingDataSet(value = { "/sample/prepare-custom.json" })
    @ShouldMatchDataSet(
            value = { "/sample/prepare-custom.json" },
            orderBy = { "RECIPE.UUID", "INGREDIENT.ID" })
    public void shouldBeFindAll() {
        Iterable<Recipe> recipes = repository.findAll();
        assertThat(recipes, is(notNullValue()));
        assertThat(recipes.iterator().hasNext(), is(true));
        int counter = 0;
        for (Recipe recipe : recipes) {
            assertThat(recipe.getTitle(), is(notNullValue()));
            counter++;
        }
        assertThat(counter, is(2));
    }
    
}
