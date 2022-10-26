import express from 'express';
import { body } from 'express-validator';

import {getAll, addOnce, getOnce, patchOnce, deleteOnce, login} from "../controllers/userController.js";
import multerConfig from "../middlewares/multer-config.js";

const router =express.Router();

router.route('/')
    .get(getAll)
    .post(multerConfig, body('username').isLength({min: 5}),
    body('password').isNumeric(),
    addOnce);

router.route("/:username")
    .get(getOnce)
    .patch(patchOnce)
    .delete(deleteOnce)

export default router;