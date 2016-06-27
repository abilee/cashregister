package com.cashregister.userinterface;

import com.cashregister.actions.PutAction;
import com.cashregister.exceptions.ValidatorException;
import com.cashregister.userinterface.BaseParser;
import com.cashregister.userinterface.POSEditor;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.fail;

/**
 */
public class BaseParserTest {
    BaseParser baseParser;

    @Before
    public void setUp() {
        baseParser = new PutAction(new POSEditor());
    }

    @Test(expected = ValidatorException.class)
    public void testPutOrTakeInvalid1() throws ValidatorException {
       baseParser.validate("put ");
    }


    @Test(expected = ValidatorException.class)
    public void testPutOrTakeInvalid2() throws ValidatorException {
        baseParser.validate("put test test test test test");
    }

    @Test(expected = ValidatorException.class)
    public void testPutOrTakeInvalid3() throws ValidatorException {
        baseParser.validate("put -1 2 3 0 5");
        fail();
    }

    @Test(expected = ValidatorException.class)
    public void testPutOrTakeInvalid4() throws ValidatorException {
        baseParser.validate("put  2 3 0 5");
        fail();
    }

    @Test
    public void testPutOrTakeValid1() throws ValidatorException {
        baseParser.validate("put 1 2 3 4 5");
    }

    @Test
    public void testPutOrTakeValid2() throws ValidatorException {
        baseParser.validate("PUT 10 1 2 3 4");
    }
}
