const { response } = require('express');
var express = require('express');
var router = express.Router();
var workhours = require('../models/workhour_model');

router.get('/:id?', function(req,res,next) {
    if(req.params.id) {
        workhours.getUsersWorkhours(req.params.id, function(err, rows) {
            if (err) {
                res.json(err);
            } else {
                res.json(rows.rows);
            }
        });
    } else {
        res.status(400);
    }
});

router.post('/', function(req,res,next) {

    const newWorkhour = {
        userid: req.body.userid,
        startTime: req.body.startTime,
        endTime: req.body.endTime,
        description: req.body.description
      }
      
    workhours.postNewWorkhour(newWorkhour, function(err, count) {
        if (err) {
            res.json(err);
        } else {
        res.json(newWorkhour);
    }
    });
});

router.put('/:id', function(req, res, next) {
    const newWorkhour = {
        userid: req.body.userid,
        startTime: req.body.startTime,
        endTime: req.body.endTime,
        description: req.body.description,
        hoursid: req.params.id
      }

    workhours.editWorkhour(newWorkhour, function(err, rows) {
        if (err) {
            res.json(err);
        } else {
            res.json(newWorkhour);
        }
    });
});

router.delete('/:id', function(req, res, next) {
    workhours.deleteWorkhour(req.params.id, function(err, count) {
        if (err) {
            res.json(err);
        } else {
            res.json(count);
        }
    });
});

module.exports = router;