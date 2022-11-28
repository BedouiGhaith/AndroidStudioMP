import {validationResult} from 'express-validator';

import Pharmacy from "../models/pharmacy.js";
import mongoose from "mongoose";

export function getAll(req, res){
    Pharmacy
        .find({}).populate('owner', ['email', 'phone', 'address'])
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
        Pharmacy
            .create({
                name: req.body.name,
                address: req.body.address,
                owner: mongoose.Types.ObjectId(req.body.owner)


            })
            .then(newPharmacy => {
                res.status(200).json(newPharmacy);
            })
            .catch(err => {
                res.status(500).json({error: err});
            });
    }
}

export function getOnce(req, res) {
    Pharmacy
        .findOne({ "name": req.params.name })
        .then(doc => {
            res.status(200).json(doc);
        })
        .catch(err => {
            res.status(500).json({ error: err });
        });
}

export function patchOnce(req, res) {
    Pharmacy
        .findOneAndUpdate({ "name": req.params.name})
        .then(doc => {
            res.status(200).json(doc);
        })
        .catch(err => {
            res.status(500).json({ error: err });
        });
}

export function deleteOnce(req, res) {
    Pharmacy
        .findOneAndRemove({ "name": req.params.name })
        .then(doc => {
            res.status(200).json(doc);
        })
        .catch(err => {
            res.status(500).json({ error: err });
        });
}