import express from 'express';
import { body } from 'express-validator';

import {getAll, getOnce, addOnce, patchOnce, deleteOnce, updateStatus, getByUserId, getByStatus} from "../controllers/orderController.js";

import multerConfig from "../middlewares/multer-config.js";

const router =express.Router();

router.route('/')
    .get(getAll)
    .get(body(['id']))
    .post(body(['user','product','status','responder']),
        addOnce)

router.route("/transporter/accept")
    .post(body(['id','status','responder']),patchOnce)


router.route("/myorders/:_id")
    .get(getByUserId)

router.route("/transporter/:status")
    .get(getByStatus)

router.route("/status")
    .post(body(['id','status']),updateStatus)

router.route("/add")
    .post(body(['user','product','quantity','status','responder']),addOnce)


export default router;