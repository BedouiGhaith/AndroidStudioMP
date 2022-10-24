import express from 'express';

const app = express();

const hostname = '127.0.0.1'
const port = process.env.PORT || 9090;

app.get('/', (req, res) => {
    res.send('Hello World!');
})

app.listen(port, () => {
    console.log(`Server running at http://localhost:${port}/`);
  });