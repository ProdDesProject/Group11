var express = require('express');
var router = express.Router();
var user = require('../models/user_model');
//const { v4: uuidv4 } = require('uuid');
//const app = express();
//const bcrypt = require('bcryptjs');

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

module.exports = router;
