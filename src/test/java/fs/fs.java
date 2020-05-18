package fs;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class fs {
    public static Object readJson (String fileName) throws IOException, ParseException {
        Object obj = null;
        try {

            String path = System.getProperty("user.dir");
            FileReader reader = new FileReader(path + "/src/resources/" + fileName);
            JSONParser jsonParser = new JSONParser();
            obj = jsonParser.parse(reader);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (
                IOException e) {
            e.printStackTrace();
        } catch (
                ParseException e) {
            e.printStackTrace();
        }

        if (obj instanceof JSONArray) {
            // It's an array
            return (JSONArray) obj;
        }
        else {
            return (JSONObject) obj;
        }

    }

}
