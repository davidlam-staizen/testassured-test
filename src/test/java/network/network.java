package network;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class network {
    public static Response makeRequest(String method, String url, String fileName, String headerFile) throws IOException, ParseException {
        RestAssured.baseURI = url;
        RequestSpecification httprequest=RestAssured.given().contentType(ContentType.JSON);
        Method httpMethod;

        switch (method) {
            case "POST":
                httpMethod = Method.POST;
                break;
            case "PUT":
                httpMethod = Method.PUT;
                break;
            case "PATCH":
                httpMethod = Method.PATCH;
                break;
            case "DELETE":
                httpMethod = Method.DELETE;
                break;
            default:
                httpMethod = Method.GET;
                break;
        }

        Object jsonObject = null;
        JSONObject headerObject = null;
        if (!method.equals("GET")) {
            if (!fileName.equals("null")) {
                jsonObject = fs.fs.readJson(fileName);
                httprequest.body(jsonObject.toString());
            }

            if (!headerFile.equals("null")) {
                headerObject = (JSONObject) fs.fs.readJson(headerFile);
                System.out.println(headerObject.get("authorization"));
                httprequest.headers("authorization", headerObject.get("authorization"));
            }
        }

        Response response = httprequest
                .request(httpMethod, "/");

        return response;
    }
}
