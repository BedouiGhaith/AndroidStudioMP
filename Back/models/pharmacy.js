import mongoose from "mongoose";
const {Schema, model} = mongoose

const pharmacySchema = new Schema(
    {
        name: {
            type: String,
            required: true
        },
        address: {
            type: String,
            required: true
        },
        owner: [{
            type: Schema.ObjectId, ref: 'User',
            required: true
        }]
    },
    {
        timestamps: true
    }
);

export default model('Pharmacy', pharmacySchema);