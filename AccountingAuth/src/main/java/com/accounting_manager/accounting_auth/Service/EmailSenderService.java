package com.accounting_manager.accounting_auth.Service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class EmailSenderService {

    private final JavaMailSender javaMailSender;

    @Value("${application.frontend.baseurl}")
    private String frontEndBaseUrl;
    @Value("${spring.mail.username}")
    private String senderEmail;

    public EmailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public boolean sendPasswordResetMail(String userEmail, String resetToken, String firstName) throws Exception {
        String html = "<table cellspacing = '0' border = '0' cellpadding = '0' width = '100%' bgcolor = '#f2f3f8'" +
                "style = '@import url(https://fonts.googleapis.com/css?family=Rubik:300,400,500,700|Open+Sans:300,400,600,700); font-family: 'Open Sans', sans-serif;' >" +
                "<tr>" +
                "<td><table style = 'background-color: #f2f3f8; max-width:670px; margin:0 auto;' width = '100%' border = '0'align = 'center' cellpadding = '0' cellspacing = '0' >" +
                "<tr><td style = 'height:80px;' > &nbsp;</td >" +
                "</tr>" +
                " <tr><td style = 'text-align:center;' ><a href =" + frontEndBaseUrl + " title = 'logo' target = '_blank' >" +
                "<img width = '160' src = 'https://i.imgur.com/Y2WHoLj.png' title = 'logo' alt = 'logo' > </a>" +
                " </td></tr>" +
                " <tr><td style = 'height:20px;' > &nbsp;</td ></tr>" +
                "<tr><td ><table width = '95%' border = '0' align = 'center' cellpadding = '0' cellspacing = '0'style = 'max-width:670px; background:#fff; border-radius:3px; text-align:center;-webkit-box-shadow:0 6px 18px 0 rgba(0,0,0,.06);-moz-box-shadow:0 6px 18px 0 rgba(0,0,0,.06);box-shadow:0 6px 18px 0 rgba(0,0,0,.06);' >" +
                "<tr ><td style = 'height:40px;' > &nbsp;</td > </tr >" +
                " <tr ><td style = 'padding:0 35px;' ><h1 style = 'color:#1e1e2d; font-weight:500; margin:0;font-size:32px;font-family:'Rubik',sans-serif;' > Get started</h1 >" +
                " <p style = 'font-size:15px; color:#455056; margin:8px 0 0; line-height:24px;' >We received a request to reset the password for your account. Please the button below to change your password</p> <br >" +
                "<a href=" + frontEndBaseUrl + "/change-password?token=" + resetToken + " style = 'background:#20e277;text-decoration:none !important; display:inline-block; font-weight:500; margin-top:24px; color:#fff;text-transform:uppercase; font-size:14px;padding:10px 24px;display:inline-block;border-radius:50px;'>Change Password</a>" +
                " <tr ><td style = 'height:40px;' > &nbsp;</td ></tr ></table >" +
                " </td ></tr >" +
                " <tr ><td style = 'text-align:center;' >" +
                " <p style = 'font-size:14px; color:rgba(69, 80, 86, 0.7411764705882353); line-height:18px; margin:0 0 0;' > &copy; <strong > www.AccountingManager.com </strong > </p >" +
                " </td ></tr >" +
                " <tr ><td style = 'height:80px;' > &nbsp;</td ></tr ></table ></td ></tr ></table >";

        MimeMessageHelper message = new MimeMessageHelper(javaMailSender.createMimeMessage(), "UTF-8");
        message.setFrom(senderEmail);
        message.setTo(userEmail);
        message.setText(html, true);
        message.setSubject("Password Reset");
        Boolean isSent = false;
        try {
            javaMailSender.send(message.getMimeMessage());
            isSent = true;
        } catch (Exception e) {
            log.error(e);
        }

        return isSent;
    }

    public boolean sendConfirmationMail(String userEmail, String confirmationToken) throws Exception {
        String html = "<table cellspacing = '0' border = '0' cellpadding = '0' width = '100%' bgcolor = '#f2f3f8'" +
                "style = '@import url(https://fonts.googleapis.com/css?family=Rubik:300,400,500,700|Open+Sans:300,400,600,700); font-family: 'Open Sans', sans-serif;' >" +
                "<tr>" +
                "<td><table style = 'background-color: #f2f3f8; max-width:670px; margin:0 auto;' width = '100%' border = '0'align = 'center' cellpadding = '0' cellspacing = '0' >" +
                "<tr><td style = 'height:80px;' > &nbsp;</td >" +
                "</tr>" +
                " <tr><td style = 'text-align:center;' ><a href =" + frontEndBaseUrl + " title = 'logo' target = '_blank' >" +
                "<img width = '160' src = 'https://i.imgur.com/Y2WHoLj.png' title = 'logo' alt = 'logo' > </a>" +
                " </td></tr>" +
                " <tr><td style = 'height:20px;' > &nbsp;</td ></tr>" +
                "<tr><td ><table width = '95%' border = '0' align = 'center' cellpadding = '0' cellspacing = '0'style = 'max-width:670px; background:#fff; border-radius:3px; text-align:center;-webkit-box-shadow:0 6px 18px 0 rgba(0,0,0,.06);-moz-box-shadow:0 6px 18px 0 rgba(0,0,0,.06);box-shadow:0 6px 18px 0 rgba(0,0,0,.06);' >" +
                "<tr ><td style = 'height:40px;' > &nbsp;</td > </tr >" +
                " <tr ><td style = 'padding:0 35px;' ><h1 style = 'color:#1e1e2d; font-weight:500; margin:0;font-size:32px;font-family:'Rubik',sans-serif;' > Get started</h1 >" +
                " <p style = 'font-size:15px; color:#455056; margin:8px 0 0; line-height:24px;' >Your account has been created on Accounting Manager web App. Please press the confirm button below,</p> <br >" +
                "<a href=" + frontEndBaseUrl + "/confirm?token=" + confirmationToken + " style = 'background:#20e277;text-decoration:none !important; display:inline-block; font-weight:500; margin-top:24px; color:#fff;text-transform:uppercase; font-size:14px;padding:10px 24px;display:inline-block;border-radius:50px;'>Confirm Email</a>" +
                " <tr ><td style = 'height:40px;' > &nbsp;</td ></tr ></table >" +
                " </td ></tr >" +
                " <tr ><td style = 'text-align:center;' >" +
                " <p style = 'font-size:14px; color:rgba(69, 80, 86, 0.7411764705882353); line-height:18px; margin:0 0 0;' > &copy; <strong > www.AccountingManager.com </strong > </p >" +
                " </td ></tr >" +
                " <tr ><td style = 'height:80px;' > &nbsp;</td ></tr ></table ></td ></tr ></table >";
        MimeMessageHelper message = new MimeMessageHelper(javaMailSender.createMimeMessage(), "UTF-8");
        message.setFrom(senderEmail);
        message.setText(html, true);
        message.setTo(userEmail);
        message.setSubject("Account Activation!");
        Boolean isSent = false;
        try {
            javaMailSender.send(message.getMimeMessage());
            isSent = true;
        } catch (Exception e) {
            log.error(e);
        }

        return isSent;
    }

    public boolean sendPasswordMail(String userEmail, String password) throws Exception {
        String html =
                "<table cellspacing = '0' border = '0' cellpadding = '0' width = '100%' bgcolor = '#f2f3f8'" +
                        "style = '@import url(https://fonts.googleapis.com/css?family=Rubik:300,400,500,700|Open+Sans:300,400,600,700); font-family: 'Open Sans', sans-serif;' >" +
                        "<tr>" +
                        "<td><table style = 'background-color: #f2f3f8; max-width:670px; margin:0 auto;' width = '100%' border = '0'align = 'center' cellpadding = '0' cellspacing = '0' >" +
                        "<tr><td style = 'height:80px;' > &nbsp;</td >" +
                        "</tr>" +
                        " <tr><td style = 'text-align:center;' ><a href =" + frontEndBaseUrl + " title = 'logo' target = '_blank' >" +
                        "<img width = '160' src = 'https://i.imgur.com/Y2WHoLj.png' title = 'logo' alt = 'logo' > </a>" +
                        " </td></tr>" +
                        " <tr><td style = 'height:20px;' > &nbsp;</td ></tr>" +
                        "<tr><td ><table width = '95%' border = '0' align = 'center' cellpadding = '0' cellspacing = '0'style = 'max-width:670px; background:#fff; border-radius:3px; text-align:center;-webkit-box-shadow:0 6px 18px 0 rgba(0,0,0,.06);-moz-box-shadow:0 6px 18px 0 rgba(0,0,0,.06);box-shadow:0 6px 18px 0 rgba(0,0,0,.06);' >" +
                        "<tr ><td style = 'height:40px;' > &nbsp;</td > </tr >" +
                        " <tr ><td style = 'padding:0 35px;' ><h1 style = 'color:#1e1e2d; font-weight:500; margin:0;font-size:32px;font-family:'Rubik',sans-serif;' > Get started</h1 >" +
                        " <p style = 'font-size:15px; color:#455056; margin:8px 0 0; line-height:24px;' >Your client account has been created on Accounting Manager web App. Below are your system generated credentials, <br ><strong > Please change the password immediately after login</strong >.</p >" +
                        " <span style = 'display:inline-block; vertical-align:middle; margin:29px 0 26px; border-bottom:1px solid #cecece; width:100px;' ></span >" +
                        " <p style = 'color:#455056; font-size:18px;line-height:20px; margin:0; font-weight: 500;' >" +
                        " <strong style = 'display: block;font-size: 13px; margin: 0 0 4px; color:rgba(0,0,0,.64); font-weight:normal;' > Username </strong >" + userEmail +
                        " <strong style = 'display: block; font-size: 13px; margin: 24px 0 4px 0; font-weight:normal; color:rgba(0,0,0,.64);' > Password </strong > " + password + "</p >" +
                        " <a href='" + frontEndBaseUrl + "/login'" + " style = 'background:#20e277;text-decoration:none !important; display:inline-block; font-weight:500; margin-top:24px; color:#fff;text-transform:uppercase; font-size:14px;padding:10px 24px;display:inline-block;border-radius:50px;'> Login to your Account</a>" +
                        " <tr ><td style = 'height:40px;' > &nbsp;</td ></tr ></table >" +
                        " </td ></tr >" +
                        " <tr ><td style = 'text-align:center;' >" +
                        " <p style = 'font-size:14px; color:rgba(69, 80, 86, 0.7411764705882353); line-height:18px; margin:0 0 0;' > &copy; <strong > www.AccountingManager.com </strong > </p >" +
                        " </td ></tr >" +
                        " <tr ><td style = 'height:80px;' > &nbsp;</td ></tr ></table ></td ></tr ></table >";
        MimeMessageHelper message = new MimeMessageHelper(javaMailSender.createMimeMessage(), "UTF-8");
        message.setFrom(senderEmail);
        message.setText(html, true);
        message.setTo(userEmail);
        message.setSubject("AccountingManager Client Account!");
        Boolean isSent = false;
        try {
            javaMailSender.send(message.getMimeMessage());
            isSent = true;
        } catch (Exception e) {
            log.error(e);
        }

        return isSent;
    }

    //feature not yet implemented
    public boolean sendSimpleMail(String to, String sub, String body) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(sub);
        mailMessage.setText(body);
        Boolean isSent = false;
        try {
            javaMailSender.send(mailMessage);
            isSent = true;
        } catch (Exception e) {
        }

        return isSent;
    }
}