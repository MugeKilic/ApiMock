package ApiMock;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class AppTest extends ApiTestCase
{
    @Test
    public void itShouldReturnEmptyWhenFirstTime() throws IOException {
        System.out.println("- Verify that the API starts with an empty store. [Starting]");

        CloseableHttpClient client = HttpClientBuilder.create().build();

        CloseableHttpResponse response = client.execute(new HttpGet(API_ROOT));
        String bodyAsString = EntityUtils.toString(response.getEntity());

        assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
        assertThat(bodyAsString, equalTo("[]"));

        System.out.println("- Verify that the API starts with an empty store. [Completed]");
    }

    @Test
    public void itShouldReturnErrorIfTitleFieldIsEmpty() throws IOException {
        System.out.println("- Verify that title is required field. [Starting]");

        CloseableHttpClient client = HttpClientBuilder.create().build();

        String payload = "{\"title\": \"\", \"author\": \"Jane Archer\"}";
        StringEntity entity = new StringEntity(payload);

        HttpPut request = new HttpPut(API_ROOT);
        request.setEntity(entity);

        CloseableHttpResponse response = client.execute(request);
        String bodyAsString = EntityUtils.toString(response.getEntity());

        assertThat(response.getStatusLine().getStatusCode(), equalTo(400));
        assertThat(bodyAsString, equalTo("{\"error\": \"Field <title> is required.\"}"));

        System.out.println("- Verify that title is required field. [Completed]");
    }

    @Test
    public void itShouldReturnErrorIfAuthorFieldIsEmpty() throws IOException {
        System.out.println("- Verify that author is required field. [Starting]");

        CloseableHttpClient client = HttpClientBuilder.create().build();

        String payload = "{\"title\": \"Test\", \"author\": \"\"}";
        StringEntity entity = new StringEntity(payload);

        HttpPut request = new HttpPut(API_ROOT);
        request.setEntity(entity);


        CloseableHttpResponse response = client.execute(request);
        String bodyAsString = EntityUtils.toString(response.getEntity());

        assertThat(response.getStatusLine().getStatusCode(), equalTo(400));
        assertThat(bodyAsString, equalTo("{\"error\": \"Field <author> is required.\"}"));

        System.out.println("- Verify that author is required field. [Completed]");
    }

    @Test
    public void itShouldReturnErrorIfIdFieldExists() throws IOException {
        System.out.println("- Verify that the id field is read−only. [Starting]");

        CloseableHttpClient client = HttpClientBuilder.create().build();

        String payload = "{\"id\": 4, \"author\": \"John Carmack\", \"title\": \"Modern Game Development\"}";
        StringEntity entity = new StringEntity(payload);

        HttpPut request = new HttpPut(API_ROOT);
        request.setEntity(entity);

        CloseableHttpResponse response = client.execute(request);
        String bodyAsString = EntityUtils.toString(response.getEntity());

        assertThat(response.getStatusLine().getStatusCode(), equalTo(400));
        assertThat(bodyAsString, equalTo("{\"error\": \"Id field should be read-only.\"}"));

        System.out.println("- Verify that the id field is read−only. [Completed]");
    }

    @Test
    public void itShouldCreateNewBook() throws IOException {
        System.out.println("- Verify that you can create a new book via PUT. [Starting]");

        CloseableHttpClient client = HttpClientBuilder.create().build();

        String payload = "{\"author\": \"Muge Kilic\", \"title\": \"Trendyol\"}";
        StringEntity entity = new StringEntity(payload);

        HttpPut request = new HttpPut(API_ROOT);
        request.setEntity(entity);

        CloseableHttpResponse response = client.execute(request);
        String bodyAsString = EntityUtils.toString(response.getEntity());

        assertThat(response.getStatusLine().getStatusCode(), equalTo(201));
        assertThat(bodyAsString, equalTo("{\"id\": 3, \"author\": \"Muge Kilic\", \"title\": \"Trendyol\"}"));

        // ==========================================================
        // ==========================================================

        client = HttpClientBuilder.create().build();

        response = client.execute(new HttpGet(API_ROOT + "/3"));
        bodyAsString = EntityUtils.toString(response.getEntity());

        assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
        assertThat(bodyAsString, equalTo(" { \"id\": 3, \"author\": \"Muge Kilic\", \"title\": \"Trendyol\" }"));

        System.out.println("- Verify that you can create a new book via PUT. [Completed]");
    }

    @Test
    public void itShouldPreventAddDuplicateBookRecord() throws IOException {
        System.out.println("- Verify that you cannot create a duplicate book. [Starting]");

        CloseableHttpClient client = HttpClientBuilder.create().build();

        String payload = "{\"title\": \"DevOps is a lie\", \"author\": \"Jane Archer\"}";
        StringEntity entity = new StringEntity(payload);

        HttpPut request = new HttpPut(API_ROOT);
        request.setEntity(entity);

        CloseableHttpResponse response = client.execute(request);
        String bodyAsString = EntityUtils.toString(response.getEntity());

        assertThat(response.getStatusLine().getStatusCode(), equalTo(400));
        assertThat(bodyAsString, equalTo("{\"error\": \"Another book with similar title and author already exists.\"}"));

        System.out.println("- Verify that you cannot create a duplicate book. [Completed]");
    }
}
