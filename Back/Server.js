import express from 'express';
import mongoose from "mongoose";
import user from "./models/user.js";
import userRouter from "./routes/userRouter.js";
import productRouter from "./routes/productRouter.js";
import pharmacyRouter from "./routes/pharmacyRouter.js";
import authentificationRouter from "./routes/authentificationRouter.js"
import auth from "./middlewares/auth.js"
import product from "./models/product.js";
import pharmacy from "./models/pharmacy.js";
const app = express();

const hostname = '127.0.0.1'
const port = process.env.PORT || 9090;
const databaseName = 'AndroidMP';

mongoose.set('debug', true);
mongoose.Promise = global.Promise;

mongoose
    .connect(`mongodb://localhost:27017/${databaseName}`)
    .then(() => {
        console.log(`Connected to ${databaseName}`);
    })
    .catch(err => {
        console.log(err);
    });

app.use(express.json()); // Pour analyser application/json
app.use(express.urlencoded({ extended: true })); // Pour analyser application/x-www-form-urlencoded

app.use('/user', userRouter);

app.use('/product', productRouter);

app.use('/pharmacy', pharmacyRouter);

app.post('/register', authentificationRouter);

app.post("/login",authentificationRouter);


app.get("/welcome", auth, (req, res) => {
    res.status(200).send("Welcome 🙌 ");
});


app.listen(port, () => {
    console.log(`Server running at http://localhost:${port}/`);
  });