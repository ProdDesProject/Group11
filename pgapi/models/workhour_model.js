var db = require('../database');

var workhour = {
    getUsersWorkhours: function(userid, callback) {
        return db.query('select * from schema1.workhours where userid=$1', [userid], callback);
    },
    postNewWorkhour: function(newWorkhour, callback) {
        console.log(newWorkhour);
        return db.query('insert into schema1.workhours(userid, startTime, endTime, description) values($1,$2,$3,$4)',
        [newWorkhour.userid, newWorkhour.startTime, newWorkhour.endTime, newWorkhour.description],
        callback
        );
    },
    editWorkhour: function(newWorkhour, callback) {
        return db.query(
            'update schema1.workhours set userid=$1, startTime=$2, endTime=$3, description=$4 where hoursid=$5',
            [newWorkhour.userid, newWorkhour.startTime, newWorkhour.endTime, newWorkhour.description, newWorkhour.hoursid],
            callback
        );
    },
    deleteWorkhour: function(hoursid, callback) {
        return db.query('delete from schema1.workhours where hoursid=$1', [hoursid], callback);
    }
};

module.exports = workhour;