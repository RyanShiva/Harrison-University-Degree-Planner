package edu.wgu.harrisonuniversitydegreeplanner;

import org.junit.Test;

import edu.wgu.harrisonuniversitydegreeplanner.Activities.AddTerm;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class AddTermTest {

    @Test
    public void emptyFieldsTest() {
        AddTerm addTerm = new AddTerm();

        String termTitle = "";
        String startDate = "2020-01-01";
        String endDate = "";

        assertEquals(false, addTerm.isValidInput(termTitle, startDate, endDate));
    }

    @Test
    public void incorrectDateFormatTest() {
        AddTerm addTerm = new AddTerm();

        String termTitle = "Term 2";
        String startDate = "January 1";
        String endDate = "May 31";

        assertEquals(false, addTerm.isValidInput(termTitle, startDate, endDate));
    }

    @Test
    public void incorrectDateTest() {
        AddTerm addTerm = new AddTerm();

        String termTitle = "Term 3";
        String startDate = "2021-02-29";
        String endDate = "2021-05-31";

        assertEquals(false, addTerm.isValidInput(termTitle, startDate, endDate));
    }

    @Test
    public void validInputTest() {
        AddTerm addTerm = new AddTerm();

        String termTitle = "Term 4";
        String startDate = "2020-01-01";
        String endDate = "2020-05-31";

        assertEquals(true, addTerm.isValidInput(termTitle, startDate, endDate));
    }
}