import mongoose from "mongoose";
const {Schema, model} = mongoose

const pharmacySchema = new Schema(
    {
        name: {
            type: String,
            required: true
        }
    },
    {
        timestamps: true
    }
);

export default model('Pharmacy', pharmacySchema);