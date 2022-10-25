import express from 'express';
import { body } from 'express-validator';

import {login, register} from "../controllers/userController.js";
import multerConfig from "../middlewares/multer-config.js";

const router =express.Router();

router.route("/login").post(multerConfig, body('username').isLength({min: 5}),
    body('password').isNumeric(),login)

router.route("/register").post(multerConfig, body('username').isLength({min: 5}),
    body('password').isNumeric(),register)


export default router;