import {validationResult} from 'express-validator';

import Product from "../models/product.js";
import bcrypt from "bcrypt";

export function getAll(req, res){
    Product
        .find({})
        .then(docs => {
            res.status(200).json(docs);
        })
        .catch(err => {
            res.status(500).json({ error: err });
        });
}

export async function addOnce(req, res) {
    if (!validationResult(req).isEmpty()) {
        res.status(400).json({errors: validationResult(req).array()});
    } else {
        Product
            .create({
                name: req.body.name,
                image: req.body.image,
                price: req.body.price,
                pharmacy: req.body.pharmacy

            })
            .then(newProduct => {
                res.status(200).json(newProduct);
            })
            .catch(err => {
                res.status(500).json({error: err});
            });
    }
}

export function getOnce(req, res) {
    User
        .findOne({ "name": req.params.name })
        .then(doc => {
            res.status(200).json(doc);
        })
        .catch(err => {
            res.status(500).json({ error: err });
        });
}

export function patchOnce(req, res) {
    User
        .findOneAndUpdate({ "name": req.params.name})
        .then(doc => {
            res.status(200).json(doc);
        })
        .catch(err => {
            res.status(500).json({ error: err });
        });
}

export function deleteOnce(req, res) {
    Product
        .findOneAndRemove({ "name": req.params.name })
        .then(doc => {
            res.status(200).json(doc);
        })
        .catch(err => {
            res.status(500).json({ error: err });
        });
}