package MapParametersPackage;

import PopUpWindow.ExceptionWindow;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class ReadJSONMapParameters {

    private final JSONParser jsonParser = new JSONParser();

    public int[] readParameters() {
        try (FileReader reader = new FileReader("parameters.json"))
        {
            Object obj = jsonParser.parse(reader);
            JSONArray StageParameters = (JSONArray) obj;
            return parseSettings((JSONObject) StageParameters.get(0));

        } catch (IOException e) {
            ExceptionWindow exceptionWindow = new ExceptionWindow();
            exceptionWindow.start("Problem reading parameters.json");
            e.printStackTrace();
        }catch (ParseException e){
            ExceptionWindow exceptionWindow = new ExceptionWindow();
            exceptionWindow.start("Parse parameters.json problem");
            e.printStackTrace();
        }
        return new int[0];
    }

    private int[] parseSettings(JSONObject menu)
    {
        JSONObject MenuObject = (JSONObject) menu.get("Parameters");
        int mapWidth    = Integer.parseInt((String) MenuObject.get("MapWidth"));
        int mapHeight   = Integer.parseInt((String) MenuObject.get("MapHeight"));
        int BotCount    = Integer.parseInt((String) MenuObject.get("BotCount"));
        int ObstacleCount = Integer.parseInt((String) MenuObject.get("ObstacleCount"));

        return new int[] {mapWidth,mapHeight,BotCount,ObstacleCount};
    }
}