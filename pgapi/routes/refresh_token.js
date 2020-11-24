var express = require('express');
var router = express.Router();
const user = require('../models/user_model');
const jwt = require('jsonwebtoken')

router.post('/', function(req, res, next) {

    //get jwt_token from 
    let accessToken
    let bearerHeader = req.headers['authorization']
    if (typeof bearerHeader !== 'undefined'){
        const bearer = bearerHeader.split(' ');
        accessToken = bearer[1];
    }

    if (!accessToken){
        return res.status(403).send()
    }

    //verify jwt_token
    let payload
    try{
        payload = jwt.verify(accessToken, process.env.ACCESS_TOKEN_SECRET)
    }
    catch(e){
        return res.status(401).send()
    }
    let refershToken = req.cookies.refreshToken

    if (!refershToken){
        return res.status(403).send()
    }

    //get userid with username
    user.getUserId(username).then((dbQueryResult)=>{
        userId = dbQueryResult.rows[0].userid;
        //retrieve the refresh token from the database
        let tokenFromDatabase = user.getRefrestToken(userId);

        
        try{
            jwt.verify(refreshToken, process.env.REFRESH_TOKEN_SECRET)
        }
        catch(e){
            return res.status(401).send()
        }
    
        let newAccessToken = jwt.sign(payload, process.env.ACCESS_TOKEN_SECRET, 
        {
            algorithm: "HS256",
            expiresIn: process.env.ACCESS_TOKEN_LIFE
        })

        let newRefreshToken = jwt.sign(payload, process.env.REFRESH_TOKEN_SECRET, {
            algorithm: "HS256",
            expiresIn: process.env.REFRESH_TOKEN_LIFE
        })

        res.cookie("refreshToken", newRefreshToken, {secure: true, httpOnly: true})
        res.json(newAccessToken);
    })

});

exports.refresh = function (req, res){

    let accessToken = req.cookies.jwt

    if (!accessToken){
        return res.status(403).send()
    }

    let payload
    try{
        payload = jwt.verify(accessToken, process.env.ACCESS_TOKEN_SECRET)
    }
    catch(e){
        return res.status(401).send()
    }


    user.getUserId(username).then((dbQueryResult)=>{
        userId = dbQueryResult.rows[0].userid;
        //retrieve the refresh token from the database
        user.getRefrestToken(userId);
     }) 
    

    //verify the refresh token
    try{
        jwt.verify(refreshToken, process.env.REFRESH_TOKEN_SECRET)
    }
    catch(e){
        return res.status(401).send()
    }

    let newToken = jwt.sign(payload, process.env.ACCESS_TOKEN_SECRET, 
    {
        algorithm: "HS256",
        expiresIn: process.env.ACCESS_TOKEN_LIFE
    })

    res.cookie("jwt", newToken, {secure: true, httpOnly: true})
    res.send()
}