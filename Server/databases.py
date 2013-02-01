from google.appengine.ext import db

class LanCloudUsers(db.Model):
    uid = db.IntegerProperty()
    username = db.StringProperty()
    password = db.StringProperty()

class LanCloudFiles(db.Model):
    fid = db.IntegerProperty()
    filename = db.StringProperty()
    content = db.TextProperty()
    rev = db.IntegerProperty()
    uid = db.IntegerProperty()
