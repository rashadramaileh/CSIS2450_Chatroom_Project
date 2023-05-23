package application.controllers;

import java.io.IOException;

import application.Main;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneController
{
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	
	/**
	 * Shows chat room scene
	 * 
	 * @param e
	 * @throws IOException
	 */
	public void chatRoomScene(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(Main.class.getResource("view/ChatRoom.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();

	}
	
	/**
	 * Shows create account scene
	 * 
	 * @param e 
	 * @throws IOException
	 */
	public void createAccountScene(ActionEvent e) throws IOException
	{
		root = FXMLLoader.load(Main.class.getResource("view/CreateAccount.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}
	
	/**
	 * Shows login form scene
	 * 
	 * @param event
	 * @throws IOException
	 */
	public void loginFormScene(Event event) throws IOException
	{
		root = FXMLLoader.load(Main.class.getResource("view/loginScene.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}
}
