import xoauth2 from "xoauth2"
import nodemailer from "nodemailer"

import dotenv from "dotenv"
dotenv.config()




export async function sendEmail(email, code) {// Use Smtp Protocol to send Email
    let smtpTransport = nodemailer.createTransport({
        service: 'gmail',
        port:465,
        secure: true,
        secureConnection: false,
        auth: {
            user: 'medtech.tn@gmail.com',
            pass: 'yplbbdhscxxrwbjj',
        },
        tls:{
            rejectUnAuthorized:true
        }
    })

    const mail = {
        from: "MedTech@gmail.com>",
        to: email,
        subject: "Password reset",
        text: "You are resetting your password: " + code,
    };

    smtpTransport.sendMail(mail, function (error, response) {
        if (error) {
            console.log(error);
        } else {
            console.log("Message sent: " + response.message);
        }

        smtpTransport.close();
    });
}

export function makeid(length) {
    let result = '';
    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    const charactersLength = characters.length;
    for (let i = 0; i < length; i++ ) {
        result += characters.charAt(Math.floor(Math.random() * charactersLength));
    }
    return result;
}
