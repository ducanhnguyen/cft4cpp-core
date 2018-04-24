package com.fit.googletest.xmlparser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Parse xml file to get the state of the test report
 *
 * @author ducanhnguyen
 */
public class XMLofGoogleTestParser implements IXMLofGoogleTestParser {
    public List<IGoogleTestTestcase> testcases;

    public static void main(String argv[]) {
    }

    @Override
    public List<IGoogleTestTestcase> parseXML(File xmlFilePath) {
        List<IGoogleTestTestcase> testcases = new ArrayList<>();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFilePath);
            doc.getDocumentElement().normalize();
            NodeList testCaseList = doc.getElementsByTagName(IXMLofGoogleTestParser.TEST_CASE_TAG);

            for (int temp = 0; temp < testCaseList.getLength(); temp++) {
                Node node = testCaseList.item(temp);
                Element element = (Element) node;
                Testcase testCase;
                if (node.hasChildNodes())
                    testCase = new Testcase(element.getAttribute(IXMLofGoogleTestParser.TEST_SUITE_ATTRIBUTE),
                            element.getAttribute(IXMLofGoogleTestParser.TEST_CASE_ATTRIBUTE), false);
                else
                    testCase = new Testcase(element.getAttribute(IXMLofGoogleTestParser.TEST_SUITE_ATTRIBUTE),
                            element.getAttribute(IXMLofGoogleTestParser.TEST_CASE_ATTRIBUTE), true);
                testcases.add(testCase);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return testcases;
    }

}
