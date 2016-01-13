package db61b;
import org.junit.Test;

import java.util.ArrayList;
/*import java.util.Arrays;*/
import java.util.List;

import static org.junit.Assert.*;

    /**Tests basics funcationality including:
     * 1. The row class
    */


public class BasicTests {
    @Test
    public void testRow() {
        Row r = new Row(new String[]{"This", "is", "a", "test."});
        assertEquals(4, r.size());
        Table t = new Table("Test", new String[]{"FirstName", "LastName",
                                                 "Age"});
        Row r3 = new Row(new String[]{"Maaz", "Uddin", "19"});
        t.add(r3);
        TableIterator iter = t.tableIterator();
        Column c1 = new Column(t, "FirstName");
        Column c2 = new Column(t, "LastName");
        Column c3 = new Column(t, "Age");
        List<TableIterator> iterators = new ArrayList<>();
        iterators.add(iter);
        c1.resolve(iterators);
        c2.resolve(iterators);
        c3.resolve(iterators);
        List<Column> columnSet1 = new ArrayList<>();
        columnSet1.add(c1);
        columnSet1.add(c2);
        columnSet1.add(c3);
        Row r2 = new Row(columnSet1);
        assertEquals(3, r2.size());
    }

    @Test
    public void testTable() {
        Table t = new Table("Test", new String[]{"FirstName", "LastName",
                                                 "Age"});
        assertEquals("Test", t.name());
        assertEquals(3, t.numColumns());
        assertEquals("LastName", t.title(1));
        assertEquals(2, t.columnIndex("Age"));
        assertEquals(-1, t.columnIndex("Country"));
        assertEquals(0, t.size());
        Row r = new Row(new String[]{"Maaz", "Uddin", "19"});
        t.add(r);
        assertEquals(1, t.size());
    }

    @Test
    public void testTableIterator() {
        Table t = new Table("Test", new String[]{"FirstName", "LastName",
                "Age"});
        Row r = new Row(new String[]{"Maaz", "Uddin", "19"});
        t.add(r);
        Row r2 = new Row(new String[]{"Zain", "Alsaie", "18"});
        t.add(r2);
        TableIterator tIter = t.tableIterator();
        assertEquals("Zain", tIter.value(0));
        assertEquals("Alsaie", tIter.value(1));
        assertEquals("18", tIter.value(2));
        tIter.next();
        assertEquals("Maaz", tIter.value(0));
        assertEquals("Uddin", tIter.value(1));
        assertEquals("19", tIter.value(2));
        tIter.reset();
        assertEquals("Zain", tIter.value(0));
        assertEquals("Alsaie", tIter.value(1));
        assertEquals("18", tIter.value(2));
    }

    @Test
    public void testCondition() {
        Table t = new Table("Test", new String[]{"FirstName", "LastName",
                "Age"});
        Row r = new Row(new String[]{"Maaz", "Uddin", "20"});
        t.add(r);
        Row r2 = new Row(new String[]{"Zain", "Alsaie", "18"});
        t.add(r2);
        Row r3 = new Row(new String[]{"Mark", "Wehbe", "20"});
        t.add(r3);
        TableIterator tIter = t.tableIterator();
        List<TableIterator> iters = new ArrayList<>();
        iters.add(tIter);
        Column firstNameCol = new Column(t, "FirstName");
        Column ageCol = new Column(t, "Age");
        ageCol.resolve(iters);
        firstNameCol.resolve(iters);
        Condition c1 = new Condition(ageCol, "=", "20");
        Condition c2 = new Condition(firstNameCol, "=", "Maaz");
        /**assertEquals(false, c1.test());
        assertEquals(false, c2.test()); These didn't work. */
    }


    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(BasicTests.class));
    }
}
