package com.example.internproject.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.internproject.dto.ContactFormDto;
import com.example.internproject.dto.OrderItemResponseDto;
import com.example.internproject.models.Orders;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class EmailService {

	private final JavaMailSender mailSender;

	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Async
	public void sendOrderConfirmation(Orders order) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setTo(order.getEmail());
			helper.setSubject("Your Pizza Order #" + order.getPublicId() + " is confirmed");
			helper.setText(buildEmailBody(order), true); // true = HTML

			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	private String buildEmailBody(Orders order) {
		StringBuilder sb = new StringBuilder();
		sb.append("<h2>Thank you for your order!</h2>");
		sb.append("<p><strong>Order ID:</strong> ").append(order.getPublicId()).append("</p>");
		sb.append("<p><strong>Email:</strong> ").append(order.getEmail()).append("</p>");
		sb.append("<p><strong>Status:</strong> ").append(order.getStatus()).append("</p>");
		sb.append("<p><strong>Pickup time:</strong> ").append(order.getPickupTime()).append("</p>");
		sb.append("<table border='1' cellpadding='5' cellspacing='0'>");
		sb.append("<thead><tr><th>Pizza</th><th>Size</th><th>Qty</th><th>Price</th></tr></thead><tbody>");

		List<OrderItemResponseDto> items = order.getOrderItems().stream()
				.map(i -> new OrderItemResponseDto(i.getPizza().getId(), i.getPizza().getName(), i.getSize().name(),
						i.getQuantity(), i.getPrice()))
				.toList();

		for (OrderItemResponseDto item : items) {
			BigDecimal rowTotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
			sb.append("<tr>").append("<td>").append(item.getPizzaName()).append("</td>").append("<td>")
					.append(item.getSize()).append("</td>").append("<td>").append(item.getQuantity()).append("</td>")
					.append("<td>").append(rowTotal).append(" €</td>").append("</tr>");
		}

		BigDecimal total = items.stream().map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		sb.append("</tbody>");
		sb.append("<tfoot><tr><td colspan='3'><strong>Total</strong></td><td>").append(total)
				.append(" €</td></tr></tfoot>");
		sb.append("</table>");

		sb.append("<p>Enjoy your pizza!</p>");
		return sb.toString();
	}

	@Async
	public void sendContactEmail(ContactFormDto form) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setTo("ttestone40@gmail.com"); // where the emails should go
			helper.setSubject("Contact Form: message from " + form.getName());

			StringBuilder body = new StringBuilder();
			body.append("Name: ").append(form.getName()).append("\n");
			body.append("Email: ").append(form.getEmail()).append("\n");
			body.append("Subject: ").append(form.getSubject()).append("\n");
			body.append("Phone: ").append(form.getPhoneNumber()).append("\n\n");
			body.append("Message:\n").append(form.getMessage());

			helper.setText(body.toString(), false); // false = plain text

			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to send contact email");
		}
	}
}
