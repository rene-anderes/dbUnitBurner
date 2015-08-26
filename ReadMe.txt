dbUnitBurner

Diese JUnit-Rule bietet die Möglichkeit mittels Annotations die für den Test notwendigen DB-Files (in den von DBUnit unterstützten Formaten) anzugeben:
   @UsingDataSet(value = { "/prepare.xls" })
   
Es können auch mehrere Files angegeben werden:
   @UsingDataSet(value = { "/Person.xls", "Offer.xls" })
 
Ebenfalls können das oder die Files angegeben werden mittels denen DBUnit die Datenbank nach der Test-Methode überprüft:
   @ShouldMatchDataSet(
        value = { "/expectedAfterDelete.xls" },
        excludeColumns = { "INGREDIENT.ID" },
        orderBy = { "RECIPE.UUID", "INGREDIENT.ID" })
 
Wie das Beispiel zeigt können Sortierung und Ausnahmen für den Vergleich definiert werden. Format: [Tabellenname].[Spaltennamen]
 
Wenn gewünscht, kann auch ein SQL-Script angegeben werden, mittels dem die Tabelle(n) vor der Test-Methode gelöscht werden.
   @CleanupUsingScript(value = { "/sql/DeleteTableContentScript.sql" })

Es wird im Klassenpfad nach den entsprechenden Files gesucht.
Als Basis für diese JUnit-Rule dient DBUnit ({@link siehe http://dbunit.sourceforge.net/})

Die Annotierungen @UsingDataSet und @CleanupUsingScript können sowohl für eine Methode als auch für eine Klasse gesetzt werden. Werden die Annotierungen auf der Klasse angebracht, so wird für jede Test-Methode die entsprechenden Files verwendet.

