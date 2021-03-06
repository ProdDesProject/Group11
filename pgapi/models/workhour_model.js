var db = require('../database');

var workhour = {
    getUsersWorkhours: function(workhour, callback) {
        if (typeof workhour.startDate === 'undefined' || typeof workhour.endDate === 'undefined') {
            return db.query('select * from schema1.workhours where userid=$1 order by starttime', [workhour.userid], callback);
        }
        else{
            return db.query('select * from schema1.workhours where userid=$1 and starttime >=$2 and starttime <$3 order by starttime', [workhour.userid, workhour.startDate, workhour.endDate], callback);
        }
    },
    postNewWorkhour: function(newWorkhour, callback) {
        console.log(newWorkhour);
        return db.query('insert into schema1.workhours(hoursid, userid, startTime, endTime, description) values($1,$2,$3,$4,$5)',
        [newWorkhour.hourid, newWorkhour.userid, newWorkhour.startTime, newWorkhour.endTime, newWorkhour.description],
        callback
        );
    },
    editWorkhour: function(newWorkhour, callback) {
        return db.query(
            'update schema1.workhours set  startTime=$1, endTime=$2, description=$3 where hoursid=$4',
            [ newWorkhour.startTime, newWorkhour.endTime, newWorkhour.description, newWorkhour.hoursid],
            callback
        );
    },
    deleteWorkhour: function(hoursid, callback) {
        return db.query('delete from schema1.workhours where hoursid=$1', [hoursid], callback);
    }
};

module.exports = workhour;