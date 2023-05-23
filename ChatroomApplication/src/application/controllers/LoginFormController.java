package application.controllers;

import java.io.IOException;
import java.net.URISyntaxException;

import application.model.User;
import application.model.UserHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import server.ChatMessager;

public class LoginFormController {
	SceneController switchScene = new SceneController();
	public static User u;
	private ChatMessager chatmessager = new ChatMessager();

    @FXML
    private Button loginBtn;
    
    @FXML
    private Label validationMessage;
    
    @FXML
    private TextField username;
    
    @FXML
    private PasswordField password;
    
	@FXML
	void onLoginClicked(ActionEvent event) throws IOException, URISyntaxException, InterruptedException {
		if(chatmessager.Login(username.getText(), password.getText())) {
			u = new User(username.getText(), password.getText(), 0, true);
			UserHolder holder = (UserHolder) UserHolder.getInstance();
			holder.setUser(u);
			holder.setChatter(chatmessager);
			
			switchScene.chatRoomScene(event);
		}else if(username.getText().isEmpty() || password.getText().isEmpty()){
			validationMessage.setText("Username or Password is empty!");
		}
		else
		{
			validationMessage.setText("Username or Password is incorrect!");
		}
	}
	
	@FXML
	void onCreateAccountClicked(ActionEvent event) throws IOException {
			switchScene.createAccountScene(event);
	}
	
	/**
	 * validate username and password text fields
	 * @throws InterruptedException 
	 * @throws URISyntaxException 
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	/*public boolean validateLogin() throws MalformedURLException, IOException, URISyntaxException, InterruptedException {
		//if(username.getText().toString().equals("admin") && password.getText().toString().equals("password"))
		if(chatmessager.Login(username.getText(), password.getText()))
		{
			return true;
		}
		else
			return false;
		
	}*/
	
}
