/**
 * The TileManager class handles the management and rendering of tile maps in a 2D game environment.
 *
 * It supports loading tile maps from XML files, parsing the data, and maintaining a collection of TileMap objects.
 */
package assets.tiles;

import java.util.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import java.awt.Graphics2D;

public class TileManager {
      
    public ArrayList<TileMap> tilemap;


    // default constructor
    public TileManager() {
        tilemap = new ArrayList<TileMap>();
    }

    // constructor that takes image path
    public TileManager(String path) {
        tilemap = new ArrayList<TileMap>();
        addTileMap(path, 64, 64); 
    }

    // method for parsing the XML file with DOM
    public void parseXMLWithDOM(File file) throws ParserConfigurationException, SAXException, IOException {

        int width, height, tileWidth, tileColumns, tileCount, tileHeight, layers = 0;
        List<String> data = new ArrayList<>();

        DocumentBuilderFactory builderFac = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFac.newDocumentBuilder();
        Document doc = builder.parse(file); 
        doc.getDocumentElement().normalize();

        NodeList list = doc.getElementsByTagName("tileset");
        Node node = list.item(0);
        Element elt = (Element) node;

        String tilesetName = elt.getAttribute("name");
        tileWidth = Integer.parseInt(elt.getAttribute("tilewidth"));
        tileHeight = Integer.parseInt(elt.getAttribute("tileheight"));
        tileCount = Integer.parseInt(elt.getAttribute("tilecount"));
        tileColumns = Integer.parseInt(elt.getAttribute("columns"));

        list = doc.getElementsByTagName("layer");
        layers = list.getLength();

        for (int i = 0; i < layers; i++) {
            node = list.item(i);
            elt = (Element) node;
            if (i <= 0) {

                width = Integer.parseInt(elt.getAttribute("width"));
                height = Integer.parseInt(elt.getAttribute("height"));
                Node dataNode = elt.getElementsByTagName("data").item(0);

                if (dataNode != null) {
                    String layerData = dataNode.getTextContent().trim();
                    if (layerData != null && !layerData.isEmpty()) {
                        data.add(layerData); 
                    }
                }
            }
        }
    }

    private void addTileMap(String path, int bWidth, int bHeight) {
        try {
            File file = new File(getClass().getClassLoader().getResource(path).toURI());
            parseXMLWithDOM(file);
        } catch (URISyntaxException e) {
            System.out.println("TILEMANAGER Error: Invalid URI for tilemap path");
            e.printStackTrace();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.out.println("TILEMANAGER Error: Cannot parse tilemap XML");
            e.printStackTrace();
        }
    }

    

    public abstract class TileMap {
        public abstract void render(Graphics2D graphics);
    }

}