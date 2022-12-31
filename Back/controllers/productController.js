
import Product from "../models/product.js";
import mongoose from "mongoose";

export function getAll(req, res){
    Product
        .find({}).populate({path: 'pharmacy', populate: {path: 'owner'}})
        .then(docs => {
            res.status(200).json(docs);
        })
        .catch(err => {
            res.status(500).json({ error: err });
        });
}

export async function addOnce(req, res) {
        (await Product
            .create({
                name: req.body.name,
                image: req.body.image,
                price: req.body.price,
                pharmacy: mongoose.Types.ObjectId(req.body.pharmacy)

            })).populate({path: 'pharmacy', populate: {path: 'owner'}})
            .then(newProduct => {
                res.status(200).json(newProduct);
            })
            .catch(err => {
                res.status(500).json({error: err});
            });
}

export function getOnce(req, res) {
    Product
        .findOne({ "name": req.params.name }).populate({path: 'pharmacy', populate: {path: 'owner'}})
        .then(doc => {
            res.status(200).json(doc);
        })
        .catch(err => {
            res.status(500).json({ error: err });
        });
}

export function patchOnce(req, res) {
    Product
        .findOneAndUpdate({ id: req.body._id},{name:req.body.name, price: req.body.price, image: req.body.image}).populate({path: 'pharmacy', populate: {path: 'owner'}})
        .then(doc => {
            res.status(200).json(doc);
        })
        .catch(err => {
            res.status(500).json({ error: err });
        });
}

export function deleteOnce(req, res) {
    Product
        .findOneAndRemove({ "_id": req.body._id }).populate({path: 'pharmacy', populate: {path: 'owner'}})
        .then(doc => {
            res.status(200).json(doc);
        })
        .catch(err => {
            res.status(500).json({ error: err });
        });
}