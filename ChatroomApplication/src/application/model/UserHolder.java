package application.model;

import server.ChatMessager;

/**
 * Holds the user and chatmessager object for this user as an instance
 *
 */
public final class UserHolder {
	  
	  private User user;
	  private ChatMessager chatter;
	  private final static UserHolder INSTANCE = new UserHolder();
	  
	  private UserHolder() {}
	  
	  public static Object getInstance() {
	    return INSTANCE;
	  }
	  
	  public void setUser(User u) {
	    this.user = u;
	  }
	  
	  public User getUser() {
	    return this.user;
	  }
	  
	  public void setChatter(ChatMessager c) {
		  this.chatter = c;
		  
	  }
	  
	  public ChatMessager getChatter() {
		  return this.chatter;
	  }
	}
