import {validationResult} from 'express-validator';

import User from "../models/user.js";
import bcrypt from "bcrypt";
import jwt from "jsonwebtoken";

export function getAll(req, res){
    User
        .find({})
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
        User
            .create({
                username: req.body.username,
                password: await bcrypt.hash(req.body.password, 10)
            })
            .then(newUser => {
                res.status(200).json(newUser);
            })
            .catch(err => {
                res.status(500).json({error: err});
            });
    }
}

    export function getOnce(req, res) {
        User
            .findOne({ "username": req.params.username })
            .then(doc => {
                res.status(200).json(doc);
            })
            .catch(err => {
                res.status(500).json({ error: err });
            });
    }

    export function patchOnce(req, res) {
        User
            .findOneAndUpdate({ "Username": req.params.username })
            .then(doc => {
                res.status(200).json(doc);
            })
            .catch(err => {
                res.status(500).json({ error: err });
            });
    }

    export function deleteOnce(req, res) {
        User
            .findOneAndRemove({ "username": req.params.username })
            .then(doc => {
                res.status(200).json(doc);
            })
            .catch(err => {
                res.status(500).json({ error: err });
            });
    }

    export async function login(req, res) {

        let jwtSecretKey = process.env.JWT_SECRET_KEY;
        // Our login logic starts here
        try {
            // Get user input

            const { username, password } = req.body;

            console.log(username)
            // Validate user input
            if (!(username && password)) {
                res.status(400).send("All input is required "+username);
            }
            // Validate if user exist in our database
            const login = await User.findOne({username});

            if (username && (await bcrypt.compare(password, login.password))) {
                // Create token
                // save user token
               const token = jwt.sign(
                    { username },
                    process.env.TOKEN_KEY+"",
                    {
                        expiresIn: "2h",
                    }
                );

                // save user token
                login.token = token;

                // user
                res.status(200).json(login);
            }
            else res.status(400).send("Invalid Credentials");
        } catch (err) {
            console.log(err);
        }
        // Our login logic ends here
    }

    export async function register (req, res){
        app.post("/register", async (req, res) => {

            // Our register logic starts here
            try {
                // Get user input
                const { username, password } = req.body;

                // Validate user input
                if (!(username && password)) {
                    res.status(400).send("All input is required");
                }

                // check if user already exist
                // Validate if user exist in our database
                const oldUser = await User.findOne({ username });

                if (oldUser) {
                    return res.status(409).send("User Already Exist. Please Login");
                }

                //Encrypt user password
                let encryptedPassword = await bcrypt.hash(password, 10);

                // Create user in our database
                const user = await User.create({
                    username: username, // sanitize: convert email to lowercase
                    password: encryptedPassword,
                });

                // Create token
                const token = jwt.sign(
                    { username },
                    process.env.TOKEN_KEY+"",
                    {
                        expiresIn: "2h",
                    }
                );
                // save user token
                user.token = token;

                // return new user
                res.status(201).json(user);
            } catch (err) {
                console.log(err);
            }
            // Our register logic ends here
        });
    }
