import express from 'express';
import { body } from 'express-validator';

import {login} from "../controllers/userController.js";
import multerConfig from "../middlewares/multer-config.js";

const router =express.Router();

router.route("/login").post(multerConfig, body('username').isLength({min: 5}),
    body('password').isNumeric(),login)

router.route("/registry").post(multerConfig, body('username').isLength({min: 5}),
    body('password').isNumeric(),login)


export default router;