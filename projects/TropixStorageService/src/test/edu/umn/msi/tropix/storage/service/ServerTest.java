package edu.umn.msi.tropix.storage.service;

import java.util.UUID;

import javax.inject.Inject;

import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;

import edu.umn.msi.tropix.common.test.ConfigDirBuilder;
import edu.umn.msi.tropix.common.test.FreshConfigTest;
import edu.umn.msi.tropix.storage.service.client.StorageServiceFactory;

@ContextConfiguration(locations = {"testClientContext.xml", "testContext.xml"})
public class ServerTest extends FreshConfigTest {

  @Inject
  private StorageServiceFactory storageServiceFactory;

  private boolean secure = true;

  @Override
  protected void initializeConfigDir(final ConfigDirBuilder builder) {
    builder.createSubConfigDir("storage")
        .addDeployProperty("storage.service.enable", "true")
        .addDeployProperty("storage.service.secure", Boolean.toString(secure));
  }

  @Test
  public void cleanTest() {
    final String address;
    if(secure) {
      address = "https://localhost:8743/";
    } else {
      address = "http://localhost:8780/";
    }

    StorageService testI = storageServiceFactory.get(address);
    final String dataId = UUID.randomUUID().toString();
    testI.getData("User", dataId);
  }
  /*
   * 
   * @Test
   * public void test() throws InterruptedException, HttpException, IOException {
   * proxyClientCall();
   * 
   * //SpringBusFactory bf = new SpringBusFactory();
   * //URL busFile = getClass().getResource("server.xml");
   * //Bus bus = bf.createBus(busFile.toString());
   * //BusFactory.setDefaultBus(cxf);
   * 
   * //setupProtocol();
   * //httpCall();
   * 
   * 
   * // If Basic Authentication required could use:
   * 
   * //String authorizationHeader = "Basic "
   * // + org.apache.cxf.common.util.Base64Utility.encode("username:password".getBytes());
   * //httpget.addRequestHeader("Authorization", authorizationHeader);
   * 
   * //System.out.println("Sending HTTPS GET request to query customer info");
   * //HttpClient httpclient = new HttpClient();
   * //GetMethod httpget = new GetMethod("https://localhost:9000/storage/data/123");
   * //httpget.addRequestHeader("Accept" , "text/xml");
   * 
   * //try {
   * // httpclient.executeMethod(httpget);
   * // System.out.println(httpget.getResponseBodyAsString());
   * //} finally {
   * // httpget.releaseConnection();
   * //}
   * 
   * 
   * 
   * 
   * 
   * //Thread.sleep(1000 * 10);
   * 
   * //Object implementor = new TestImpl();
   * //String address = "https://localhost:9001/storage";
   * //Endpoint.publish(address, implementor);
   * 
   * }
   * 
   * private void proxyClientCall() {
   * final String address;
   * if(secure) {
   * address = "https://33.33.33.11:8743/";
   * } else {
   * address = "http://localhost:8780/";
   * }
   * 
   * StorageService testI = storageServiceFactory.get(address);
   * //StorageService testI = JAXRSClientFactory.create(address, StorageService.class);
   * 
   * //bean.setBus(cxf);
   * //final StorageService testI = (StorageService) bean.create();
   * //final TestI testI = JAXRSClientFactory.create("https://localhost:9000", TestI.class, "edu/umn/msi/tropix/storage/service/server.xml");
   * 
   * //bean.setConduitSelector(new UpfrontConduitSelector());
   * //bean.setConduitSelector(new UpfrontConduitSelector(conduit));
   * //final WebClient wc = bean.createWebClient();
   * 
   * final String dataId = UUID.randomUUID().toString();
   * testI.getData("User", dataId);
   * }
   */
}
