# dbUnitBurner

Diese [JUnit-Rule](https://github.com/junit-team/junit4/wiki/rules) bietet die Möglichkeit mittels Annotations die für den Test notwendigen DB-Files (XML, CSV, XLS und JSON) anzugeben:
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
Wie das Beispiel zeigt können Sortierung und Ausnahmen für den Vergleich definiert werden.
Format: [Tabellenname].[Spaltennamen]
 
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
Als Basis für diese JUnit-Rule dient [DBUnit](http://dbunit.sourceforge.net/)

Die Annotierungen `@UsingDataSet`, `@UsingDataSetScript` und `@CleanupUsingScript` können sowohl für eine Methode als auch für eine Klasse gesetzt werden. Werden die Annotierungen auf der Klasse angebracht, so wird für jede Test-Methode die entsprechenden Files verwendet.

Beispiel JUnit-Test
```
@UsingDataSet(value = { prepare.json })
public class ProcessFacadeIT {

    @Rule
    public DbUnitRule dbunitRule = new DbUnitRule(Persistence.createEntityManagerFactory("pu").getConnection());

    @Test
    @ShouldMatchDataSet(value = { "ProcessAfterRemove.json" }
    public void addProcess() {
        ...
    }

}
```

<h3>Weiter Dokumentationen siehe im Wiki: https://github.com/rene-anderes/dbUnitBurner/wiki</h3>

[![](https://jitpack.io/v/rene-anderes/dbUnitBurner.svg)](https://jitpack.io/#rene-anderes/dbUnitBurner)


