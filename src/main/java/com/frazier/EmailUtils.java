package com.frazier;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

@SuppressWarnings("unused")
public class EmailUtils {
    private static final String host = "smtp.qq.com";
    private static final String username = "3083027818@qq.com";
    private static final String password = "jqurelskutjsdhbj";

    /**
     * @param contactEmails 收件人
     * @param subject       主题
     * @param text          内容
     * @param bookName      书名
     * @param book          附件
     */
    public static int sendAttachmentMail(List<String> contactEmails, String subject, String text, String bookName, File book) {
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true"); // 发送服务器需要身份验证
        props.setProperty("mail.transport.protocol", "smtp"); // 发送邮件协议名称
        props.setProperty("mail.smtp.host", host); // 设置邮件服务器主机
        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(username)); // 发件人
            msg.setSubject(subject);
            InternetAddress[] sendTo = new InternetAddress[contactEmails.size()];
            for (int i = 0; i < contactEmails.size(); i++) {
                System.out.println("发送给:" + contactEmails.get(i));
                //logger.info("发送给:"+contactEmails.get(i));
                sendTo[i] = new InternetAddress(contactEmails.get(i));
            }
            msg.setRecipients(javax.mail.internet.MimeMessage.RecipientType.TO, sendTo);
            // 设置正文
            BodyPart bp = new MimeBodyPart();
            bp.setContent("<h1>" + text + "</h1>", "text/html;charset=utf-8");
            MimeMultipart mp = new MimeMultipart();
            mp.addBodyPart(bp);
            // 设置附件
            if (book != null) {
                bp = new MimeBodyPart();
                FileDataSource fds = new FileDataSource(book);
                bp.setDataHandler(new DataHandler(fds));
                System.out.println(bookName);
                bp.setFileName(MimeUtility.encodeText(bookName, "UTF-8", "B"));
                mp.addBodyPart(bp);
            }
            msg.setContent(mp);

            Transport.send(msg);
            return 1;
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
