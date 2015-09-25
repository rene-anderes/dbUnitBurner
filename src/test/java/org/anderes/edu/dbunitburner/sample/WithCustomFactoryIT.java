package org.anderes.edu.dbunitburner.sample;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.text.ParseException;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.anderes.edu.dbunitburner.DbUnitRule;
import org.anderes.edu.dbunitburner.DbUnitRule.ShouldMatchDataSet;
import org.anderes.edu.dbunitburner.DbUnitRule.UsingDataSet;
import org.anderes.edu.dbunitburner.sample.data.Recipe;
import org.anderes.edu.dbunitburner.sample.data.RecipeRepository;
import org.apache.commons.lang3.time.DateParser;
import org.apache.commons.lang3.time.FastDateFormat;
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

    private static final DateParser DATETIME_PARSER = FastDateFormat.getInstance("dd.MM.yyyy HH:mm:ss"); 
    private static final DateParser DATE_PARSER = FastDateFormat.getInstance("dd.MM.yyyy");
    
    @Inject
    private RecipeRepository repository;
    @Inject
    private EntityManager manager;
    
    @Inject @Rule 
    public DbUnitRule dbUnitRule;
   
    @Before
    public void setup() {
        manager.clear();
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
    public void shouldBeFindOne() throws ParseException {
        Recipe recipe = repository.findOne("c0e5582e-252f-4e94-8a49-e12b4b047afb");
        assertThat(recipe, is(notNullValue()));
        assertThat(recipe.getAddingDate(), is(notNullValue()));
        assertThat(recipe.getAddingDate(), is(DATETIME_PARSER.parse("22.01.2014 23:03:20")));
        assertThat(recipe.getLastUpdate(), is(notNullValue()));
        assertThat(recipe.getLastUpdate(), is(DATETIME_PARSER.parse("22.01.2014 23:03:20")));
        assertThat(recipe.getImage().getImageDate(), is(notNullValue()));
        assertThat(recipe.getImage().getImageDate(), is(DATE_PARSER.parse("20.1.2014")));
        
        recipe = repository.findOne("adf99b55-4804-4398-af4e-e37ec2c692c7");
        assertThat(recipe, is(notNullValue()));
        assertThat(recipe.getAddingDate(), is(notNullValue()));
        assertThat(recipe.getAddingDate(), is(DATETIME_PARSER.parse("23.01.2015 20:03:55")));
        assertThat(recipe.getLastUpdate(), is(notNullValue()));
        assertThat(recipe.getLastUpdate(), is(DATETIME_PARSER.parse("31.01.2014 23:03:20")));
        assertThat(recipe.getImage().getImageDate(), is(notNullValue()));
        assertThat(recipe.getImage().getImageDate(), is(DATE_PARSER.parse("01.01.2014")));
    }
    
}
