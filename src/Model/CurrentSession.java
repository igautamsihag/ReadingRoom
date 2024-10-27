package Model;

// Singleton pattern current session class that manages a user's current log in session	
//		(AdityaPratapBhuyan, The Singleton Design Pattern: Ensuring a single instance in Java 2023)
public class CurrentSession {

	// declaring the current session class attributes
	private static CurrentSession instance;
    private User currentUser;

    // current session constructor to instantiate the current session for a user
    private CurrentSession() {}

    // getter method of current session that allows other components of applications to access current session of the user
    public static CurrentSession getInstance() {
        if (instance == null) {
            instance = new CurrentSession();
        }
        return instance;
    }

    // getter method to access the information of the current user
    public User getCurrentUser() {
        return currentUser;
    }

    // setter method to set the user
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
}


// References

// AdityaPratapBhuyan (2023) The Singleton Design Pattern: Ensuring a single instance in Java, DEV Community. Available at: https://dev.to/adityapratapbh1/the-singleton-design-pattern-ensuring-a-single-instance-in-java-5c1o (Accessed: 15 October 2024). 