package mail;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

/**
 * @author dinghy
 * @date
 */
public class SendmailUtil {
    private static String smtpHost = "smtp.126.com";
    private static String mailBox = "xx@126.com";
    //授权码
    private static String authCode = "xxx";

    public static void sendMail(String toEmailAddress, String emailTitle, String emailContent) throws MessagingException, IOException, GeneralSecurityException {

        Properties props = new Properties();
        // 设置邮件服务器主机名
        props.setProperty("mail.host", smtpHost);
        props.setProperty("mail.transport.protocol", "smtp");
        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        // 使用 STARTTLS安全连接
        props.put("mail.smtp.starttls.enable", "true");
        // 开启debug调试
        props.setProperty("mail.debug", "true");
        /**SSL认证，注意腾讯邮箱是基于SSL加密的，所以需要开启才可以使用**/
       MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        props.put("mail.smtp.ssl.socketFactory", sf);
        //创建会话
        Session session = Session.getInstance(props);
        //获取邮件对象
        //发送的消息，基于观察者模式进行设计的
        Message msg = new MimeMessage(session);
        //发送方
        msg.setFrom(new InternetAddress(mailBox,"英雄联盟", "UTF-8"));
        //设置标题
        msg.setSubject(emailTitle);
        //设置邮件内容
        //设置显示的发件时间
        msg.setSentDate(new Date());
        //设置邮件内容
        msg.setText(emailContent);
        //得到邮差对象
        Transport transport = session.getTransport();
        //连接自己的邮箱账户
        //密码不是自己QQ邮箱的密码，而是在开启SMTP服务时所获取到的授权码
        //connect(host, user, password)
        transport.connect(smtpHost, mailBox, authCode);
        //发送邮件
        transport.sendMessage(msg, new Address[]{new InternetAddress(toEmailAddress)});
        //将该邮件保存到本地
      /*  OutputStream out = new FileOutputStream("MyEmail.eml");
        msg.writeTo(out);
        out.flush();
        out.close();*/

        transport.close();

    }

    public static void main(String[] args) throws IOException, MessagingException, GeneralSecurityException {
        sendMail("test@qq.com", "测试消息发送", "啦啦啦德玛西亚");
    }
}
