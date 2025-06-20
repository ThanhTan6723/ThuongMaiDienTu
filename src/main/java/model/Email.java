package model;

import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Transport;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import java.io.File;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Email {

	private static final String FROM_EMAIL = "feanfoodkorea@gmail.com";
	private static final String PASSWORD = "mvfe qrhy crej mbks";

	// ExecutorService to handle asynchronous email sending
	private static final ExecutorService executor = Executors.newFixedThreadPool(5);

	// SMTP properties
	private static final Properties PROPERTIES = new Properties();

	static {
		PROPERTIES.put("mail.smtp.host", "smtp.gmail.com");
		PROPERTIES.put("mail.smtp.port", "587");
		PROPERTIES.put("mail.smtp.auth", "true");
		PROPERTIES.put("mail.smtp.starttls.enable", "true");
	}

	public static void sendEmailAsync(String to, String title, String content) {
		executor.submit(() -> sendEmail(to, title, content));
	}

	public static boolean sendEmail(String to, String title, String content) {
		Session session = createSession();
		try {
			MimeMessage message = createEmailMessage(session, to, title, content);
			Transport.send(message);
			System.out.println("Email sent successfully to: " + to);
			return true;
		} catch (MessagingException e) {
			System.out.println("Failed to send email to: " + to);
			e.printStackTrace();
			return false;
		}
	}

	public static void sendEmailWithAttachmentAsync(String to, String title, String content, File attachment) {
		executor.submit(() -> sendEmailWithAttachment(to, title, content, attachment));
	}

	public static boolean sendEmailWithAttachment(String to, String title, String content, File attachment) {
		Session session = createSession();
		try {
			MimeMessage message = createEmailWithAttachment(session, to, title, content, attachment);
			Transport.send(message);
			System.out.println("Email with attachment sent successfully to: " + to);
			return true;
		} catch (Exception e) {
			System.out.println("Failed to send email with attachment to: " + to);
			e.printStackTrace();
			return false;
		}
	}

	public static void shutdownEmailExecutor() {
		executor.shutdown();
		try {
			if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
				executor.shutdownNow();
			}
		} catch (InterruptedException e) {
			executor.shutdownNow();
		}
	}

	private static Session createSession() {
		Authenticator auth = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
			}
		};
		return Session.getInstance(PROPERTIES, auth);
	}

	private static MimeMessage createEmailMessage(Session session, String to, String title, String content) throws MessagingException {
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(FROM_EMAIL));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
		message.setSubject(title);
		message.setSentDate(new Date());
		message.setContent(content, "text/html; charset=UTF-8");
		return message;
	}

	private static MimeMessage createEmailWithAttachment(Session session, String to, String title, String content, File attachment) throws Exception {
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(FROM_EMAIL));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
		message.setSubject(title);
		message.setSentDate(new Date());

		MimeBodyPart textPart = new MimeBodyPart();
		textPart.setText(content, "UTF-8");

		MimeBodyPart filePart = new MimeBodyPart();
		filePart.attachFile(attachment);

		MimeMultipart multipart = new MimeMultipart();
		multipart.addBodyPart(textPart);
		multipart.addBodyPart(filePart);

		message.setContent(multipart);
		return message;
	}

	public static void main(String[] args) {
		// Test sending a simple email
		sendEmailAsync("recipient@example.com", "Test Email", "This is a test email sent asynchronously.");

		// Test sending an email with attachment
		File testFile = new File("test_attachment.txt");
		sendEmailWithAttachmentAsync("recipient@example.com", "Test Email with Attachment", "This is a test email with attachment.", testFile);

		// Shut down executor after testing
		shutdownEmailExecutor();
	}
}
