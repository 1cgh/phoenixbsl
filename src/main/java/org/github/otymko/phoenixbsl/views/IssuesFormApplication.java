package org.github.otymko.phoenixbsl.views;

import com.sun.javafx.scene.control.LabeledText;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.github.otymko.phoenixbsl.App;
import org.github.otymko.phoenixbsl.entities.Issue;

import java.util.ArrayList;
import java.util.List;

public class IssuesFormApplication extends Application {

  private App app;
  private List<Issue> listIssues = new ArrayList<>();

  public IssuesFormApplication() {
    this.app = App.getInstance();
    List <Diagnostic> list = app.getDiagnostics();
    initListIssues(list);
  }

  private void initListIssues(List<Diagnostic> list) {
    for (Diagnostic diagnostic : list) {

      Issue issue = new Issue();
      issue.setDiscription(diagnostic.getMessage());
      Range range = diagnostic.getRange();
      Position position = range.getStart();
      String location = String.format("[%s, %s]", position.getLine() + 1, position.getCharacter() + 1);
      issue.setLocation(location);
      issue.setStartLine(position.getLine() + 1);

      listIssues.add(issue);
    }
  }

  @Override
  public void start(Stage primaryStage) throws Exception {

   // primaryStage.initStyle(StageStyle.TRANSPARENT);
    //primaryStage.initStyle(StageStyle.TRANSPARENT);
    primaryStage.setOpacity(0.9);

    primaryStage.setTitle("Замечания");

    ObservableList<Issue> listForForm = FXCollections.<Issue>observableArrayList(listIssues);

    ListView<Issue> issues = new ListView<>(listForForm);
    issues.setOrientation(Orientation.VERTICAL);
    issues.setPrefSize(400, 600);

    issues.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 &&
            (event.getTarget() instanceof LabeledText || ((GridPane) event.getTarget()).getChildren().size() > 0)) {

          ObservableList<Issue> itemList = issues.getSelectionModel().getSelectedItems();
          app.focusDocumentLine(itemList.get(0).getStartLine());

        }
      }
    });

    VBox issuesSelection = new VBox();
    issuesSelection.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
    issuesSelection.setSpacing(10);
    issuesSelection.getChildren().add(issues);

    GridPane pane = new GridPane();
    pane.setHgap(10);
    pane.setVgap(5);
    pane.addColumn(0, issuesSelection);

    Scene scene = new Scene(pane, Color.TRANSPARENT);
    primaryStage.setScene(scene);
    primaryStage.show();

  }
}
