var express = require('express');
var router = express.Router();
var user = require('../models/user_model');
const { v4: uuidv4 } = require('uuid');
//const app = express();
const bcrypt = require('bcrypt');


/* GET users listing. */
router.get('/', function(req, res, next) {
  user.get(function(err,rows){
    if (err) {
      res.json(err);
    }
    else{
      res.json(rows.rows);
    }
  });
//  res.send('respond with a resource');
});


router.post('/', function(req, res, next) {
  const hashedPassword = bcrypt.hashSync(req.body.password, 10)
   
  const newUser = {
      id: uuidv4(),

      email: req.body.email,
      username: req.body.username,
      password: hashedPassword 
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

  //users.push(newUser);
  user.postNewUser(newUser, function(err, result) {
    if(err) {
      res.json(err);
    } else {
      if (result != false){
        res.json(newUser);
      }
      else{
        res.status(409).send();
      }
      
    }
  });

  //res.sendStatus(201);
});



module.exports = router;