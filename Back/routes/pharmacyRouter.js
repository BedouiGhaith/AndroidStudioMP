import express from 'express';
import { body } from 'express-validator';

import {getAll, getOnce, addOnce, patchOnce, deleteOnce} from "../controllers/pharmacyController.js";

import multerConfig from "../middlewares/multer-config.js";

const router =express.Router();

router.route('/')
    .get(getAll)
    .post(multerConfig, body(['name', 'address']),
        addOnce)

router.route("/:name")
    .get(getOnce)
    .patch(patchOnce)
    .delete(deleteOnce)

export default router;