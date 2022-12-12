import express from 'express';
import { body } from 'express-validator';

import {getAll, getOnce, addOnce, patchOnce, deleteOnce, updateStatus, getByUserId} from "../controllers/orderController.js";

import multerConfig from "../middlewares/multer-config.js";

const router =express.Router();

router.route('/')
    .get(getAll)
    .get(multerConfig, body(['id']))
    .post(multerConfig, body(['user','product','status','responder']),
        addOnce)

router.route("/:UserId")
    .patch(patchOnce)
    .delete(deleteOnce)

router.route("/myorders")
    .get(multerConfig, body(['_id']),getByUserId)

router.route("/status")
    .post(multerConfig, body(['id','status']),updateStatus)

router.route("/add")
    .post(body(['user','product','quantity','status','responder']),addOnce)


export default router;