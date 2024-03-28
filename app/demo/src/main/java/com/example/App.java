package com.example;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        //realizaremos un agrupamiento
        Parent root = FXMLLoader.load(getClass().getResource("interfaz.fxml"));
        Scene scene = new Scene(root);
        
        stage.setResizable(false);
        Image imagen = new Image( getClass().getResource("store.png").toString() );
        stage.getIcons().add(imagen);
        stage.setTitle("TiendaVirtual");
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}

/*
 * 
 * Group root = new Group();

        scene = new Scene(root, Color.WHITE); //configuracion de la escena...
        //definimos en nuestro escenario
        stage.setScene(scene);

        //configuracion del escenario
        stage.setTitle("Formulario Tienda Virtual");
        stage.setWidth(1100);
        stage.setHeight(600);
        stage.setResizable(false);
        stage.setX(300);
        stage.setY(200);

        //definimos un texto.
        Text texto = new Text();
        texto.setText("Tienda Virtual");
        texto.setX((double) (stage.getWidth()/3));
        texto.setY(70);
        texto.setFont(Font.font("Verdana", 40));
        texto.setFill(Color.GREEN);


        root.getChildren().add(texto);
 */