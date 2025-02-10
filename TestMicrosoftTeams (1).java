package Microsoft.TeamsApp;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;

public class TestMicrosoftTeams {

	private AndroidDriver driver;

	@BeforeMethod
	public void setUp() throws MalformedURLException {

		DesiredCapabilities caps = new DesiredCapabilities();
	
		caps.setCapability("platformName", "Android");
		caps.setCapability("appium:deviceName", "507859337d76");
		caps.setCapability("appium:u`did", "507859337d76");
		caps.setCapability("appium:appPackage", "com.microsoft.teams");
	    caps.setCapability("appium:appActivity", "com.microsoft.skype.teams.views.activities.SplashActivity");
	    caps.setCapability("appium:automationName", "UiAutomator2");
	    caps.setCapability("appium:ensureWebviewsHavePages", true);
	    caps.setCapability("appium:nativeWebScreenshot", true);
	    caps.setCapability("appium:newCommandTimeout", 30);
	    caps.setCapability("appium:connectHardwareKeyboard", true);
	    
	    driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), caps);
	    HttpCommandExecutor executor = new HttpCommandExecutor(new URL("http://127.0.0.1:4723/wd/hub"));
	    driver = new AndroidDriver(executor, caps);
	


		
		 System.out.println("Succ.");
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

	}
	


    @Test(dataProvider = "signupData")
    public void testSignup(String email, String password, String confirmPassword, boolean Expected) {
    	
        WebElement signupButton = driver.findElement(By.id("com.microsoft.teams:id/signup"));
        signupButton.click();

        WebElement emailField = driver.findElement(By.id("com.microsoft.teams:id/email"));
        emailField.clear();
        emailField.sendKeys(email);

        WebElement nextButton = driver.findElement(By.id("com.microsoft.teams:id/next"));
        nextButton.click();

        WebElement passwordField = driver.findElement(By.id("com.microsoft.teams:id/password"));
        passwordField.clear();
        passwordField.sendKeys(password);

        WebElement confirmPasswordField = driver.findElement(By.id("com.microsoft.teams:id/confirmPassword"));
        confirmPasswordField.clear();
        confirmPasswordField.sendKeys(confirmPassword);

        WebElement createAccountButton = driver.findElement(By.id("com.microsoft.teams:id/createAccount"));
        createAccountButton.click();

        if (Expected) {
            WebElement welcomeScreen = driver.findElement(By.id("com.microsoft.teams:id/welcome"));
            Assert.assertTrue(welcomeScreen != null, "Signup failed");
        } else {
            WebElement errorMessage = driver.findElement(By.id("com.microsoft.teams:id/error_message"));
            Assert.assertTrue(errorMessage != null, "Error message not displayed for invalid signup");
        }
    }
    
	
	@DataProvider(name = "signupData")
    public Object[][] getSignupData() {
        return new Object[][] {
            {"afnan@axsos.academy.com", "afnan1234", "afnan1234", true}, 
            {"", "afnan1234", "afnan1234", false}, 
            {"afnan@axsos.academy.com", "", "afnan1234", false}, 
            {"afnan@axsos.academy.com", "afnan1234", "WrongPass", false} 
        };
    }
	 
	   @Test(dataProvider = "loginData",priority=1)
	    public void testLogin(String email, String password, boolean Expected) {
		WebElement getStart = driver.findElement(By.id("com.microsoft.teams:id/get_started_button"));
		getStart.click();
		WebElement emailField = driver.findElement(By.id("com.microsoft.teams:id/email"));
	        emailField.clear();
	        emailField.sendKeys(email);
	        WebElement nextButton = driver.findElement(By.id("com.microsoft.teams:id/next"));
	        nextButton.click();
	        WebElement passwordField = driver.findElement(By.id("com.microsoft.teams:id/password"));
	        passwordField.clear();
	        passwordField.sendKeys(password);
	        WebElement signInButton = driver.findElement(By.id("com.microsoft.teams:id/signIn"));
	        signInButton.click();
	        if (Expected) {
	            WebElement homeScreen = driver.findElement(By.id("com.microsoft.teams:id/home"));
	            Assert.assertTrue(homeScreen != null, "Login failed");
	        } else {
	            WebElement errorMessage = driver.findElement(By.id("com.microsoft.teams:id/error_message"));
	            Assert.assertTrue(errorMessage != null, "Error message not displayed for invalid login");
	        }
	    }
		 @DataProvider(name = "loginData")
		    public Object[][] getLoginData() {
		        return new Object[][] {
		            {"afnan.kharoof@axsos.academy.com", "Ghaith1234", true},
		            {"invalid#user@test.com", "hskl@@", false}, 
		            {"", "Test@1234", false}, 
		            {"testuser@example.com", "", false} 
		        };
		    }
		 
		 @Test
		    public void testSendMessage() {
		        WebElement chatTab = driver.findElement(By.id("com.microsoft.teams:id/chat_tab"));
		        chatTab.click();

		        WebElement newChatButton = driver.findElement(By.id("com.microsoft.teams:id/new_chat"));
		        newChatButton.click();

		        WebElement recipientField = driver.findElement(By.id("com.microsoft.teams:id/recipient"));
		        recipientField.sendKeys("testuser@example.com");

		        WebElement messageField = driver.findElement(By.id("com.microsoft.teams:id/message_input"));
		        messageField.sendKeys("Hello, this is a test message!");

		        WebElement sendButton = driver.findElement(By.id("com.microsoft.teams:id/send_button"));
		        sendButton.click();

		        WebElement lastMessage = driver.findElement(By.xpath("//android.widget.TextView[contains(@text, 'Hello, this is a test message!')]"));
		        Assert.assertTrue(lastMessage != null, "Message not sent successfully");
		    }

		    @Test
		    public void testReceiveMessage() {
		        WebElement chatTab = driver.findElement(By.id("com.microsoft.teams:id/chat_tab"));
		        chatTab.click();

		        WebElement lastReceivedMessage = driver.findElement(By.xpath("//android.widget.TextView[contains(@text, 'Hello, this is a reply!')]"));
		        Assert.assertTrue(lastReceivedMessage != null, "Message not received");
		    }
		    
		    @Test
		    public void testMakeAudioCall() {
		        WebElement callsTab = driver.findElement(By.id("com.microsoft.teams:id/calls_tab"));
		        callsTab.click();

		        WebElement newCallButton = driver.findElement(By.id("com.microsoft.teams:id/new_call"));
		        newCallButton.click();

		        WebElement contactField = driver.findElement(By.id("com.microsoft.teams:id/contact_search"));
		        contactField.sendKeys("testuser@example.com");

		        WebElement audioCallButton = driver.findElement(By.id("com.microsoft.teams:id/audio_call"));
		        audioCallButton.click();

		        WebElement callStatus = driver.findElement(By.id("com.microsoft.teams:id/call_status"));
		        Assert.assertTrue(callStatus != null, "Audio call failed");
		    }

		    @Test
		    public void testMakeVideoCall() {
		        WebElement callsTab = driver.findElement(By.id("com.microsoft.teams:id/calls_tab"));
		        callsTab.click();

		        WebElement newCallButton = driver.findElement(By.id("com.microsoft.teams:id/new_call"));
		        newCallButton.click();

		        WebElement contactField = driver.findElement(By.id("com.microsoft.teams:id/contact_search"));
		        contactField.sendKeys("testuser@example.com");

		        WebElement videoCallButton = driver.findElement(By.id("com.microsoft.teams:id/video_call"));
		        videoCallButton.click();

		        WebElement callStatus = driver.findElement(By.id("com.microsoft.teams:id/call_status"));
		        Assert.assertTrue(callStatus != null, "Video call failed");
		    }

		    @Test
		    public void testReceiveCall() {
		        WebElement incomingCall = driver.findElement(By.id("com.microsoft.teams:id/incoming_call"));
		        Assert.assertTrue(incomingCall != null, "No incoming call detected");

		        WebElement answerButton = driver.findElement(By.id("com.microsoft.teams:id/answer_call"));
		        answerButton.click();
		    }

		    @Test
		    public void testEndCall() {
		        WebElement endCallButton = driver.findElement(By.id("com.microsoft.teams:id/end_call"));
		        endCallButton.click();

		        WebElement callEnded = driver.findElement(By.id("com.microsoft.teams:id/call_ended"));
		        Assert.assertTrue(callEnded != null, "Call did not end successfully");
		    }
		    
		    @Test
		    public void testScheduleMeeting() {
		        WebElement calendarTab = driver.findElement(By.id("com.microsoft.teams:id/calendar_tab"));
		        calendarTab.click();

		        WebElement newMeetingButton = driver.findElement(By.id("com.microsoft.teams:id/new_meeting"));
		        newMeetingButton.click();

		        WebElement titleField = driver.findElement(By.id("com.microsoft.teams:id/meeting_title"));
		        titleField.sendKeys("Team Sync Meeting");

		        WebElement inviteField = driver.findElement(By.id("com.microsoft.teams:id/invite_people"));
		        inviteField.sendKeys("testuser@example.com");

		        WebElement scheduleButton = driver.findElement(By.id("com.microsoft.teams:id/schedule_button"));
		        scheduleButton.click();

		        WebElement confirmationMessage = driver.findElement(By.id("com.microsoft.teams:id/confirmation_message"));
		        Assert.assertTrue(confirmationMessage != null, "Meeting scheduling failed");
		    }

		    @Test
		    public void testJoinMeeting() {
		        WebElement calendarTab = driver.findElement(By.id("com.microsoft.teams:id/calendar_tab"));
		        calendarTab.click();

		        WebElement meetingItem = driver.findElement(By.xpath("//android.widget.TextView[contains(@text, 'Team Sync Meeting')]"));
		        meetingItem.click();

		        WebElement joinButton = driver.findElement(By.id("com.microsoft.teams:id/join_meeting"));
		        joinButton.click();

		        WebElement meetingScreen = driver.findElement(By.id("com.microsoft.teams:id/meeting_screen"));
		        Assert.assertTrue(meetingScreen != null, "Failed to join the meeting");
		    }

		    @Test
		    public void testCancelMeeting() {
		        WebElement calendarTab = driver.findElement(By.id("com.microsoft.teams:id/calendar_tab"));
		        calendarTab.click();

		        WebElement meetingItem = driver.findElement(By.xpath("//android.widget.TextView[contains(@text, 'Team Sync Meeting')]"));
		        meetingItem.click();

		        WebElement cancelButton = driver.findElement(By.id("com.microsoft.teams:id/cancel_meeting"));
		        cancelButton.click();

		        WebElement confirmCancel = driver.findElement(By.id("com.microsoft.teams:id/confirm_cancel"));
		        confirmCancel.click();

		        WebElement meetingItemAfterCancel = driver.findElement(By.xpath("//android.widget.TextView[contains(@text, 'Team Sync Meeting')]"));
		        Assert.assertTrue(meetingItemAfterCancel == null, "Meeting was not cancelled successfully");
		    }

		    @Test
		    public void testVerifyCalendarSync() {
		        WebElement calendarTab = driver.findElement(By.id("com.microsoft.teams:id/calendar_tab"));
		        calendarTab.click();

		        WebElement syncIndicator = driver.findElement(By.id("com.microsoft.teams:id/sync_status"));
		        Assert.assertTrue(syncIndicator != null, "Calendar sync failed");
		    }
		    
		    @Test
		    public void testFileSharing() throws InterruptedException {
		        loginToTeams();
		        navigateToChatOrTeam();
		        shareFile();
		        verifyFileSharing();
		    }
		    
		    private void loginToTeams() throws InterruptedException {
		        WebElement emailField = driver.findElement(By.id("com.microsoft.teams:id/emailField"));
		        emailField.sendKeys("afnan.kharoof@axsos.academy.com");

		        WebElement passwordField = driver.findElement(By.id("com.microsoft.teams:id/passwordField"));
		        passwordField.sendKeys("Ghaith123");
		        
		        WebElement loginButton = driver.findElement(By.id("com.microsoft.teams:id/loginButton"));
		        loginButton.click();
		        
		        driver.manage().timeouts().implicitlyWait(5000, TimeUnit.SECONDS);
		    }

		    private void navigateToChatOrTeam() {
		        WebElement chatTab = driver.findElement(By.id("com.microsoft.teams:id/chatTab"));
		        chatTab.click();
		    }

		    private void shareFile() throws InterruptedException {
		       
		        WebElement attachButton = driver.findElement(By.id("com.microsoft.teams:id/attachButton"));
		        attachButton.click();

		        WebElement fileOption = driver.findElement(By.id("com.microsoft.teams:id/fileOption"));
		        fileOption.click();

		        WebElement fileToSelect = driver.findElement(By.xpath("//android.widget.TextView[@text='example-file.txt']"));
		        fileToSelect.click();

		        WebElement sendButton = driver.findElement(By.id("com.microsoft.teams:id/sendButton"));
		        sendButton.click();

		        driver.manage().timeouts().implicitlyWait(3000, TimeUnit.SECONDS);
		    }

		    private void verifyFileSharing() {
		        WebElement sharedFile = driver.findElement(By.xpath("//android.widget.TextView[@text='example-file.txt']"));
		        Assert.assertTrue(sharedFile.isDisplayed(), "File sharing failed.");
		    }
		    
		    @Test
		    public void testTeamsAndChannels() throws InterruptedException {
		        loginToTeams();
		        navigateToTeams();
		        selectTeam();
		        interactWithChannel();
		        verifyMessageSent();
		    }

		    private void navigateToTeams() {
		        WebElement teamsTab = driver.findElement(By.id("com.microsoft.teams:id/teamsTab"));
		        teamsTab.click();
		    }

		    private void selectTeam() {
		        WebElement team = driver.findElement(By.xpath("//android.widget.TextView[@text='Your Team Name']"));
		        team.click();
		    }

		    private void interactWithChannel() throws InterruptedException {
		        WebElement channel = driver.findElement(By.xpath("//android.widget.TextView[@text='Your Channel Name']"));
		        channel.click();
		        driver.manage().timeouts().implicitlyWait(2000, TimeUnit.SECONDS);

		        
		        WebElement messageField = driver.findElement(By.id("com.microsoft.teams:id/messageField"));
		        messageField.sendKeys("Hello, this is a test message!");

		        WebElement sendButton = driver.findElement(By.id("com.microsoft.teams:id/sendButton"));
		        sendButton.click();

		        driver.manage().timeouts().implicitlyWait(3000, TimeUnit.SECONDS);
		    }

		    private void verifyMessageSent() {
		        WebElement message = driver.findElement(By.xpath("//android.widget.TextView[@text='Hello, this is a test message!']"));
		        Assert.assertTrue(message.isDisplayed(), "Message was not sent successfully.");
		    }
		    
		    @Test
		    public void testAppIntegration() throws InterruptedException {
		        loginToTeams();
		        navigateToApps();
		        launchApp("SharePoint");
		        verifyAppIntegration();
		    }

		    private void navigateToApps() {
		        WebElement appsTab = driver.findElement(By.id("com.microsoft.teams:id/appsTab"));
		        appsTab.click();
		    }

		    private void launchApp(String appName) {
		        WebElement searchBar = driver.findElement(By.id("com.microsoft.teams:id/searchField"));
		        searchBar.sendKeys(appName);
		        WebElement appItem = driver.findElement(By.xpath("//android.widget.TextView[@text='" + appName + "']"));
		        appItem.click();
		    }

		    private void verifyAppIntegration() {
		        WebElement appScreen = driver.findElement(By.id("com.microsoft.teams:id/appScreen"));
		        Assert.assertTrue(appScreen.isDisplayed(), "App is not integrated correctly.");
		    }
		    
		    @Test
		    public void testLoginSecurity() throws InterruptedException {
		        loginToTeams();

		       
		        verifyMultiFactorAuthentication();
		    }

		    private void verifyMultiFactorAuthentication() {
		        try {
		            WebElement mfaPage = driver.findElement(By.id("com.microsoft.teams:id/mfaPage"));
		            Assert.assertTrue(mfaPage.isDisplayed(), "Multi-Factor Authentication page is not displayed.");
		        } catch (Exception e) {
		            System.out.println("MFA not triggered.");
		        }
		    }
		    
		    





	
	

	
	@AfterMethod
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}
}
