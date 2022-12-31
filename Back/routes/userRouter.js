import express from 'express';
import { body } from 'express-validator';

import {getAll, addOnce, getOnce, patchOnce, deleteOnce, login, reset, validate, editProfile, checkEmail, checkUsername} from "../controllers/userController.js";
import multerConfig from "../middlewares/multer-config.js";

const router =express.Router();

router.route('/')
    .post(body('username'),
    body('password'),
    addOnce);

router.route('/users').post(body(['_id']),getAll)

router.route('/edit')
    .post(body(['_id','username','password','email','phone','address','role']),editProfile)

router.route('/checkUsername/:username').get(checkUsername)

router.route('/checkEmail/:email').get(checkEmail)

router.route("/:username")
    .get(getOnce)
    .patch(patchOnce)
    .delete(deleteOnce)

router.route("/reset")
    .post(multerConfig, body('email'),reset)

router.route("/reset/validate")
    .post(multerConfig, body(['email','password']),validate)



export default router;