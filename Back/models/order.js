import mongoose from "mongoose";
import User from "./user.js";
import Product from "./product.js";
const {Schema, model} = mongoose

const orderSchema = new Schema(
    {
        user: {
            type: Schema.ObjectId, ref: "User",
        },
        product: [{
            type: Schema.ObjectId, ref: 'Product',
        }],
        quantity: {
            type: [Number] ,
        },
        status: {
            type: String,
        },
        responder: {
            type: Schema.ObjectId, ref: "User",
        }
    },
    {
        timestamps: true
    }
);

export default model('Order', orderSchema);