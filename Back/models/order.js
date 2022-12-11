import mongoose from "mongoose";
import User from "./user.js";
import Product from "./product.js";
const {Schema, model} = mongoose

const orderSchema = new Schema(
    {
        user: {
            type: User,
            required: true
        },
        product: [{
            type: Product,
            required: true
        }],
        status: {
            type: String,
            required: true
        },
        responder: {
            type: User,
            required: false
        }
    },
    {
        timestamps: true
    }
);

export default model('Order', orderSchema);