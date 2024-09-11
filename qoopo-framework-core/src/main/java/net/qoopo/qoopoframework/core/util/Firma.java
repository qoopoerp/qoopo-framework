package net.qoopo.qoopoframework.core.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

/**
 * Convierte el valor de la firma del Control Signature del Primefaces a una
 * imagen SVG
 *
 * @author alberto
 */
public class Firma {

    public String toSvg(String signature, int width, int height) {
        List<String> paths = new ArrayList<>();
        try (JsonReader jsonReader = Json.createReader(new StringReader(signature))) {
            JsonObject jsonObject = jsonReader.readObject();
            JsonArray jsonArray = jsonObject.getJsonArray("lines");
            jsonArray.forEach(line -> paths.add(toSvgPath((JsonArray) line)));
        }
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("<svg width=\"%d\" height=\"%d\" xmlns=\"http://www.w3.org/2000/svg\">\n", width, height));
        paths.forEach(path -> sb.append(path));
        sb.append("</svg>");
        return sb.toString();
    }

    private String toSvgPath(JsonArray line) {
        StringBuilder sb = new StringBuilder("<path d=\"");
        for (int i = 0; i < line.size(); i++) {
            JsonArray coords = (JsonArray) line.getJsonArray(i);
            sb.append(String.format("%s%d %d ", (i == 0 ? "M" : "L"), coords.getInt(0), coords.getInt(1)));
        }
        sb.append("\" stroke=\"black\" fill=\"transparent\"/>\n");
        return sb.toString();
    }

    /*
    
    
    
Just check the PrimeFaces showcase for p:signature. Signature values are bound to a string, so you can simply store a signature as a string in your database. Later you can simply show the signature using the stored string in combination with the readonly attribute set to true. This is exactly what is demonstrated in the showcase.

Your signature string value will look something like:

{"lines":[[[81,75],[81,78],[81,80],[81,84],[83,87],...]]}
If you really want to store it as an image instead of a string, you have a few options.

SVG
As the string basically is an JSON object with a "lines" array, which per line has the coordinates, you can simply transform it to a SVG. All you need to do is create some paths. A rough implementation:

public String toSvg(String signature, int width, int height) {
  List<String> paths = new ArrayList<>();
  try (JsonReader jsonReader = Json.createReader(new StringReader(signature))) {
    JsonObject jsonObject = jsonReader.readObject();
    JsonArray jsonArray = jsonObject.getJsonArray("lines");
    jsonArray.forEach(line -> paths.add(toSvgPath((JsonArray) line)));
  }
  StringBuilder sb = new StringBuilder();
  sb.append(String.format("<svg width=\"%d\" height=\"%d\" xmlns=\"http://www.w3.org/2000/svg\">\n", width, height));
  paths.forEach(path -> sb.append(path));
  sb.append("</svg>");
  return sb.toString();
}


private String toSvgPath(JsonArray line) {
  StringBuilder sb = new StringBuilder("<path d=\"");
  for (int i = 0; i < line.size(); i++) {
    JsonArray coords = (JsonArray) line.getJsonArray(i);
    sb.append(String.format("%s%d %d ", (i == 0 ? "M" : "L"), coords.getInt(0), coords.getInt(1)));
  }
  sb.append("\" stroke=\"black\" fill=\"transparent\"/>\n");
  return sb.toString();
}
But, as the SVG can easily be created from the JSON object, you might just want to store the JSON object and create the SVG when you need to render the signature.

See also:

How to convert String to JsonObject
Please explain SVG Path Commands and Coordinates

    
    
    PNG
p:signature has a base64Value attribute which writes a PNG image as base64 encoded data to the provided property:

<p:signature id="signature"
             base64Value="#{myBean.signature}">
This will give you a URL which will look like:

data:image/png;base64,iVBORw0KGgoAAAANSU...
In your bean it's just a matter of getting the data from the URL:

private static final String URL_DATA_PNG_BASE64_PREFIX = "data:image/png;base64,";

..

String encoded = signatureUrl.substring(URL_DATA_PNG_BASE64_PREFIX.length());
byte[] decoded = Base64.getDecoder().decode(encoded);
You could optionally save it as a file:

Path path = Paths.get("path/to/your/image.png");
Files.write(path, decoded);
See also:

Decode Base64 data in Java
I would also suggest to download and read the documentation PDF. It also covers an alternative way to get the signature as PNG.
    
    
     */
}
