package com.example.garbagecollection.bmw;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;


public class ReportingService extends BroadcastReceiver {

    Context con;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        new SendReport().execute();
           
    }

    public class SendReport extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.v(GlobalVariables.Server, "=== Pre ReportingService");
        }

        @Override
        protected String doInBackground(final String... params) {
            Log.v(GlobalVariables.Server, "=== Background SendReport");

                    try {
                        /*Properties props = new Properties();
                        props.put("mail.smtp.auth", "true");
                        props.put("mail.smtp.starttls.enable", "true");
                        props.put("mail.smtp.host", "smtp.gmail.com");//"smtp.gmail.com");//"smtp.office365.com"
                        props.put("mail.smtp.port", 587);//587

                        Session session = Session.getInstance(props,
                                new javax.mail.Authenticator() {
                                    protected PasswordAuthentication getPasswordAuthentication() {
                                        return new PasswordAuthentication("bhavin2887@gmail.com", "Vasanaroad1");
                                    }
                                });

                        Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress("bhavin2887@gmail.com", "DigitalPPE"));

                        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("bhavin2887@gmail.com"));
                        message.setSubject("Testing DigitalPPE");
                        message.setText("Hi,"+ "\n\n No spam to my email, please!");

                        /*Multipart multipart = new MimeMultipart();

                        String file = path +"/"+ startDate +"_dailyReport.csv";
                        String fileName = startDate +"_dailyReport.csv";
                        MimeBodyPart messageBodyPart = new MimeBodyPart();
                        DataSource source = new FileDataSource(file);
                        messageBodyPart.setDataHandler(new DataHandler(source));
                        messageBodyPart.setFileName(fileName);
                        multipart.addBodyPart(messageBodyPart);
                        message.setContent(multipart);*/

                        //Transport.send(message);

                        /*Message messageLogs = new MimeMessage(session);
                        messageLogs.setFrom(new InternetAddress(GlobalObjects.LOGS_USERNAME, "DigitalPPE"));
                        messageLogs.setRecipients(Message.RecipientType.TO, InternetAddress.parse(LOGS_USERNAME));
                        messageLogs.setSubject("Testing DigitalPPE");
                        messageLogs.setText("Hi,"+ "\n\n No spam to my email, please!");

                        Multipart multipartLog = new MimeMultipart();

                        String fileLog = path+"/"+ startDate +"_logcat.txt";
                        String fileNameLog = startDate +"_Logs.txt";
                        MimeBodyPart messageBodyPartLogs = new MimeBodyPart();
                        DataSource sourceLogs = new FileDataSource(fileLog);
                        messageBodyPartLogs.setDataHandler(new DataHandler(sourceLogs));
                        messageBodyPartLogs.setFileName(fileNameLog);

                        multipartLog.addBodyPart(messageBodyPartLogs);

                        messageLogs.setContent(multipartLog);

                        Transport.send(messageLogs);*/

                    } catch (Exception e) {
                    }
              

            return "Success";
        }

        @Override
        protected void onPostExecute(String result) {
            Log.v(GlobalVariables.Server, "=== Post ReportingService");

        }


        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }
}