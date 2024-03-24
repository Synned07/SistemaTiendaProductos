package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.shape.Circle;

public class ControllerBotones {

    @FXML
    private Circle myCircle;
    private double X;
    private double Y; 
    
    public void up(ActionEvent evento)
    {
        this.Y -= myCircle.getScaleY();
        myCircle.setTranslateY(this.Y);
    }

    public void down(ActionEvent evento)
    {
        this.Y += myCircle.getScaleY();
        myCircle.setTranslateY(this.Y);
    }


    public void right(ActionEvent evento)
    {
        System.out.println("RIGHT");
    }

    public void left(ActionEvent evento)
    {
        System.out.println("LEFT");
    }



    public void circle(ActionEvent evento)
    {
        System.out.println(evento.getEventType());
    }
}
