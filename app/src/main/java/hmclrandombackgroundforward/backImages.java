package hmclrandombackgroundforward;
/* Developed by NyaShulker */

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Collections;
import java.util.Comparator;


public class backImages {
    private String author;
    private String url;

    public String getAuthor(){
        return author;
    }
    public void setAuthor(String author){
        this.author = author;
    }
    public String getUrl(){
        return url;
    }
    public void setUrl(String url){
        this.url = url;
    }
}

class imageServerData {
    private String ServerName;
    private Map<String, backImages> ImageList;

    public String getServerName(){
        return ServerName;
    }
    public void setServerName(String serverName){
        ServerName = serverName;
    }
    public Map<String, backImages> getImageList() {
        return this.ImageList;
    }
    public void setImageList(Map<String, backImages> imageList){
        ImageList = imageList;
    }
}
class imageService{
    private imageServerData serverData;
    public imageService(String jsonURL){
        Gson gson = new Gson();
        String jsonString = getJsonFromUrl(jsonURL);
        serverData = gson.fromJson(jsonString, imageServerData.class);
    }
    private String getJsonFromUrl(String url) {
        StringBuilder stringBuilder = new StringBuilder();
        try{
            //创建URL对象
            URL urlObj = new URL(url);
            //建立连接并获取输入流
            InputStream inputStream = urlObj.openStream();
            //创建缓冲阅读器读取输入流
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            //读取输入流的每一行并使用StringBuilder构建完整json字符串
            String line;
            while ((line = reader.readLine()) != null){
                stringBuilder.append(line);
            }
            reader.close();
            inputStream.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
    public int getMaxId(){
        int maxId;
        Set<String> keys = serverData.getImageList().keySet();
	    //System.out.println("String<> MaxID"+keys);
        Comparator<String> cmp = new Comparator<String>(){
            @Override
            public int compare(String o1,String o2){
                return Integer.valueOf(o1).compareTo(Integer.valueOf(o2));
            }
        };
        String maxIds = Collections.max(keys,cmp);
        maxId = Integer.parseInt(maxIds);
        return maxId;
    }
    public backImages getImageById(int id){
        String idS = String.valueOf(id);
        return serverData.getImageList().get(idS);
    }
}

class randomId{
    public int genRandomId(int maxId){
        //System.out.println("MaxID-TEST: "+maxId);
        Random random = new Random();
        int randomId = random.nextInt(maxId) + 1;
        return randomId;
    }
}
