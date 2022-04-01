package entity;

public class Account {
	
	private String username, password, email;


	@Override
	public String toString() {
		return "Account [username=" + username + ", password=" + password + ", email=" + email + "]";
	}

	public Account(String username, String password, String email) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
	}
	
	public Account(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	

	public Account() {}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
