/** This is a SUGGESTED skeleton for a class that describes a single Row of a
 Table. You can throw this away if you want, but it is a good idea to try to
 understand it first.  Our solution changes or adds about 10 lines in this
 skeleton.

 Comments that start with "//" are intended to be removed from your
 solutions.
 */
package db61b;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static db61b.Utils.*;

/** A single table in a database.
 *  @author Maaz Uddin
 */
class Table implements Iterable<Row> {
    /** A new Table named NAME whose columns are give by COLUMNTITLES,
     *  which must be distinct (else exception thrown). */
    Table(String name, String[] columnTitles) {
        HashSet<String> dups = new HashSet<>();
        for (String s : columnTitles) {
            dups.add(s);
        }
        if (columnTitles.length != dups.size()) {
            throw new DBException("Column Titles not Unique");
        }
        _name = name;
        _titles = columnTitles;
    }

    /** A new Table named NAME whose column names are give by COLUMNTITLES. */
    Table(String name, List<String> columnTitles) {
        this(name, columnTitles.toArray(new String[columnTitles.size()]));
    }

    /** Return the number of columns in this table. */
    int numColumns() {
        return _titles.length;
    }

    /** Returns my name. */
    String name() {
        return _name;
    }

    /** Returns a TableIterator over my rows in an unspecified order. */
    TableIterator tableIterator() {
        return new TableIterator(this);
    }

    /** Returns an iterator that returns my rows in an unspecified order. */
    @Override
    public Iterator<Row> iterator() {
        return _rows.iterator();
    }

    /** Return the title of the Kth column.  Requires 0 <= K < columns(). */
    String title(int k) {
        return _titles[k];
    }

    /** Return the number of the column whose title is TITLE, or -1 if
     *  there isn't one. */
    int columnIndex(String title) {
        for (int i = 0; i < _titles.length; i++) {
            if (_titles[i].equals(title)) {
                return i;
            }
        }
        return -1;
    }

    /** Return the number of Rows in this table. */
    int size() {
        return _rows.size();
    }

    /** Add ROW to THIS if no equal row already exists.  Return true if anything
     *  was added, false otherwise. */
    boolean add(Row row) {
        if (_rows.contains(row)) {
            return false;
        } else {
            _rows.add(row);
            return true;
        }
    }

    /** Read the contents of the file NAME.db, and return as a Table.
     *  Format errors in the .db file cause a DBException. */
    static Table readTable(String name) {
        BufferedReader input;
        Table table;
        input = null;
        table = null;
        try {
            input = new BufferedReader(new FileReader(name + ".db"));
            String header = input.readLine();
            if (header == null) {
                throw error("missing header in DB file");
            }
            String[] columnNames = header.split(",");
            table = new Table(name, columnNames);
            boolean controlStatement = true;
            while (controlStatement) {
                String nextLine = input.readLine();
                if (nextLine != null) {
                    String[] line = nextLine.split(",");
                    Row r = new Row(line);
                    table._rows.add(r);
                } else {
                    controlStatement = false;
                }
            }
        } catch (FileNotFoundException e) {
            throw error("could not find %s.db", name);
        } catch (IOException e) {
            throw error("problem reading from %s.db", name);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    /* Ignore IOException */
                }
            }
        }
        return table;
    }

    /** Write the contents of TABLE into the file NAME.db. Any I/O errors
     *  cause a DBException. */
    void writeTable(String name) {
        PrintStream output;
        output = null;
        try {
            String sep;
            sep = ",";
            output = new PrintStream(name + ".db");
            for (int i = 0; i < _titles.length; i++) {
                if (i == _titles.length - 1) {
                    output.append(_titles[i] + "/n");
                } else {
                    output.append(_titles[i] + sep);
                }
            }
            for (Row r : _rows) {
                for (int i = 0; i < r.size(); i++) {
                    output.append(r.get(i) + sep);
                }
                output.append("/n");
            }
        } catch (IOException e) {
            throw error("trouble writing to %s.db", name);
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

    /** Print my contents on the standard output, separated by spaces
     *  and indented by two spaces. */
    void print() {
        for (Row r : _rows) {
            System.out.print("  ");
            for (int i = 0; i < r.size(); i++) {
                if (i == r.size() - 1) {
                    System.out.print(r.get(i));
                } else {
                    System.out.print(r.get(i) + " ");
                }
            }
            System.out.println();
        }
    }

    /** My name. */
    private final String _name;
    /** My column titles. */
    private String[] _titles;
    /** My rows. */
    private HashSet<Row> _rows = new HashSet<>();
}

