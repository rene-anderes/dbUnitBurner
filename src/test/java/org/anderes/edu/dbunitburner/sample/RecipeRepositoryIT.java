package org.anderes.edu.dbunitburner.sample;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.anderes.edu.dbunitburner.DbUnitRule;
import org.anderes.edu.dbunitburner.DbUnitRule.CleanupUsingScript;
import org.anderes.edu.dbunitburner.DbUnitRule.ShouldMatchDataSet;
import org.anderes.edu.dbunitburner.DbUnitRule.UsingDataSet;
import org.anderes.edu.dbunitburner.DbUnitRule.UsingDataSetScript;
import org.anderes.edu.dbunitburner.sample.data.Ingredient;
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
@ContextConfiguration(locations = { "classpath:/sample/application-context.xml", "classpath:/sample/rule-context.xml" })
public class RecipeRepositoryIT {

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
    @UsingDataSet(value = { "/sample/prepare.json" })
    @ShouldMatchDataSet(
            value = { "/sample/prepare.json" },
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
    
    @Test
    @UsingDataSetScript(value = { "/sql/LoadTestdata.sql" })
    public void shouldBeFindAllTestdata() {
        Iterable<Recipe> recipes = repository.findAll();
        assertThat(recipes, is(notNullValue()));
        assertThat(recipes.iterator().hasNext(), is(true));
        int counter = 0;
        for (Recipe recipe : recipes) {
            assertThat(recipe.getTitle(), is(notNullValue()));
            counter++;
        }
        assertThat(counter, is(5));
    }
    
    @Test
    @UsingDataSet(value = { "/sample/prepare.xls" })
    public void shouldBeOneRecipe() {
        final Recipe recipe = repository.findOne("c0e5582e-252f-4e94-8a49-e12b4b047afb");
        assertNotNull(recipe);
        assertThat(recipe.getTitle(), is("Arabische Spaghetti"));
    }
    
    @Test
    @UsingDataSet(value = { "/sample/prepare.xls" })
    @ShouldMatchDataSet(
            value = { "/sample/prepare.xls" },
            orderBy = { "RECIPE.UUID", "INGREDIENT.ID" })
    public void getRecipesByTitle() {
        final Collection<Recipe> recipes = repository.findByTitleLike("%Spaghetti%");

        assertNotNull(recipes);
        assertThat(recipes.size(), is(1));
        final Recipe recipe = recipes.iterator().next();
        assertThat(recipe.getTitle(), is("Arabische Spaghetti"));
    }
    
    @Test
    @CleanupUsingScript(value = { "/sql/DeleteTableContentScript.sql" })
    @UsingDataSet(value = { "/sample/prepare.xls" })
    public void shouldBeSaveNewRecipe() {
        // given
        final Recipe newRecipe = RecipeBuilder.buildRecipe();
        
        // when
        final Recipe savedRecipe = repository.save(newRecipe);
        
        // then
        assertThat(savedRecipe, is(not(nullValue())));
        assertThat(savedRecipe.getUuid(), is(not(nullValue())));
        
        final Recipe findRecipe = repository.findOne(savedRecipe.getUuid());
        assertNotSame(newRecipe, findRecipe);
        assertThat(newRecipe, is(findRecipe));
    }
    
    @Test
    @UsingDataSet(value = { "/sample/prepare.xls" })
    @ShouldMatchDataSet(value = { "/sample/expected-afterUpdate.xls" },
            excludeColumns = { "INGREDIENT.ID" },
            orderBy = { "RECIPE.UUID", "INGREDIENT.ANNOTATION" }
    )
    public void shouldBeUpdateRecipe() {
        final Recipe updateRecipe = repository.findOne("c0e5582e-252f-4e94-8a49-e12b4b047afb");
        updateRecipe.setPreamble("Neuer Preamble vom Test");
        updateRecipe.addIngredient(new Ingredient("1", "Tomate", "vollreif"));
        final Recipe savedRecipe = repository.save(updateRecipe);
        
        assertThat(savedRecipe, is(not(nullValue())));
        assertThat(savedRecipe.getPreamble(), is("Neuer Preamble vom Test"));
        assertThat(savedRecipe.getIngredients().size(), is(4));
        
        final Recipe findRecipe = repository.findOne(savedRecipe.getUuid());
        assertThat(findRecipe, is(not(nullValue())));
        assertThat(findRecipe.getPreamble(), is("Neuer Preamble vom Test"));
        assertNotSame(updateRecipe, findRecipe);
        assertThat(updateRecipe, is(findRecipe));
    }
    
    @Test
    @UsingDataSet(value = { "/sample/prepare.xls" })
    @ShouldMatchDataSet(value = { "/sample/expected-afterDeleteOne.xls" },
            excludeColumns = { "RECIPE.ADDINGDATE" },
            orderBy = { "RECIPE.UUID", "INGREDIENT.ID" })
    public void shouldBeDelete() {
        final Recipe toDelete = repository.findOne("c0e5582e-252f-4e94-8a49-e12b4b047afb");
        assertThat("Das Rezept mit der ID 'c0e5582e-252f-4e94-8a49-e12b4b047afb' existiert nicht in der Datenbank", toDelete, is(not(nullValue())));
        repository.delete(toDelete);
        
        final Collection<Recipe> recipes = repository.findByTitleLike("%Spaghetti%");
        assertNotNull(recipes);
        assertThat(recipes.size(), is(0));
    }
    
    @Test
    @UsingDataSet(value = { "/sample/prepare.xls" })
    public void shouldBeFindAllTag() {
        final List<String> tags = repository.findAllTag();
        assertThat(tags, is(notNullValue()));
        assertThat(tags.size(), is(4));
    }
}
