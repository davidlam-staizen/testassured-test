import io.restassured.response.Response;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.*;


public class CsvTrial {
    @Test(dataProvider="dataprovider")
    void runner(String method,
                String url,
                String requestFile,
                String responseFile,
                String requestHeaderFile,
                String responseHeaderFile) throws IOException, ParseException {
        System.out.println("---------------------------------------------------------------------------------");
        System.out.println("/" + method + " " + url);

        Response actualResponse = network.network.makeRequest(method, url, requestFile, requestHeaderFile);

        String responseBody = actualResponse.getBody().asString();

        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        Object responseBodyJson = jsonParser.parse(responseBody);

        Object expectedResponseBody = fs.fs.readJson(responseFile);

        Assert.assertEquals(responseBodyJson, expectedResponseBody);
    }

    @DataProvider(name="dataprovider")
    Object[] getData() throws IOException {
        String path = System.getProperty("user.dir");
        int rownum = countRow(path + "/src/test/java/" + "test.csv");
        int colcount = 6;

        String data[][] = new String[rownum][colcount];

        Reader in = new FileReader(path + "/src/test/java/" + "test.csv");
        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withHeader("METHOD", "URL", "REQUEST", "RESPONSE", "REQUESTHEADER", "RESPONSEHEADER")
                .parse(in);

        int i = 0;
        for (CSVRecord record : records) {
            String method = record.get("METHOD");
            String url = record.get("URL");
            String request = record.get("REQUEST");
            String response = record.get("RESPONSE");
            String requestheader = record.get("REQUESTHEADER");
            String responseheader = record.get("RESPONSEHEADER");
            if (i < rownum) {
                data[i][0] = method;
                data[i][1] = url;
                data[i][2] = request;
                data[i][3] = response;
                data[i][4] = requestheader;
                data[i][5] = responseheader;
            }
            i++;
        }

        return data;
    }

    public int countRow(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }

}
