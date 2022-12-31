import express from 'express';
import { body } from 'express-validator';

import {getAll, getOnce, addOnce, patchOnce, deleteOnce, updateStatus, getByUserId, getByStatus, getPending, getFinished} from "../controllers/orderController.js";

const router =express.Router();

router.route('/')
    .get(getAll)
    .get(body(['id']))
    .post(body(['user','product','status','responder']),
        addOnce)

router.route("/transporter/accept")
    .post(body(['_id','status','responder']),patchOnce)


router.route("/myorders/:id")
    .get(getByUserId)



router.route("/transporter/onroute/:_id")
    .get(getPending)

router.route("/transporter/finished/:_id")
    .get(getFinished)

router.route("/transporter/:status/:id")
    .get(getByStatus)

router.route("/status")
    .post(body(['id','status']),updateStatus)

router.route("/add")
    .post(body(['user','product','quantity','status','responder']),addOnce)


export default router;