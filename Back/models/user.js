import mongoose from "mongoose";
const {Schema, model} = mongoose

const userSchema = new Schema(
    {
        username: {
            type: String,
            required: true
        },
        password: {
            type: String,
            required: true
        },
        email: {
            type: String,
            required: true
        },
        phone: {
            type: String,
            required: true
        },
        address: {
            type: String,
            required: true
        },
        token: {
            type: String },
        role: {
            type: String,
            enum: ['admin','user','pharmacien','transporter'],
            required: true
        },
    },
    {
        timestamps: true
    }
);

export default model('User', userSchema);