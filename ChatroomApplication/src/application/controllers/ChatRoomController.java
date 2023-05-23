package application.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import server.ChatMessager;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import application.model.User;
import application.model.UserHolder;

public class ChatRoomController implements Initializable {
	SceneController switchScene = new SceneController();
	User u;
	ChatMessager chatmessager;

	String name = "";
	String status = "";
	String room = "";

	@FXML
	private TextArea messageField;

	@FXML
	private Button btnSend, emojiBtn, gifBtn, btn, avatarBtn, usernameBtn, passwordBtn, updateNameBtn, updatePassBtn,
			updatePicBtn;

	@FXML
	private AnchorPane svgPane, chatPane, rootPane, pane1, pane2, settingsPane;

	@FXML
	private ScrollPane activeUserScroll, scrollPane;

	@FXML
	private VBox activeUserBox, detailBox, chatBox, avatarBox, usernameBox, passwordBox;

	@FXML
	private HBox iconBox;

	@FXML
	private ImageView logoutBtn, settingsBtn1;

	@FXML
	private Label loggedInAsName, currentUser, nameError, currPassError, passError, passOk;

	@FXML
	private TextField messageBubble, changeUsername;

	@FXML
	private PasswordField currPass, newPass, confPass;

	@FXML
	private RadioButton button0, button1, button2, button3, button4, button5, button6, button7, button8;

	@FXML
	void leaveChatRoom(MouseEvent event) throws IOException, URISyntaxException, InterruptedException {
		chatmessager.LogOut();
		switchScene.loginFormScene(event);
		// TODO clear session
	}

	@FXML
	void openSettingsPane(MouseEvent click) {
		// TODO show settings pane or dialog box

		// slide animation
		settingsBtn1.setDisable(true);
		TranslateTransition slide = new TranslateTransition();
		slide.setDuration(Duration.millis(350));
		slide.setNode(pane2);
		slide.setOnFinished(event -> settingsBtn1.setDisable(false));

		// Open settings pane
		if (!pane1.isVisible()) {
			pane1.setVisible(true);
			slide.setByY(+1000);
			slide.play();
		}

		// close settings panes
		else {
			pane1.setVisible(false);
			slide.setByY(-1000);
			slide.play();
			closeAll(1);
		}
	}

