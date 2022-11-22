import mongoose from "mongoose";
import Pharmacy from "./pharmacy.js";
const {Schema, model} = mongoose

const productSchema = new Schema(
    {
        name: {
            type: String,
            required: true
        },
        image: {
            type: String,
            required: true
        },
        price: {
            type: String,
            required: true
        },
        pharmacy: {
            type: Pharmacy,
            required: true
        },
    },
    {
        timestamps: true
    }
);

export default model('Product', productSchema);