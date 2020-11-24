var db = require('../database');

var user = {
    get: function(callback) {
        return db.query('select * from schema1.users', callback);
    },
    postNewUser: function(newUser, callback){
        console.log(newUser);
        db.query('select * from schema1.users where username = $1', [newUser.username], function(err,result){
            if (result.rowCount > 0){
                callback(null,false);
            }
            else{
                return db.query('insert into schema1.users(userid, email, username, userpassword) values($1,$2,$3, $4)',        
                [newUser.id ,newUser.email, newUser.username, newUser.password],
                callback
                );
            }
        });
        
    },
    getUserId: function(username, callback){
        return db.query('select userid from schema1.users where username = $1', [username],
        callback
        );
    },
    saveRefrestToken: function(newRefrestToken,expirydate,userid, callback){
        return db.query('insert into schema1.refreshtokens(jwtid,expirydate,userid) values($1,$2,$3)',[newRefrestToken,expirydate,userid],
        callback
        );
    },
    getRefrestToken: function(userId, callback){
        return db.query('select refrestToken from schema1.users where userid=\'$1\'',[userId],callback
        );
    }
};



module.exports = user;