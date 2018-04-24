package test.parser;

import com.fit.config.Paths;
import org.apache.log4j.Logger;

/**
 * This class is used to test the ability of executing unit test project. The
 * unit test source code format is represented in form of google test framework
 *
 * @author ducanhnguyen
 */
public class GoogleTestSourceCodeTest extends AGoogleTestSourceCodeTest {
    final static Logger logger = Logger.getLogger(GoogleTestSourceCodeTest.class);

    public GoogleTestSourceCodeTest() throws Exception {
        runAll();
    }

    public static void main(String[] args) throws Exception {
        new GoogleTestSourceCodeTest();
    }

    public void runAll() throws Exception {
        basicTest();
        arrayTest();
        pointerTest();
        stringTest();
        classTest();
        callTest();
        throwExceptionTest();

        logger.debug("TEST SUMMARY");
        logger.debug("Pass = " + passedFunctions.toString());
        logger.debug("Fail = " + failedFunctions.toString());
    }

    /**
     * Throw exception
     *
     * @throws Exception
     */
    private void throwExceptionTest() throws Exception {
        test(Paths.TSDV_R1_2, "Divide(int,int)", "x=3;y=0", new String[]{"throw,throw,std::exception"}, PASS_TEST);
    }

    /**
     * Test the function contains calls
     *
     * @throws Exception
     */
    private void callTest() throws Exception {
        test(Paths.TSDV_R1_2, "mmin3_extern(int,int,int)", "x=1;y=2;z=3;", new String[]{"int,returnVar,1"},
                PASS_TEST);
    }

    private void classTest() throws Exception {
        test(Paths.TSDV_R1, "SimpleMethodTest()", "x=1", new String[]{"int,x,1", "int,returnVar,1"}, PASS_TEST);

        // static function
        test(Paths.TSDV_R1_2, "AddTest(int,int)", "x=1;y=1;", new String[]{"int,returnVar,1"}, PASS_TEST);
        test(Paths.TSDV_R1_2, "MathUtils::MinusTest(int,int)", "x=2;y=1;", new String[]{"int,returnVar,1"},
                PASS_TEST);
    }

    private void pointerTest() throws Exception {
        // one level
        test(Paths.TSDV_R1, "Level1OutputAdvance(int*)", "p[0]=10;", new String[]{"int,p[0],11", "int,p[1],2"},
                PASS_TEST);

        test(Paths.TSDV_R1, "Level1OutputAdvance(int*)", "p[0]=10;", new String[]{"int,p[0],11", "int,p[1],20000"},
                FALSE_TEST);

        test(Paths.TSDV_R1, "Level1OutputAdvance(int*)", "p[0]=10;", new String[]{"int*,p,NULL"}, FALSE_TEST);

        test(Paths.SYMBOLIC_EXECUTION_TEST, "basicTest22(int&)", "a=1;", new String[]{"int&,a,2", "int,returnVar,4"},
                PASS_TEST);

        // two level
    }

    private void stringTest() throws Exception {
        // ASCII 49='1'; ASCII 97='a'
        test(Paths.SYMBOLIC_EXECUTION_TEST, "pointerTest25(char*)", "a[0]=49;", new String[]{"char,a[0],97"},
                PASS_TEST);
    }

    /**
     * The parameters, return value is all basic variable.
     * <p>
     * The test report of a test path looks like:
     * <p>
     * <p>
     * <pre>
     * 	#include "gtest/gtest.h"
     * #include "./Sample_for_R1/BasicType.cpp"
     *
     * TEST(IntTest0, testpath1) {
     * // parameters
     * int x=1;
     *
     * // expected output
     * int EO_x = 1;
     * int EO_returnVar = 1;
     *
     * // execute function under test data
     * int AO = IntTest(x);
     *
     * // comparision
     * EXPECT_EQ(EO_x, x);
     * EXPECT_EQ(EO_returnVar, AO);
     * }
     * </pre>
     */
    private void basicTest() throws Exception {
        test(Paths.TSDV_R1, "IntTest(int)", "x=1;", new String[]{"int,x,1", "int,returnVar,1"}, PASS_TEST);
        test(Paths.TSDV_R1, "IntTest(int)", "x=0;", new String[]{"int,x,0", "int,returnVar,0"}, PASS_TEST);
        test(Paths.TSDV_R1, "IntTest(int)", "x=0;", new String[]{"int,x,1", "int,returnVar,0"}, FALSE_TEST);
        test(Paths.TSDV_R1, "IntTest(int)", "x=-10;", new String[]{"int,x,0", "int,returnVar,1"}, FALSE_TEST);

        test(Paths.TSDV_R1_2, "Abs(int)", "x=-10;", new String[]{"int,x,-10", "int,returnVar,10"}, PASS_TEST);
    }

    /**
     * The parameters is array/pointer; the return value of basic
     *
     * @throws Exception
     */
    private void arrayTest() throws Exception {
        // one dimension array
        test(Paths.TSDV_R1, "Dim1InputSimple(int[])", "p[0]=1;", new String[]{"int,p[0],1", "int,returnVar,1"},
                PASS_TEST);
        test(Paths.TSDV_R1, "Dim1InputSimple(int[])", "p[0]=1;", new String[]{"int,p[0],0", "int,returnVar,1"},
                FALSE_TEST);

        // two dimension array
        test(Paths.TSDV_R1, "Dim2InputSimple(int[][1])", "p[0][0]=1;",
                new String[]{"int,p[0][0],1", "int,returnVar,1"}, PASS_TEST);
    }

}
