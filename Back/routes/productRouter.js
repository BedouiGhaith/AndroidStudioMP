import express from 'express';
import { body } from 'express-validator';

import {getAll, getOnce, addOnce, patchOnce, deleteOnce} from "../controllers/productController.js";

import multerConfig from "../middlewares/multer-config.js";

const router =express.Router();

router.route('/')
    .get(getAll)
    .post(body(['name','image', 'price', 'pharmacy']), addOnce)

router.route('/modify')
    .post(body(['_id','name','image', 'price', 'pharmacy']), patchOnce)

router.route('/delete')
    .post(body(['_id','name','image', 'price', 'pharmacy']), deleteOnce)


router.route('/')
    .get(getAll)
    .post(body(['name','image', 'price', 'pharmacy']), addOnce)


router.route("/:name")
    .get(getOnce)
    .delete(deleteOnce)

export default router;