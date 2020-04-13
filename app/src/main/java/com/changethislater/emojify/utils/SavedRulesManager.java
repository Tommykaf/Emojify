package com.changethislater.emojify.utils;

import android.content.Context;

import com.changethislater.emojify.R;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import androidx.annotation.NonNull;


public class SavedRulesManager {


    private static SavedRulesManager instance;
    private File savedRulesXML;
    private DocumentBuilder builder;

    public static SavedRulesManager getInstance(@NonNull Context context) {
        if (instance == null) {
            instance = new SavedRulesManager(context);
        }

        return SavedRulesManager.instance;
    }

    private SavedRulesManager(Context context) {
        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
        try {
            this.builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        File file = new File(context.getFilesDir(), context.getString(R.string.rulesXML));

        try {
            if (!file.createNewFile()) {
                this.setSavedRulesXML(file);
            } else {
                initializeFile(file, context);
                this.setSavedRulesXML(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initializeFile(File file, Context context) throws IOException {
        context.openFileOutput(file.getName(), Context.MODE_PRIVATE)
                .write("<?xml version=\"1.0\"?> <rules> </rules>".getBytes(StandardCharsets.UTF_8));
    }

    public void setSavedRulesXML(File savedRulesXML) {
        this.savedRulesXML = savedRulesXML;
    }

    public File getSavedRulesXML() {
        return savedRulesXML;
    }

    public List<Rule> readRulesFromFile(Context context) throws IOException, SAXException {
        List<Rule> result = new ArrayList<>();
        Document doc = getFileContents(context);
        Element root = doc.getDocumentElement();

        NodeList nl = root.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Element ith = (Element)nl.item(i);
            String name = ith.getAttribute("name");
            String to = ith.getElementsByTagName("to").item(0).getTextContent();
            String[] froms = new String[ith.getElementsByTagName("from").getLength()];
            for (int j = 0; j <froms.length; j++) {
                froms[j] = ith.getElementsByTagName("from").item(j).getTextContent();
            }
            Rule rule = new Rule(name,to,froms);
            result.add(rule);
        }

        return result;
    }

    public void addRuleToFile(Context context, String name, String to, String... from) {
        try {
            Document doc = getFileContents(context);
            Element root = doc.getDocumentElement();
            Element repRule = doc.createElement("replacement-rule");
            root.appendChild(repRule);

            Attr nameAttr = doc.createAttribute("name");
            nameAttr.setValue(name);
            repRule.setAttributeNode(nameAttr);

            Element fromElement = doc.createElement("from-set");
            for (String s : from) {
                Element temp = doc.createElement("from");
                temp.appendChild(doc.createTextNode(s));
                fromElement.appendChild(temp);
            }
            repRule.appendChild(fromElement);

            Element toElement = doc.createElement("to");
            toElement.appendChild(doc.createTextNode(to));
            repRule.appendChild(toElement);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(context.openFileOutput(savedRulesXML.getName(), Context.MODE_PRIVATE));
            transformer.transform(source, result);

        } catch (IOException | SAXException | TransformerException e) {
            e.printStackTrace();
        }



    }


    private Document getFileContents(Context context) throws IOException, SAXException {
        FileInputStream fis = context.openFileInput(savedRulesXML.getName());
        return builder.parse(fis);
    }
}
