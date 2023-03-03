package com.booking.recruitment.scoring.extensions;

import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.net.ConnectException;
import java.util.concurrent.atomic.AtomicBoolean;

import static io.restassured.RestAssured.when;

@Slf4j
public class RESTExtension implements BeforeAllCallback {
  private static AtomicBoolean started = new AtomicBoolean();

  @Override
  public void beforeAll(ExtensionContext context) throws ConnectException {
    if (started.compareAndSet(false, true)) {
      String port = System.getProperty("server.port");
      if (port == null) {
        log.info("Property server.port is not defined - defaulting to 8000");
        RestAssured.port = 8000;
      } else {
        RestAssured.port = Integer.parseInt(port);
      }

      checkConnectionAvailable();
    }
  }

  private void checkConnectionAvailable() throws ConnectException {
    try {
      when().get("/");
    } catch (Exception e) {
      log.error("Unable to connect to " + RestAssured.baseURI + ":" + RestAssured.port, e);
      throw new ConnectException("Unable to connect to the configured server");
    }
  }
}
