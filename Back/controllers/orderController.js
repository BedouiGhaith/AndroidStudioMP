import {validationResult} from 'express-validator';

import Order from "../models/order.js";

export function getAll(req, res){
    Order
        .find({}).populate([{path: 'product', populate: {path: 'pharmacy', populate: {path: 'owner'}}},{path: 'user'}])
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
        Order
            .create({
                user: req.body.user,
                product: req.body.product,
                quantity: req.body.quantity,
                status: req.body.status,
                price: req.body.price,
                responder: req.body.responder
            })
            .then(async newOrder => {
                res.status(200).json(await newOrder.populate([{path: 'product', populate: {path: 'pharmacy', populate: {path: 'owner'}}},{path: 'user'}]));
            })
            .catch(err => {
                res.status(500).json({error: err});
            });
    }
}

export function getOnce(req, res) {
    Order
        .findOne({ "_id": req.params._id })
        .then(doc => {
            res.status(200).json(doc);
        })
        .catch(err => {
            res.status(500).json({ error: err });
        });
}

export function getByUserId(req, res) {
    Order
        .find({ "order.user._id": req.params._id }).populate([{path: 'product', populate: {path: 'pharmacy', populate: {path: 'owner'}}},{path: 'user'}])
        .then(doc => {
            res.status(200).json(doc);
        })
        .catch(err => {
            res.status(500).json({ error: err });
        });
}

export function getByStatus(req, res) {
    Order
        .find({ "order.status": req.params.status }).populate([{path: 'product', populate: {path: 'pharmacy', populate: {path: 'owner'}}},{path: 'user'}])
        .then(doc => {
            res.status(200).json(doc);
        })
        .catch(err => {
            res.status(500).json({ error: err });
        });
}

export function patchOnce(req, res) {
    Order
        .findOneAndUpdate(req.body._id,{ "status": req.body.status, "responder":req.body.responder})
        .then(async doc => {
            res.status(200).json(await doc.populate([{
                path: 'product',
                populate: {path: 'pharmacy', populate: {path: 'owner'}}
            }, {path: 'user'}]));
        })
        .catch(err => {
            res.status(500).json({ error: err });
        });
}

export function updateStatus(req, res) {
    Order
        .findOneAndUpdate({ "_id": req.params._id},{"status": req.params.status})
        .then(doc => {
            res.status(200).json(doc);
        })
        .catch(err => {
            res.status(500).json({ error: err });
        });
}

export function deleteOnce(req, res) {
    Order
        .findOneAndRemove({ "_id": req.params._id })
        .then(doc => {
            res.status(200).json(doc);
        })
        .catch(err => {
            res.status(500).json({ error: err });
        });
}