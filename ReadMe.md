# dbUnitBurner

Diese JUnit-Rule bietet die Möglichkeit mittels Annotations die für den Test notwendigen DB-Files (in den von DBUnit unterstützten Formaten) anzugeben:
   ```
   @UsingDataSet(value = { "/prepare.xls" })
   ```
   
Es können auch mehrere Files angegeben werden:
```
   @UsingDataSet(value = { "/Person.xls", "Offer.xls" })
```
Ebenfalls können das oder die Files angegeben werden mittels denen DBUnit die Datenbank nach der Test-Methode überprüft:
```
   @ShouldMatchDataSet(
        value = { "/expectedAfterDelete.xls" },
        excludeColumns = { "INGREDIENT.ID" },
        orderBy = { "RECIPE.UUID", "INGREDIENT.ID" })
```
Wie das Beispiel zeigt können Sortierung und Ausnahmen für den Vergleich definiert werden. Format: [Tabellenname].[Spaltennamen]
 
Wenn gewünscht, kann auch ein SQL-Script angegeben werden, mittels dem die Tabelle(n) vor der Test-Methode gelöscht werden.
```
   @CleanupUsingScript(value = { "/sql/DeleteTableContentScript.sql" })
```
Es ist auch möglich, dass für das Laden von Testdaten ein (oder mehrere) SQl-Script(s) verwendet wird:
```
  @UsingDataSetScript(value = { "/sql/LoadTestdata.sql" })
```
Die SQL-Scripts sollten im UTF-8 Format (ohne BOM) vorliegen. Eine Zeile im SQL-Script entspricht einem SQL-Kommando. Ein Delimiter wie z.B. ';' ist nicht notwendig.

Es wird im Klassenpfad nach den entsprechenden Files gesucht.
Als Basis für diese JUnit-Rule dient DBUnit (siehe http://dbunit.sourceforge.net/)

Die Annotierungen @UsingDataSet, @UsingDataSetScript und @CleanupUsingScript können sowohl für eine Methode als auch für eine Klasse gesetzt werden. Werden die Annotierungen auf der Klasse angebracht, so wird für jede Test-Methode die entsprechenden Files verwendet.

### Data-Type-Factory
Mittels der Data-Type-Factory ```CustomDataTypeFactory``` ist es möglich ein Zeitstempel (Timestamp) oder Datum (Date) in folgenden Formaten im Datenbankfile abzulegen:
```
    dd.MM.yyyy HH:mm:ss     (Nanosekunden sind 0)
    dd.MM.yyyy HH:mm        (Sekunden und Nanosekunden sind 0)
    dd.MM.yyyy              (Stunden, Minuten, Sekunden und Nanosekunden sind 0)
```
### JSON
Neben den von DBUnit unterstützen Formaten (XML, CSV und XLS Dateien) können auch JSON-Files eingesetzt werden um die Datenbank zu befüllen.
Beispiel mit drei Tabellen (RECIPE, TAGS und INGREDIENT)
```
{
	"RECIPE": [{
		"UUID": "c0e5582e-252f-4e94-8a49-e12b4b047afb",
		"LASTUPDATE": "2014-01-22 23:03:20",
		"ADDINGDATE": "2014-01-22 23:03:20",
		"NOOFPERSON": 2,
		"PREAMBLE": "<p>Da bei diesem Rezept das Scharfe (Curry) mit dem Süssen (Sultaninen) gemischt wird, habe ich diese Rezept \"Arabische Spaghetti\" benannt.</p>",
		"PREPARATION": "<p>Pouletfleisch in schmale Streifen schneiden und kurz anbraten. Aus der Pfanne nehmen und bei Seite stellen.<br />\n<br />\nWasser aufsetzen, salzen und Spaghetti al dente kochen.<br",
		"TITLE": "Arabische Spaghetti",
		"IMAGE_DESCRIPTION": null,
		"IMAGE_URL": null,
		"RATING": 4,
		"VERSION": 1
	},
	{
		"UUID": "adf99b55-4804-4398-af4e-e37ec2c692c7",
		"LASTUPDATE": "2014-01-31 23:03:20",
		"ADDINGDATE": "2015-01-23 23:03:20",
		"NOOFPERSON": 4,
		"PREAMBLE": null,
		"PREPARATION": "<p>Die Zitrone auspressen und den Saft, mit dem Öl, Salz, Pfeffer und Curry in eine Schüssel geben und verrühren. Die Avocados in Würfelchen schneiden, nicht zu klein damit es kein Mus ",
		"TITLE": "Avocadotatar",
		"IMAGE_DESCRIPTION": null,
		"IMAGE_URL": null,
		"RATING": 5,
		"VERSION": 1
	}],
	"TAGS": [{
		"RECIPE_ID": "c0e5582e-252f-4e94-8a49-e12b4b047afb",
		"TAGS": "pasta"
	},
	{
		"RECIPE_ID": "c0e5582e-252f-4e94-8a49-e12b4b047afb",
		"TAGS": "fleisch"
	},
	{
		"RECIPE_ID": "adf99b55-4804-4398-af4e-e37ec2c692c7",
		"TAGS": "kalt"
	},
	{
		"RECIPE_ID": "adf99b55-4804-4398-af4e-e37ec2c692c7",
		"TAGS": "vorspeise"
	}],
	"INGREDIENT": [{
		"ID": 203,
		"ANNOTATION": null,
		"DESCRIPTION": "Zucker",
		"QUANTITY": "50g",
		"RECIPE_ID":"adf99b55-4804-4398-af4e-e37ec2c692c7" 
	},
	{
		"ID": 100,
		"ANNOTATION": null,
		"DESCRIPTION": "Spaghetti",
		"QUANTITY": "250g",
		"RECIPE_ID":"c0e5582e-252f-4e94-8a49-e12b4b047afb" 
	},
	{
		"ID": 101,
		"ANNOTATION": "Bruststückez",
		"DESCRIPTION": "Poulet",
		"QUANTITY": "200-300g",
		"RECIPE_ID":"c0e5582e-252f-4e94-8a49-e12b4b047afb" 
	},
	{
		"ID": 102,
		"ANNOTATION": "oder Lauch",
		"DESCRIPTION": "Frühlingszwiebeln",
		"QUANTITY": "4-5",
		"RECIPE_ID":"c0e5582e-252f-4e94-8a49-e12b4b047afb" 
	},
	{
		"ID": 200,
		"ANNOTATION": "gelb oder rot",
		"DESCRIPTION": "Peproni",
		"QUANTITY": "2-3",
		"RECIPE_ID":"adf99b55-4804-4398-af4e-e37ec2c692c7" 
	},
	{
		"ID": 201,
		"ANNOTATION": null,
		"DESCRIPTION": "Tomate",
		"QUANTITY": "1",
		"RECIPE_ID":"adf99b55-4804-4398-af4e-e37ec2c692c7" 
	},
	{
		"ID": 202,
		"ANNOTATION": null,
		"DESCRIPTION": "Peperoncini",
		"QUANTITY": "1",
		"RECIPE_ID":"adf99b55-4804-4398-af4e-e37ec2c692c7" 
	}]
}
```

