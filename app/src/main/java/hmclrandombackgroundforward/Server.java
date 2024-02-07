package hmclrandombackgroundforward;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import hmclrandombackgroundforward.Shulker.colorANSI;

import javax.imageio.ImageIO;

public class Server {
    private static Server instance;
    private Properties prop;
    private Server(){
        //Private Constructor
    }
    public static synchronized Server getInstance() throws IOException {
        if (instance == null){
            instance = new Server();
            instance.loadConfig();
        }
        return instance;
    }
    public Properties loadConfig() throws IOException{
        colorANSI color = new colorANSI();
        prop = new Properties();
        if (Files.exists(Paths.get("config.properties"))){
            System.out.println(color.color("green")+"Configuration exists, Loading Properties"+color.color("reset"));
            InputStream input = new FileInputStream("./config.properties");
            prop.load(input);
            input.close();
            return prop;
        }else {
            System.out.println(color.color("red") + "Missing Properties, Generating Configurations" + color.color("reset"));
            try {
                prop.setProperty("port", "8000");
                prop.setProperty("operator", "NAME");
                prop.store(new FileOutputStream("config.properties"), null);
            } catch (IOException ex){
                ex.printStackTrace();
            }
            return prop;
        }
    }
    public String getConfig(String key){
        return prop.getProperty(key);
    }
    int createServer(int port){
        try{
            HttpServer server = HttpServer.create(new InetSocketAddress(port),0);
            server.createContext("/test", new testHandler());
            server.createContext("/server", new serverHandler());
            server.start();
            return 0;
        }catch (IOException e){
            e.printStackTrace();
            return 1;
        }
    }
    void testHandler(){

    }
    public int runServer() throws IOException {
        int port = Integer.parseInt(getConfig("port"));
        int statusCode = createServer(port);
        if(statusCode==0){
            return 0;
        }else {
            return statusCode;
        }
    }
}
class testHandler implements HttpHandler{
    @Override
    public void handle(HttpExchange t) throws IOException{
        colorANSI color = new colorANSI();
        String response = color.color("cyan")+Server.getInstance().getConfig("operator")+color.color("green")+" "+"Random Background Image Forward Server Running Properly.\nServer Version :"+color.color("underline")+color.color("yellow")+App.VERSION+"\n";
        t.sendResponseHeaders(200,response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes(StandardCharsets.UTF_8));
        os.close();
    }
}
class serverHandler implements HttpHandler{
    @Override
    public void handle(HttpExchange t) throws IOException{
        //Set colorANSI support
        colorANSI color = new colorANSI();
        //Get GET request content
        String request = t.getRequestURI().getQuery();
        System.out.println(color.color("green")+"Get one server forward request {"+color.color("yellow")+request+color.color("green")+color.color("green")+"}"+color.color("reset"));
        //Run backImage functions
        //Set random ID
        randomId rID = new randomId();
        //Set requset url for imageService
        imageService myImageService = new imageService(request);
        //Get return image url
        backImages image = myImageService.getImageById(rID.genRandomId(myImageService.getMaxId()));
        String imageUrl = image.getUrl();
        System.out.println(color.color("green")+"Return Image URL: "+color.color("yellow")+imageUrl+color.color("reset"));
        String fileExtension = imageUrl.substring(imageUrl.lastIndexOf(".") + 1);
        String contentType;
        switch (fileExtension.toLowerCase()) {
            case "jpg":
            case "jpeg":
                contentType = "image/jpeg";
                break;
            case "png":
                contentType = "image/png";
                break;
            case "gif":
                contentType = "image/gif";
                break;
            default:
                throw new IOException("Unsupported image format: " + fileExtension);
        }
        BufferedImage bufferedImage = ImageIO.read(new URL(imageUrl));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage,fileExtension,byteArrayOutputStream);
        byte[] imageData = byteArrayOutputStream.toByteArray();
        t.getResponseHeaders().set("Content-Type", contentType);
        t.sendResponseHeaders(200,imageData.length);
        OutputStream os = t.getResponseBody();
        os.write(imageData);
        os.close();
    }
}