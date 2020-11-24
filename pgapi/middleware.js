const jwt = require('jsonwebtoken')

exports.verify = function(req, res, next){
    let accessToken = req.cookies.jwt
    let accessToken2
    let bearerHeader = req.headers['authorization']
    if (typeof bearerHeader !== 'undefined'){
        const bearer = bearerHeader.split(' ');
        accessToken2 = bearer[1];
    }

    //if there is no token stored in cookies, the request is unauthorized
    if (!accessToken2){
        return res.status(403).send()
    }

    let payload
    try{
        //use the jwt.verify method to verify the access token
        //throws an error if the token has expired or has a invalid signature
        payload = jwt.verify(accessToken2, process.env.ACCESS_TOKEN_SECRET)
        next()
    }
    catch(e){
        //if an error occured return request unauthorized error
        console.log(e)
        return res.status(401).send()
    }
}