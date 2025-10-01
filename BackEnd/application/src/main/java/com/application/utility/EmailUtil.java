package com.application.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Date;

@Component
public class EmailUtil {
    
    // Outlook SMTP configuration | Outlook SMTP配置
    private static final String SMTP_HOST = "smtp-mail.outlook.com";
    private static final String SMTP_PORT = "587";
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Value("${spring.mail.password}")
    private String fromPassword;
    /**
     * Send verification code email | 发送验证码邮件
     * @param toEmail Target email address | 目标邮箱地址
     * @param verificationCode 6-digit verification code | 6位验证码
     * @return true if sent successfully, false otherwise | 发送成功返回true，否则返回false
     */
    public boolean sendVerificationCode(String toEmail, String verificationCode) {
        try {
            // Create email session | 创建邮件会话
            Session session = createEmailSession();
            
            // Create message | 创建邮件消息
            MimeMessage message = new MimeMessage(session);
            
            // Set sender | 设置发件人
            message.setFrom(new InternetAddress(fromEmail, "JAVAEE Application"));
            
            // Set recipient | 设置收件人
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            
            // Set subject | 设置主题
            message.setSubject("Email Verification Code - JAVAEE Application", "UTF-8");
            
            // Set email content | 设置邮件内容
            String emailContent = buildEmailContent(verificationCode);
            message.setContent(emailContent, "text/html; charset=UTF-8");
            
            // Set sent date | 设置发送时间
            message.setSentDate(new Date());
            
            // Send email | 发送邮件
            Transport.send(message);
            
            System.out.println("✅ Verification code sent successfully to: " + toEmail);
            return true;
            
        } catch (Exception e) {
            System.err.println("❌ Failed to send verification code to: " + toEmail);
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Create email session with Outlook SMTP settings | 创建带有Outlook SMTP设置的邮件会话
     */
    private Session createEmailSession() {
        Properties properties = new Properties();
        
        // SMTP server configuration | SMTP服务器配置
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.starttls.required", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        
        // Create authenticator | 创建认证器
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, fromPassword);
            }
        };
        
        return Session.getInstance(properties, authenticator);
    }
    
    /**
     * Build professional email content | 构建专业的邮件内容
     */
    private String buildEmailContent(String verificationCode) {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Email Verification</title>
                <style>
                    body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.6; color: #333; background-color: #f4f4f4; margin: 0; padding: 20px; }
                    .container { max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 10px; box-shadow: 0 4px 10px rgba(0,0,0,0.1); overflow: hidden; }
                    .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; text-align: center; }
                    .header h1 { margin: 0; font-size: 28px; font-weight: 300; }
                    .content { padding: 40px 30px; }
                    .verification-code { background-color: #f8f9fa; border: 2px dashed #007bff; border-radius: 8px; padding: 20px; text-align: center; margin: 30px 0; }
                    .code { font-size: 32px; font-weight: bold; color: #007bff; letter-spacing: 8px; font-family: 'Courier New', monospace; }
                    .warning { background-color: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; margin: 20px 0; border-radius: 4px; }
                    .footer { background-color: #f8f9fa; padding: 20px; text-align: center; font-size: 12px; color: #6c757d; border-top: 1px solid #e9ecef; }
                    .button { display: inline-block; background-color: #007bff; color: white; padding: 12px 30px; text-decoration: none; border-radius: 5px; margin: 20px 0; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🔐 Email Verification</h1>
                        <p>JAVAEE Application Security Service</p>
                    </div>
                    
                    <div class="content">
                        <p>Dear Valued User,</p>
                        
                        <p>Thank you for using our JAVAEE Application. To ensure the security of your account, please verify your email address using the verification code below.</p>
                        
                        <div class="verification-code">
                            <p style="margin: 0; font-size: 16px; color: #666;">Your Verification Code:</p>
                            <div class="code">%s</div>
                            <p style="margin: 0; font-size: 14px; color: #888;">Valid for 10 minutes</p>
                        </div>
                        
                        <p><strong>How to use this code:</strong></p>
                        <ol>
                            <li>Return to the application verification page</li>
                            <li>Enter the 6-digit code exactly as shown above</li>
                            <li>Click "Verify" to complete the process</li>
                        </ol>
                        
                        <div class="warning">
                            <strong>⚠️ Security Notice:</strong><br>
                            • This code will expire in 10 minutes<br>
                            • Do not share this code with anyone<br>
                            • If you didn't request this verification, please ignore this email
                        </div>
                        
                        <p>If you have any questions or need assistance, please contact our support team.</p>
                        
                        <p>Best regards,<br>
                        <strong>JAVAEE Application Team</strong><br>
                        Security & Verification Department</p>
                    </div>
                    
                    <div class="footer">
                        <p>© 2024 JAVAEE Application. All rights reserved.</p>
                        <p>This is an automated message, please do not reply to this email.</p>
                        <p>🛡️ Your security is our priority | 🔒 End-to-end encrypted communication</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(verificationCode);
    }
    
    /**
     * Generate 6-digit verification code | 生成6位验证码
     */
    public String generateVerificationCode() {
        return String.format("%06d", (int)(Math.random() * 1000000));
    }

}