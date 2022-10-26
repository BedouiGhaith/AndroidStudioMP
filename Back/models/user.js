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
        token: {
            type: String },

    },
    {
        timestamps: true
    }
);

export default model('User', userSchema);