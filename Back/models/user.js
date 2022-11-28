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
        role: {
            type: String,
            enum: ['admin','user','pharmacien'],
            required: true
        },
        token: {
            type: String },

    },
    {
        timestamps: true
    }
);

export default model('User', userSchema);