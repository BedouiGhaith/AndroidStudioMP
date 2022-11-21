package com.androidproject.app.appcomponents.utils

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


class JavaMailAPI(
    private val mContext: Context,
    private val mEmail: String,
    private val mSubject: String,
    private val mMessage: String
) :
    AsyncTask<Void?, Void?, Void?>() {
    private var mSession: Session? = null
    private var mProgressDialog: ProgressDialog? = null
    override fun onPreExecute() {
        super.onPreExecute()
        mProgressDialog =
            ProgressDialog.show(mContext, "Sending message", "Please wait...", false, false)
    }

    override fun onPostExecute(aVoid: Void?) {
        super.onPostExecute(aVoid)
        mProgressDialog!!.dismiss()
        Toast.makeText(mContext, "Message Sent to "+mEmail, Toast.LENGTH_SHORT).show()
    }

    override fun doInBackground(vararg p0: Void?): Void? {
        val props = Properties()
        props["mail.smtp.host"] = "smtp.gmail.com"
        props["mail.smtp.socketFactory.port"] = "465"
        props["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.port"] = "465"
        mSession = Session.getDefaultInstance(props,
            object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(Utils.EMAIL, Utils.PASSWORD)
                }
            })
        try {
            val mm = MimeMessage(mSession)
            mm.setFrom(InternetAddress(Utils.EMAIL))
            mm.addRecipient(Message.RecipientType.TO, InternetAddress(mEmail))
            mm.subject = mSubject
            mm.setText(mMessage)
            Transport.send(mm)
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
        return null    }

}