	@FXML
	void sendMessage(ActionEvent event) {
		String m = messageField.getText();

		try {
			chatmessager.SendMessage(m);
			messageField.clear();
		} catch (IOException | URISyntaxException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void displayMessage(String username, String message, String timestamp) throws MalformedURLException {

		HBox msgHBox = new HBox();
//		msgHBox.setId("outgoingMsgHBox");
		chatBox.getChildren().add(msgHBox);
		if (u.getUsername().equals(username)) {
			msgHBox.setStyle("-fx-alignment:TOP_RIGHT;");
		} else {
			msgHBox.setStyle("-fx-alignment:TOP_LEFT;");
		}

		ImageView msgPofilePic = new ImageView();
		msgHBox.getChildren().add(msgPofilePic);

		VBox msgVBox = new VBox();
		msgHBox.getChildren().add(msgVBox);
		msgVBox.setStyle("-fx-background-color:rgba(0,0,0,0.6);");

		TextArea msg = new TextArea(message);
		msg.setEditable(false);
		msg.setWrapText(true);
		msg.setPrefWidth(350);
		msg.setPrefHeight(75);
		msg.setId("outgoingMsgBox");

		Label time = new Label(timestamp);
		time.setStyle("-fx-text-fill:white;");
		time.setId("outgoingMessageTimestamp");
		Label senderName = new Label(username);

		senderName.setStyle("-fx-text-fill:white;");
		msgVBox.getChildren().add(msg);
		msgVBox.getChildren().add(time);
		msgVBox.getChildren().add(senderName);

	}

	@FXML
	void openChangeAvatar(MouseEvent event) {
		slide(avatarBox, avatarBtn);
	}

	@FXML
	void openChangePassword(MouseEvent event) {
		slide(passwordBox, passwordBtn);
	}

	@FXML
	void openChangeUsername(MouseEvent event) {
		slide(usernameBox, usernameBtn);
	}

	@FXML
	void updateUsername(MouseEvent event) {
		// check if username field is empty
		if (!changeUsername.getText().isEmpty()) {

			// update username
			name = changeUsername.getText();
			try {
				chatmessager.UpdateName(name);
				u.setUsername(name);
				loggedInAsName.setText(name);
				currentUser.setText(name);
				changeUsername.setText("");
			} catch (IOException | URISyntaxException | InterruptedException e) {
				nameError.setText("Server connection error, new username not sent");
			}
		}

		else {
			nameError.setText("Username field must not be blank!");
		}
	}

	@FXML
	void updatePassword(MouseEvent event) {
		// check whether current password was entered
		if (!currPass.getText().isEmpty()) {
			currPassError.setText("");

			// check if current password is correct
			if (u.getPassword().equals(currPass.getText().toString())) {
				currPassError.setText("");

				// check if new password and confirm password are blank
				if (!newPass.getText().isEmpty() && !confPass.getText().isEmpty()) {
					passError.setText("");

					// check if new pass and confirm pass match
					if (newPass.getText().toString().equals(confPass.getText().toString())) {
						passError.setText("");

						// update password
						try {
							chatmessager.UpdatePassword(newPass.getText());
							u.setPassword(newPass.getText());
							passOk.setText("Password Change Successful");
							currPass.setText("");
							newPass.setText("");
							confPass.setText("");
						} catch (IOException | URISyntaxException | InterruptedException e) {
							passError.setText("Server Connection error, password not updated");
						}
					} else {
						passError.setText("Passwords do not match!");
					}
				} else {
					passError.setText("Please fill out both fields!");
				}
			} else {
				currPassError.setText("Incorrect Password");
			}
		} else {
			currPassError.setText("Required");
		}
	}

	@FXML
	void updateAvatar(MouseEvent event) {
		int newPic = 0;

		if (button0.isSelected() == true) {
			newPic = 0;
		}
		if (button1.isSelected() == true) {
			newPic = 1;
		}
		if (button2.isSelected() == true) {
			newPic = 2;
		}
		if (button3.isSelected() == true) {
			newPic = 3;
		}
		if (button4.isSelected() == true) {
			newPic = 4;
		}
		if (button5.isSelected() == true) {
			newPic = 5;
		}
		if (button6.isSelected() == true) {
			newPic = 6;
		}
		if (button7.isSelected() == true) {
			newPic = 7;
		}
		if (button8.isSelected() == true) {
			newPic = 8;
		}

		try {
			chatmessager.UpdateAvatar(newPic);
		} catch (IOException | URISyntaxException | InterruptedException e) {
			System.out.println("error");
		}
		slide(avatarBox, avatarBtn);
	}

	// slide animations for settings tabs
	public void slide(VBox box, Button btn) {

		// reset labels on transitions
		passOk.setText("");
		passError.setText("");
		nameError.setText("");
		currPassError.setText("");
		currPass.setText("");
		newPass.setText("");
		confPass.setText("");
		changeUsername.setText("");

		// disable button during animation
		btn.setDisable(true);

		TranslateTransition slide = new TranslateTransition();
		slide.setDuration(Duration.millis(350));
		slide.setNode(box);

		// set opening animation, close other panes
		if (!box.isVisible()) {
			slide.setOnFinished(event -> btn.setDisable(false));
			closeAll(0);
			settingsPane.setVisible(true);
			box.setVisible(true);
			slide.setByY(1000);
			slide.play();
		}

		// set closing animations
		else {
			slide.setNode(box);
			slide.setByY(-1000);
			slide.setOnFinished(event -> {
				btn.setDisable(false);
				box.setVisible(false);
				settingsPane.setVisible(false);
			});
			slide.play();
		}
	}

	public void closeAll(int type) {

		// reset labels on transition
		passOk.setText("");
		passError.setText("");
		nameError.setText("");
		currPassError.setText("");
		currPass.setText("");
		newPass.setText("");
		confPass.setText("");
		changeUsername.setText("");

		// set close animation, close any open settings panes
		TranslateTransition closeAll = new TranslateTransition();
		closeAll.setDuration(Duration.millis(350));
		if (settingsPane.isVisible()) {
			if (avatarBox.isVisible()) {
				closeAll.setNode(avatarBox);
				closeAll.setOnFinished(event -> {
					avatarBox.setVisible(false);
					if (type == 1) {
						settingsPane.setVisible(false);
					}
				});
			} else if (usernameBox.isVisible()) {
				closeAll.setNode(usernameBox);
				closeAll.setOnFinished(event -> {
					usernameBox.setVisible(false);
					if (type == 1) {
						settingsPane.setVisible(false);
					}
				});
			} else if (passwordBox.isVisible()) {
				closeAll.setNode(passwordBox);
				closeAll.setOnFinished(event -> {
					passwordBox.setVisible(false);
					if (type == 1) {
						settingsPane.setVisible(false);
					}
				});
			}
		}
		closeAll.setByY(-1000);
		closeAll.play();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		UserHolder holder = (UserHolder) UserHolder.getInstance();// get instance of this user
		u = holder.getUser();// assign user from instance of this user
		chatmessager = holder.getChatter();// get chatter assigned to this user
		name = u.getUsername();// get username of this user

		loggedInAsName.setText(name);// displays username on left side pane
		loggedInAsName.setStyle("-fx-font-size:16px;-fx-font-weight:bold");
		currentUser.setText(u.getUsername());// what label does this belong to?

		scrollPane.vvalueProperty().bind(chatBox.heightProperty());// sets scroll pane to bottom position

		// Settings Pane: move all off-screen
		pane1.setVisible(false);
		TranslateTransition slide = new TranslateTransition();
		slide.setDuration(Duration.millis(1));
		slide.setNode(pane2);
		slide.setByY(-1000);
		slide.play();

		TranslateTransition setAvPane = new TranslateTransition();
		setAvPane.setDuration(Duration.millis(350));
		setAvPane.setByY(-1000);
		setAvPane.setNode(avatarBox);
		setAvPane.play();

		TranslateTransition setUnPane = new TranslateTransition();
		setUnPane.setDuration(Duration.millis(350));
		setUnPane.setByY(-1000);
		setUnPane.setNode(usernameBox);
		setUnPane.play();

		TranslateTransition setPwPane = new TranslateTransition();
		setPwPane.setDuration(Duration.millis(350));
		setPwPane.setByY(-1000);
		setPwPane.setNode(passwordBox);
		setPwPane.play();

		List<String[]> newMessages = Collections.synchronizedList(new ArrayList<String[]>());
		List<String[]> usersActive = Collections.synchronizedList(new ArrayList<String[]>());

		ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
		exec.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					String[][] veryNewMessages = chatmessager.GetComments();
					synchronized (newMessages) {
						newMessages.addAll(Arrays.asList(veryNewMessages));
					}

				} catch (IOException | URISyntaxException | InterruptedException e1) {
					e1.printStackTrace();
				}

				try {
					String[][] veryUsersActive = chatmessager.GetActive();
					synchronized (usersActive) {
						usersActive.clear();
						for (String[] s : veryUsersActive)
							usersActive.add(s);
					}
				} catch (IOException | InterruptedException | URISyntaxException e) {
					e.printStackTrace();
				}
			}
		}, 0, 2, TimeUnit.SECONDS);

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						// gets all messages not seen by this instance of chatter
						// and displays them in messages bubbles

						try {
							for (String[] s : newMessages)
								displayMessage(s[0], s[1], s[2]);
							newMessages.clear();
							
						} catch (MalformedURLException e1) {
							e1.printStackTrace();
						}

						// updates active user list
						try {
							activeUserBox.getChildren().clear();// clears old active user list
							for (String[] strings : usersActive) {
								if (!(u.getUsername().equals(strings[0]))) {// all active users except this user
									displayUsers(strings[0], strings[1]);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}, 0, 2000);
	}

	/*
	 * grabs the string number from the array and converts it into the corresponding
	 * image
	 * 
	 * Still a work in progress...
	 * 
	 * @return image that will display in the active users
	 * 
	 */
	public String getImage(String num) throws FileNotFoundException, URISyntaxException {
		String imagepath = "";

		switch (num) {
		case "0":
			imagepath = "avatar-0.png";
			break;
		case "1":
			imagepath = "avatar-1.png";
			break;
		case "2":
			imagepath = "avatar-2.png";
			break;
		case "3":
			imagepath = "avatar-3.png";
			break;
		case "4":
			imagepath = "avatar-4.png";
			break;
		case "5":
			imagepath = "avatar-5.png";
			break;
		case "6":
			imagepath = "avatar-6.png";
			break;
		case "7":
			imagepath = "avatar-7.png";
			break;
		case "8":
			imagepath = "avatar-8.png";
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + num);
		}

		return imagepath;

	}

	/**
	 * Adds containers and displays each active user in the container
	 * 
	 * @param username the user name of the user
	 * @param avatar   the avatar of the user
	 * @throws MalformedURLException
	 * @throws FileNotFoundException
	 * @throws URISyntaxException
	 */
	public void displayUsers(String username, String avatar)
			throws MalformedURLException, FileNotFoundException, URISyntaxException {
		activeUserBox.setSpacing(10);

		HBox userHBox = new HBox();
		userHBox.setSpacing(10);
		activeUserBox.getChildren().add(userHBox);

		Image image = new Image(getImage(avatar), 50, 50, true, true);
		ImageView userAvatar = new ImageView();
		userAvatar.setImage(image);

		Label user = new Label(username);
		user.setStyle("-fx-text-fill:white;-fx-font-size:18px; -fx-font-weight:bold");

		Circle circle = new Circle();
		circle.setStroke(Color.WHITE);
		circle.setStrokeWidth(2);
		circle.setFill(new ImagePattern(image));
		circle.setRadius(25);

		userHBox.getChildren().add(circle);
		userHBox.getChildren().add(user);

	}

}
