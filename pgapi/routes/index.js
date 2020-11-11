var express = require('express');
var router = express.Router();
var user = require('../models/workhours_model');
//const { v4: uuidv4 } = require('uuid');
//const app = express();
//const bcrypt = require('bcryptjs');

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

/*
app.post('/users', (req, res) => {
  const hashedPassword = bcrypt.hash(req.body.password, 10)
    const newUser = {
      id: uuidv4(),

      email: req.body.email,
      username: req.body.username,
      password: hashedPassword,
    }

  if('email' in req.body == false ) {
    res.status(400);
    res.json({status: "Give a email"})
    return;
  }
  if('password' in req.body == false ) {
    res.status(400);
    res.json({status: "Give a password"})
    return;
  }
  if('username' in req.body == false ) {
    res.status(400);
    res.json({status: "Give a username"})
    return;
  }
  users.push(newUser);
  res.sendStatus(201);
});
*/
module.exports = router;
