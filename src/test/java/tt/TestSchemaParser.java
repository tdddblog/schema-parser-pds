package tt;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TestSchemaParser
{

    public static void main(String[] args) throws Exception
    {
        Document doc = readXml(new File("/tmp/schema/PDS4_DISP_1B00.xsd"));
        Element root = doc.getDocumentElement();

        //System.out.println(root.getNodeName());
        
        
        //printAttributes(root);
        printElements(root);
        

    }

    
    public static void printElements(Element root)
    {
        //NodeList nl = root.getElementsByTagNameNS("http://www.w3.org/2001/XMLSchema", "element");
        
        NodeList nl = root.getChildNodes();
        if(nl == null) return;
        
        for(int i = 0; i < nl.getLength(); i++)
        {
            Node node = nl.item(i);
            if("http://www.w3.org/2001/XMLSchema".equals(node.getNamespaceURI()) && "element".equals(node.getLocalName()))
            {
                NamedNodeMap attrs = node.getAttributes();
                System.out.println(attrs.getNamedItem("name") + ", " + attrs.getNamedItem("type"));
            }
        }
    }
    
    
    public static void printAttributes(Element el)
    {
        NamedNodeMap attrs = el.getAttributes();
        if(attrs == null) return;
        
        for(int i = 0; i < attrs.getLength(); i++)
        {
            Node node = attrs.item(i);
            System.out.println(node);
        }
    }
    
    
    public static Document readXml(File file) throws Exception
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);        
        return doc;
    }
}
