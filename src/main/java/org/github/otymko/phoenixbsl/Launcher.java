package org.github.otymko.phoenixbsl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Launcher {

  private static final Logger log = LoggerFactory.getLogger(Launcher.class);

  public static void main(String[] args) {

    App app = App.getInstance();
    if (app.appIsRunning()) {
      app.abort();
    }
    try {
      app.run();
    } catch (RuntimeException ex) {
      log.error("Приложение упало. Причина " + ex.getMessage());
    }
  }

}
