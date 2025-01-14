package common;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.*;

public class XMLConfigManager {
    private static Document loadXMLFromString(String xmlStr) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new ByteArrayInputStream(xmlStr.getBytes("UTF-8")));
    }

    public static double[] getWeights(String xmlContent, String classID) {
        try {
            Document doc = loadXMLFromString(xmlContent);
            NodeList classNodes = doc.getElementsByTagName("Class");

            for (int i = 0; i < classNodes.getLength(); i++) {
                Element classElement = (Element) classNodes.item(i);
                String id = classElement.getElementsByTagName("ClassID").item(0).getTextContent();

                if (id.equals(classID)) {
                    double attendance = Double.parseDouble(classElement.getElementsByTagName("AttendanceWeight").item(0).getTextContent());
                    double midterm = Double.parseDouble(classElement.getElementsByTagName("MidtermWeight").item(0).getTextContent());
                    double finalExam = Double.parseDouble(classElement.getElementsByTagName("FinalWeight").item(0).getTextContent());
                    return new double[]{attendance, midterm, finalExam};
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new double[]{0.2, 0.3, 0.5}; // Default values
    }

    public static Map<String, double[]> getGradeLetters(String xmlContent, String classID) {
        Map<String, double[]> gradeLetters = new HashMap<>();
        try {
            Document doc = loadXMLFromString(xmlContent);
            NodeList classNodes = doc.getElementsByTagName("Class");

            for (int i = 0; i < classNodes.getLength(); i++) {
                Element classElement = (Element) classNodes.item(i);
                String id = classElement.getElementsByTagName("ClassID").item(0).getTextContent();

                if (id.equals(classID)) {
                    NodeList gradeNodes = classElement.getElementsByTagName("GradeLetter");
                    for (int j = 0; j < gradeNodes.getLength(); j++) {
                        Element gradeElement = (Element) gradeNodes.item(j);
                        String letter = gradeElement.getTextContent();
                        double min = Double.parseDouble(gradeElement.getAttribute("min"));
                        double max = Double.parseDouble(gradeElement.getAttribute("max"));
                        gradeLetters.put(letter, new double[]{min, max});
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gradeLetters;
    }
}